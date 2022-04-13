package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.Gender;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("/signup")
public class Signup {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @POST
    @Produces("application/json")
    public Response signup(@Context HttpServletRequest request) {
        String email=request.getParameter("email");
        String name=request.getParameter("name");
        String surname=request.getParameter("surname");
        String password=request.getParameter("password");
        String birthdayStr=request.getParameter("birthday");
        String genderStr=request.getParameter("gender");

        if (email==null || password==null || name==null || surname==null || birthdayStr==null || genderStr==null || (!(genderStr.equals("M") || genderStr.equals("F") || genderStr.equals("NB"))))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Date birthday=new Date(Long.parseLong(birthdayStr));
        Gender gender=Gender.valueOf(genderStr);

        if (userServiceBean.UserExists(email)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        else {
            User user=userServiceBean.createUser(email, password,name,surname, birthday, gender);
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String json=gson.toJson(user);

            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
        }
    }
}