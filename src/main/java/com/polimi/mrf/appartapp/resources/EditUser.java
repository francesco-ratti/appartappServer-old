package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.Gender;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("/reserved/edituser")
public class EditUser {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @POST
    @Produces("application/json")
    public Response EditUser(@Context HttpServletRequest request) {
        String email = request.getParameter("newemail");
        String password = request.getParameter("newpassword");

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String birthdayStr = request.getParameter("birthday");
        String genderStr = request.getParameter("gender");

        User user= (User) request.getAttribute("user");
        if (email!=null && email.trim().length()>0)
            user.setEmail(email.trim());

        if (name!=null && name.trim().length()>0)
            user.setName(name.trim());

        if (surname!=null && surname.trim().length()>0)
            user.setSurname(surname.trim());

        if (password!=null && password.trim().length()>0)
            user.setPassword(password.trim());

        if (birthdayStr!=null && birthdayStr.trim().length()>0)
            user.setBirthday(new Date(Long.parseLong(birthdayStr)));

        if (genderStr.equals("M") || genderStr.equals("F") || genderStr.equals("NB"))
            user.setGender(Gender.valueOf(genderStr));

        userServiceBean.updateUser(user);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String json=gson.toJson(user);

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
}
