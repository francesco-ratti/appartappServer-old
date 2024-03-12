package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.ImgServiceBean;
import com.polimi.mrf.appart.entities.Image;

import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

@Path("/")
public class GetImages {

    @EJB
    ImgServiceBean imgServiceBean;

//    public Response buildResponse(String idStr, String folderPath) {
//        if (idStr == null || idStr.isEmpty())
//            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
//
//        try {
//            //for security reasons cast to long: ids can only be numeric, avoid path navigation security issue
//            long id=Long.parseLong(idStr.trim());
//            Image img=imgServiceBean.getApartmentImageById(id);
//
//            Response.ResponseBuilder response = Response.ok((StreamingOutput) output -> {
//                output.write(img.getImageBytes());
//                output.flush();
//            });
//
//            response.header("Content-Disposition",
//                    "attachment; filename=" + id + ".jpg");
//            return response.build();
//        } catch (NumberFormatException e) {
//            return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).entity("id not numeric").build();
//        }
//    }

//    @Path("/images/apartments/{apartmentId}/{imgId}")

    @GET
    @Path("/images/apartments/{imgId}")
    @Produces("image/jpg")
    public Response getApartmentImage(@PathParam("imgId") String idStr) {

        if (idStr == null || idStr.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();

        try {
            //for security reasons cast to long: ids can only be numeric, avoid path navigation security issue
            long id=Long.parseLong(idStr.trim());
            byte[] img=imgServiceBean.getApartmentImageBytesById(id);

            if (img==null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("id not exists").build();

            Response.ResponseBuilder response = Response.ok((StreamingOutput) output -> {
                output.write(img);
                output.flush();
            });

            response.header("Content-Disposition",
                    "attachment; filename=" + id + ".jpg");
            return response.build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).entity("id not numeric").build();
        }
    }

    @GET
    @Path("/images/users/{imgId}")
    @Produces("image/jpg")
    public Response getUserImage(@PathParam("imgId") String idStr, @Context HttpServletRequest request) {

        if (idStr == null || idStr.length() == 0)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();

        try {
            //for security reasons cast to long: ids can only be numeric, avoid path navigation security issue
            long id=Long.parseLong(idStr.trim());

            byte[] img=imgServiceBean.getUserImageBytesById(id);
            if (img==null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("id not exists").build();

            Response.ResponseBuilder response = Response.ok(new StreamingOutput(){
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    output.write(img);
                    output.flush();
                }
            });

            response.header("Content-Disposition",
                    "attachment; filename=" + id + ".jpg");
            return response.build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).entity("id not numeric").build();
        }


    }
}
