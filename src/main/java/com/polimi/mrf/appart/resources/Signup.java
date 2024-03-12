package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appart.beans.UserAuthServiceBean;
import com.polimi.mrf.appart.enums.Gender;
import com.polimi.mrf.appart.UserAdapter;
import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.CredentialsUser;
import com.polimi.mrf.appart.entities.GoogleUser;
import com.polimi.mrf.appart.entities.User;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.io.IOException;
import java.util.Date;

import static com.polimi.mrf.appart.resources.Login.generateNewTokenAndAppendToResponse;

@WebServlet(name = "Signup", value = "/api/signup")
public class Signup extends HttpServlet {
    @EJB
    UserServiceBean userServiceBean;

    @EJB
    UserAuthServiceBean userAuthServiceBean;

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email=request.getParameter("email");
        String name=request.getParameter("name");
        String surname=request.getParameter("surname");
        String password=request.getParameter("password");
        String birthdayStr=request.getParameter("birthday");
        String genderStr=request.getParameter("gender");

        if (email==null || password==null || name==null || surname==null || birthdayStr==null || genderStr==null || (!(genderStr.equals("M") || genderStr.equals("F") || genderStr.equals("NB")))) {
            response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            response.setContentType(MediaType.TEXT_PLAIN);
            return;
        }

        Date birthday=new Date(Long.parseLong(birthdayStr));
        Gender gender=Gender.valueOf(genderStr);

        if (userServiceBean.UserExists(email)) {
            response.setStatus(Response.Status.NOT_ACCEPTABLE.getStatusCode());
            response.setContentType(MediaType.TEXT_PLAIN);
        }
        else {
            User user=userServiceBean.createCredentialsUser(email, password,name,surname, birthday, gender);
            UserAdapter userAdapter=new UserAdapter();
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(User.class, userAdapter)
                    .registerTypeAdapter(CredentialsUser.class, userAdapter)
                    .registerTypeAdapter(GoogleUser.class, userAdapter)
                    .create();
            String json=gson.toJson(user);

            response = generateNewTokenAndAppendToResponse(response, userAuthServiceBean, user);

            response.setStatus(Response.Status.OK.getStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON);
            response.getWriter().println(json);
        }
    }
}