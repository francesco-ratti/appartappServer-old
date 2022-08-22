package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.beans.UserAuthServiceBean;
import com.polimi.mrf.appartapp.enums.Gender;
import com.polimi.mrf.appartapp.UserAdapter;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.CredentialsUser;
import com.polimi.mrf.appartapp.entities.GoogleUser;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

import static com.polimi.mrf.appartapp.resources.Login.generateNewTokenAndAppendToResponse;

@WebServlet(name = "Signup", value = "/api/signup")
public class Signup extends HttpServlet {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @EJB(name = "com.polimi.mrf.appartapp.beans/UserAuthServiceBean")
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