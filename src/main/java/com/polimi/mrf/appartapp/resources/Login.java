package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.Gender;
import com.polimi.mrf.appartapp.HashGenerator;
import com.polimi.mrf.appartapp.UserAdapter;
import com.polimi.mrf.appartapp.beans.UserAuthServiceBean;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.CredentialsUser;
import com.polimi.mrf.appartapp.entities.GoogleUser;
import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserAuthToken;
import com.polimi.mrf.appartapp.google.GoogleTokenVerifier;
import com.polimi.mrf.appartapp.google.GoogleUserInfo;
import org.apache.commons.lang3.RandomStringUtils;

import javax.ejb.EJB;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/login")
public class Login {

    private static final int COOKIE_TIMEOUT=15768000; //6 months

    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @EJB(name = "com.polimi.mrf.appartapp.beans/UserAuthServiceBean")
    UserAuthServiceBean userAuthServiceBean;

    public static HttpServletResponse generateNewTokenAndAppendToResponse(HttpServletResponse response, UserAuthServiceBean userAuthServiceBean, User user) throws UnsupportedEncodingException {
        UserAuthToken newToken = new UserAuthToken();

        String selector = RandomStringUtils.randomAlphanumeric(12);
        String rawValidator =  RandomStringUtils.randomAlphanumeric(64);

        String hashedValidator = null;

        hashedValidator = HashGenerator.generateSHA256(rawValidator);
        newToken.setSelector(selector);
        newToken.setValidator(hashedValidator);
        newToken.setLastUse(new Date());

        newToken.setUser(user);

        userAuthServiceBean.create(newToken);

        Cookie cookieSelector = new Cookie("selector", selector);
        cookieSelector.setMaxAge(COOKIE_TIMEOUT);

        Cookie cookieValidator = new Cookie("validator", rawValidator);
        cookieValidator.setMaxAge(COOKIE_TIMEOUT);

        response.addCookie(cookieSelector);
        response.addCookie(cookieValidator);
        return response;
    }

    @POST
    @Produces("application/json")
    public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response) {

        HttpSession session = request.getSession();


        User user = null;

        try {
            if (session != null && session.getAttribute("loggeduser") != null) {
                user = (User) session.getAttribute("loggeduser");
            } else {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String idToken = request.getParameter("idtoken");


                if ((email == null || email.isEmpty()) && (password == null || password.isEmpty()) && (idToken == null || idToken.isEmpty())) {

                    Cookie[] cookies = request.getCookies();

                    if (cookies != null) {
                        // process auto login for remember me feature
                        String selector = "";
                        String rawValidator = "";

                        for (Cookie aCookie : cookies) {
                            if (aCookie.getName().equals("selector")) {
                                selector = aCookie.getValue();
                            } else if (aCookie.getName().equals("validator")) {
                                rawValidator = aCookie.getValue();
                            }
                        }

                        if (!"".equals(selector) && !"".equals(rawValidator)) {
                            UserAuthToken token = userAuthServiceBean.findAuthTokenBySelector(selector);

                            if (token != null) {
                                String hashedValidatorDatabase = token.getValidator();
                                String hashedValidatorCookie = HashGenerator.generateSHA256(rawValidator);

                                if (hashedValidatorCookie.equals(hashedValidatorDatabase)) {
                                    user = token.getUser();

                                    // update new token in database
                                    String newSelector = RandomStringUtils.randomAlphanumeric(12);
                                    String newRawValidator = RandomStringUtils.randomAlphanumeric(64);

                                    String newHashedValidator = HashGenerator.generateSHA256(newRawValidator);

                                    token.setSelector(newSelector);
                                    token.setValidator(newHashedValidator);
                                    token.setLastUse(new Date());
                                    userAuthServiceBean.update(token);

                                    // update cookie
                                    Cookie cookieSelector = new Cookie("selector", newSelector);
                                    cookieSelector.setMaxAge(COOKIE_TIMEOUT);

                                    Cookie cookieValidator = new Cookie("validator", newRawValidator);
                                    cookieValidator.setMaxAge(COOKIE_TIMEOUT);

                                    response.addCookie(cookieSelector);
                                    response.addCookie(cookieValidator);
                                }

                            }
                        }
                    }
                } else {
                    //validate cred
                    if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                        //google login
                        GoogleUserInfo googleUserInfo = GoogleTokenVerifier.verifyToken(idToken);
                        if (googleUserInfo != null) {
                            //connect to db
                            GoogleUser googleUser=userAuthServiceBean.findGoogleUserByGoogleId(googleUserInfo.getId());

                            String fullName=googleUserInfo.getName().trim();

                            Pattern p = Pattern.compile("\\s\\S+$");
                            Matcher m = p.matcher(fullName);
                            m.find();
                            int surnameBegin = m.start();
                            String name=fullName.substring(0, surnameBegin).trim();
                            String surname=fullName.substring(surnameBegin).trim();

                            if (googleUser==null) {
                                //create google user

                                //TODO:redirect to signup

                                //TODO: add image from google
                                //googleUserInfo.getName().split();

                                user=userServiceBean.createGoogleUser(googleUserInfo.getId(), googleUserInfo.getEmail(), name, surname, new Date(), Gender.M);
                            } else {
                                googleUser.setName(name);
                                googleUser.setSurname(surname);
                                googleUser.setEmail(googleUserInfo.getEmail());

                                user=userServiceBean.updateGoogleUser(googleUser);
                            }
                        }
                    } else {
                        user = userServiceBean.getUser(email, password);
                    }

                    if (user != null) {
                        Cookie[] cookies = request.getCookies();

                        if (cookies == null) {
                            //create new entity
                            response = generateNewTokenAndAppendToResponse(response, userAuthServiceBean, user);
                        } else {

                            String selector = "";
                            String rawValidator = "";

                            for (Cookie aCookie : cookies) {
                                if (aCookie.getName().equals("selector")) {
                                    selector = aCookie.getValue();
                                } else if (aCookie.getName().equals("validator")) {
                                    rawValidator = aCookie.getValue();
                                }
                            }

                            if (!"".equals(selector) && !"".equals(rawValidator)) {
                                UserAuthToken token = userAuthServiceBean.findAuthTokenBySelector(selector);

                                if (token == null) {
                                    //create new entity
                                    response = generateNewTokenAndAppendToResponse(response, userAuthServiceBean, user);
                                } else {
                                    String hashedValidatorDatabase = token.getValidator();
                                    String hashedValidatorCookie = HashGenerator.generateSHA256(rawValidator);

                                    if (hashedValidatorCookie.equals(hashedValidatorDatabase)) {
                                        // update new token in database
                                        String newSelector = RandomStringUtils.randomAlphanumeric(12);
                                        String newRawValidator = RandomStringUtils.randomAlphanumeric(64);

                                        String newHashedValidator = HashGenerator.generateSHA256(newRawValidator);

                                        token.setSelector(newSelector);
                                        token.setValidator(newHashedValidator);
                                        token.setLastUse(new Date());
                                        userAuthServiceBean.update(token);

                                        // update cookie
                                        Cookie cookieSelector = new Cookie("selector", newSelector);
                                        cookieSelector.setMaxAge(604800);

                                        Cookie cookieValidator = new Cookie("validator", newRawValidator);
                                        cookieValidator.setMaxAge(604800);

                                        response.addCookie(cookieSelector);
                                        response.addCookie(cookieValidator);
                                    } else {
                                        //create new entity
                                        response = generateNewTokenAndAppendToResponse(response, userAuthServiceBean, user);
                                    }

                                }
                            } else {
                                //create new entity
                                response = generateNewTokenAndAppendToResponse(response, userAuthServiceBean, user);

                            }
                        }


                    }
                }
            }
            if (user != null) {
                session = request.getSession();
                session.setAttribute("loggeduser", user);

                UserAdapter userAdapter=new UserAdapter();
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .registerTypeAdapter(User.class, userAdapter)
                        .registerTypeAdapter(CredentialsUser.class, userAdapter)
                        .registerTypeAdapter(GoogleUser.class, userAdapter)
                        .create();
                String json = gson.toJson(user);

                return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
            } else
                return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity("UNAUTHORIZED").build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity("UNAUTHORIZED").build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity("INTERNAL_SERVER_ERROR").build();
        }
    }
}