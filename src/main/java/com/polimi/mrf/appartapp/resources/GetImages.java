package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/images")
public class GetImages {
    @GET
    @Path("apartments/{id}")
    @Produces("image/jpg")
    public Response getApartmentImage(@PathParam("id") String idStr) {
        return buildResponse(idStr, ApartmentServiceBean.apartmentImagesFolderPath);
    }
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
}
