package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appart.enums.Gender;
import com.polimi.mrf.appart.HashGenerator;
import com.polimi.mrf.appart.imageutils.ImgFromUrlToInputStream;
import com.polimi.mrf.appart.UserAdapter;
import com.polimi.mrf.appart.beans.UserAuthServiceBean;
import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.CredentialsUser;
import com.polimi.mrf.appart.entities.GoogleUser;
import com.polimi.mrf.appart.entities.User;
import com.polimi.mrf.appart.entities.UserAuthToken;
import com.polimi.mrf.appart.google.GoogleTokenVerifier;
import com.polimi.mrf.appart.google.GoogleUserInfo;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;



import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "Login", value = "/api/login")
public class Login extends HttpServlet {

    private static final int COOKIE_TIMEOUT=15768000; //6 months

    @EJB(name = "com.polimi.mrf.appart.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @EJB(name = "com.polimi.mrf.appart.beans/UserAuthServiceBean")
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
        cookieSelector.setPath("/");

        Cookie cookieValidator = new Cookie("validator", rawValidator);
        cookieValidator.setMaxAge(COOKIE_TIMEOUT);
        cookieValidator.setPath("/");

        response.addCookie(cookieSelector);
        response.addCookie(cookieValidator);
        return response;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();


        User user = null;

        try {
            //if (session != null && session.getAttribute("loggeduser") != null) {
            //    user = (User) session.getAttribute("loggeduser");
            //}
            //else {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String idToken = request.getParameter("idtoken");
                String accessToken = request.getParameter("accesstoken");

                if ((email == null || email.isEmpty()) && (password == null || password.isEmpty()) && (idToken == null || idToken.isEmpty()) && (accessToken==null || accessToken.isEmpty())) {

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
                                    cookieSelector.setPath("/");

                                    Cookie cookieValidator = new Cookie("validator", newRawValidator);
                                    cookieValidator.setMaxAge(COOKIE_TIMEOUT);
                                    cookieValidator.setPath("/");

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
                        GoogleUserInfo googleUserInfo = GoogleTokenVerifier.verifyToken(idToken, accessToken);
                        if (googleUserInfo != null) {
                            GoogleUser googleUser=userAuthServiceBean.findGoogleUserByGoogleId(googleUserInfo.getId());

                            //parse user infos
                            String name;
                            String surname;

                            if(googleUserInfo.getFamilyName() == null || googleUserInfo.getFamilyName().isEmpty() || googleUserInfo.getGivenName()== null || googleUserInfo.getName().isEmpty()) {
                                String fullName = googleUserInfo.getName().trim();

                                Pattern p = Pattern.compile("\\s\\S+$");
                                Matcher m = p.matcher(fullName);
                                m.find();
                                int surnameBegin = m.start();
                                name = fullName.substring(0, surnameBegin).trim();
                                surname = fullName.substring(surnameBegin).trim();
                            } else {
                                name= googleUserInfo.getGivenName();
                                surname= googleUserInfo.getFamilyName();
                            }

                            com.google.api.services.people.v1.model.Date birthday=googleUserInfo.getBirthday().getDate();
                            Date myBirthday=new Date(0);

                            if (birthday.getDay()!=null)
                                myBirthday.setDate(birthday.getDay());
                            if (birthday.getMonth()!=null)
                                myBirthday.setMonth(birthday.getMonth()-1);
                            if (birthday.getYear()!=null)
                                myBirthday.setYear(birthday.getYear()-1900);

                            com.google.api.services.people.v1.model.Gender gender=googleUserInfo.getGender();
                            Gender myGender;
                            switch (gender.getValue()) {
                                case "male":
                                    myGender=Gender.M;
                                    break;
                                case "female":
                                    myGender=Gender.F;
                                case "unspecified":
                                    myGender=Gender.NB;
                                    break;
                                default:
                                    myGender=Gender.NB;
                                    break;
                            }
                            //create user
                            if (googleUser==null) {
                                user=userServiceBean.createGoogleUser(googleUserInfo.getId(), googleUserInfo.getEmail(), name, surname, myBirthday, myGender);
                                ImgFromUrlToInputStream imgFromUrlToInputStream=new ImgFromUrlToInputStream(googleUserInfo.getPictureUrl());
                                List images=new ArrayList<InputStream>(1);
                                images.add(imgFromUrlToInputStream.getInputStream());
                                userServiceBean.addImage(user, images);
                            } else {
                                //update user
                                googleUser.setName(name);
                                googleUser.setSurname(surname);
                                googleUser.setEmail(googleUserInfo.getEmail());
                                googleUser.setGender(myGender);
                                googleUser.setBirthday(myBirthday);

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
                                        cookieSelector.setMaxAge(COOKIE_TIMEOUT);
                                        cookieSelector.setPath("/");

                                        Cookie cookieValidator = new Cookie("validator", newRawValidator);
                                        cookieValidator.setMaxAge(COOKIE_TIMEOUT);
                                        cookieValidator.setPath("/");

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
//                }
            }
            if (user != null) {
                //session = request.getSession();
                //session.setAttribute("loggeduser", user);

                UserAdapter userAdapter=new UserAdapter();
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .registerTypeAdapter(User.class, userAdapter)
                        .registerTypeAdapter(CredentialsUser.class, userAdapter)
                        .registerTypeAdapter(GoogleUser.class, userAdapter)
                        .create();
                String json = gson.toJson(user);

                response.setStatus(Response.Status.OK.getStatusCode());
                response.setContentType(MediaType.APPLICATION_JSON);
                response.getWriter().println(json);
            } else {
                response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
                response.setContentType(MediaType.TEXT_PLAIN);
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            response.setContentType(MediaType.TEXT_PLAIN);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            response.setContentType(MediaType.TEXT_PLAIN);
        }

    }
}