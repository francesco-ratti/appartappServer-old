package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserImage;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/")
public class GetImages {
    public Response buildResponse(String idStr, String folderPath) {
        if (idStr == null || idStr.length() == 0)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();

        try {
            //for security reasons cast to long: ids can only be numeric, avoid path navigation security issue
            long id=Long.parseLong(idStr.trim());

            File file = new File(folderPath + String.valueOf(id) + ".jpg");

            if (!file.exists())
                return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).entity("file doesn't exist").build();
            if (!file.canRead())
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity("file exist but can't be read. Internal server error").build();

            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition",
                    "attachment; filename=" + id + ".jpg");
            return response.build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).entity("id not numeric").build();
        }
    }

    @GET
    @Path("/images/apartments/{id}")
    @Produces("image/jpg")
    public Response getApartmentImage(@PathParam("id") String idStr) {
        return buildResponse(idStr, ApartmentServiceBean.apartmentImagesFolderPath);
    }

    @GET
    @Path("/images/users/{id}")
    @Produces("image/jpg")
    public Response getUserImage(@PathParam("id") String idStr, @Context HttpServletRequest request) {
        /*User user= (User) request.getAttribute("user");
        try {
            //avoiding useless lookup , since equals method of Image class has been overridden, returns true if ids are the same
            UserImage userImage = new UserImage();
            userImage.setId(Long.parseLong(idStr));
            //if (user.getImages().contains(userImage)) //security check TODO: allow matches too! not only logged user to see his/her pictures
            return buildResponse(idStr, UserServiceBean.userImagesFolderPath);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).entity("id not numeric").build();
        }
        //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity("INTERNAL_SERVER_ERROR").build(); */

        return buildResponse(idStr, UserServiceBean.userImagesFolderPath);
    }
}
