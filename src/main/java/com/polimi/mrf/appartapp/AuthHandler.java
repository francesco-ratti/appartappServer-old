/*package com.polimi.mrf.appartapp;

import com.polimi.mrf.appartapp.beans.UserAuthServiceBean;
import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserAuthToken;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class AuthHandler {



    public static void appendNewTokenToSession(HttpServletResponse response, UserAuthServiceBean userAuthServiceBean, User user) throws UnsupportedEncodingException {
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
        cookieSelector.setMaxAge(15768000); //6 months

        Cookie cookieValidator = new Cookie("validator", rawValidator);
        cookieValidator.setMaxAge(15768000);

        response.addCookie(cookieSelector);
        response.addCookie(cookieValidator);
    }

    public static void tokenHandler(HttpServletRequest request, HttpServletResponse response, UserAuthServiceBean userAuthServiceBean, User user) throws UnsupportedEncodingException {
        HttpSession session=request.getSession();

        boolean loggedIn = session != null && session.getAttribute("loggeduser") != null;

        Cookie[] cookies = request.getCookies();

        if (loggedIn)
            return;

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

                if (token == null) {
                    String hashedValidatorDatabase = token.getValidator();
                    String hashedValidatorCookie = HashGenerator.generateSHA256(rawValidator);

                    if (hashedValidatorCookie.equals(hashedValidatorDatabase)) {
                        session = request.getSession();
                        session.setAttribute("loggeduser", token.getUser());
                        loggedIn = true;

                        // update new token in database
                        String newSelector = RandomStringUtils.randomAlphanumeric(12);
                        String newRawValidator =  RandomStringUtils.randomAlphanumeric(64);

                        String newHashedValidator = HashGenerator.generateSHA256(newRawValidator);

                        token.setSelector(newSelector);
                        token.setValidator(newHashedValidator);
                        userAuthServiceBean.update(token);

                        // update cookie
                        Cookie cookieSelector = new Cookie("selector", newSelector);
                        cookieSelector.setMaxAge(604800);

                        Cookie cookieValidator = new Cookie("validator", newRawValidator);
                        cookieValidator.setMaxAge(604800);

                        response.addCookie(cookieSelector);
                        response.addCookie(cookieValidator);
                    }
                }
            } else {
                appendNewTokenToSession(response, userAuthServiceBean, user);
            }

        }
    }
}
*/