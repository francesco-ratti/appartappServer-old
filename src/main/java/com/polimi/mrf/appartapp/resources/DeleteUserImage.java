package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.Image;
import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserImage;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/reserved/deleteuserimage")
public class DeleteUserImage {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
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