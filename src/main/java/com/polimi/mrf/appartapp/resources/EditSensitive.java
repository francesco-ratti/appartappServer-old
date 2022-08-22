package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.UserAdapter;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.CredentialsUser;
import com.polimi.mrf.appartapp.entities.GoogleUser;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reserved/editsensitive")
public class EditSensitive {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @POST
    @Produces("application/json")
    public Response EditSensitive(@Context HttpServletRequest request) {
        String email = request.getParameter("newemail");
        String password = request.getParameter("password");
        String newpassword = request.getParameter("newpassword");

        User user = (User) request.getAttribute("user");

        if (user instanceof CredentialsUser) {
            CredentialsUser cu = (CredentialsUser) user;
            if (password != null && password.trim().length() > 0 && cu.getPassword().equals(password)) {
                if (email != null && email.trim().length() > 0)
                    cu.setEmail(email.trim());

                if (newpassword != null && newpassword.trim().length() > 0) {
                    cu.setPassword(newpassword.trim());
                }
                userServiceBean.updateUser(user);

                //HttpSession session=request.getSession();
                //if (session!=null)
                //    session.setAttribute("loggeduser", user);

                UserAdapter userAdapter = new UserAdapter();
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .registerTypeAdapter(User.class, userAdapter)
                        .registerTypeAdapter(CredentialsUser.class, userAdapter)
                        .registerTypeAdapter(GoogleUser.class, userAdapter)
                        .create();
                String json = gson.toJson(user);

                return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
            } else return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity("UNAUTHORIZED").build();
        } else return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("BAD_REQUEST").build();
    }
}
