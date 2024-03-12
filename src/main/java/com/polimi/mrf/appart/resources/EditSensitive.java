package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appart.UserAdapter;
import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.CredentialsUser;
import com.polimi.mrf.appart.entities.GoogleUser;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reserved/editsensitive")
public class EditSensitive {
    @EJB
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
