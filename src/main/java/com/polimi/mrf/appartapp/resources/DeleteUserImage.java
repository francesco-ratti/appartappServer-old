package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.Image;
import com.polimi.mrf.appart.entities.User;
import com.polimi.mrf.appart.entities.UserImage;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@Path("/reserved/deleteuserimage")
public class DeleteUserImage {
    @EJB(name = "com.polimi.mrf.appart.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @POST
    @Produces("text/plain")
    public Response DeleteImage(@Context HttpServletRequest request) {
        User user= (User) request.getAttribute("user");
        String idStr= (String) request.getParameter("id");

        try {
            long id=Long.parseLong(idStr);

            if (userServiceBean.deleteImage(user, id)) {
                return Response.status(Response.Status.OK).entity("removed").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("image with this id does not exist").build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("id not numeric").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error removing file from disk").build();
        }
    }
}