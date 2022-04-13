package com.polimi.mrf.appartapp.resources;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ejb.EJB;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Path("/reserved/createapartment")
public class CreateApartment {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("application/json")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response CreateApartment(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                    //@DefaultValue("") @FormDataParam("tags") String tags,
                                    @FormDataParam("images") List<FormDataBodyPart> bodyParts,
                                    @FormDataParam("images") FormDataContentDisposition fileDispositions
    ) {
        String listingTitle=request.getParameter("listingtitle");
        String description=request.getParameter("description");
        String address=request.getParameter("address");
        String additionalExpenseDetail=request.getParameter("additionalexpensedetail");
        String priceStr=request.getParameter("price");

        if (listingTitle==null || description==null || address==null || additionalExpenseDetail==null || priceStr==null || listingTitle.length()==0 || description.length()==0 || address.length()==0 || additionalExpenseDetail.length()==0 || priceStr.length()==0)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        try {
            int price = Integer.parseInt(priceStr);
            User user=(User) request.getAttribute("user");

            List<InputStream> images= new ArrayList<>();

            int iterations= Math.min(bodyParts.size(), 15);
            for (int i = 0; i < iterations; i++) {
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                //String fileName = bodyParts.get(i).getContentDisposition().getFileName();
                images.add(bodyPartEntity.getInputStream());
            }

            Apartment apartment=apartmentServiceBean.createApartment(user, listingTitle, description, price, address, additionalExpenseDetail, images);
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String json=gson.toJson(apartment);
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity("internal server error").build();
        }
    }
}