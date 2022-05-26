package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/reserved/deleteapartmentimage")
public class DeleteApartmentImage {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("text/plain")
    public Response DeleteApartmentImage(@Context HttpServletRequest request) {
        User user= (User) request.getAttribute("user");
        String imageIdStr= (String) request.getParameter("imageid");
        String apartmentIdStr=(String) request.getParameter("apartmentid");

        try {
            long imageId=Long.parseLong(imageIdStr);
            long apartmentId=Long.parseLong(apartmentIdStr);

            Apartment apartment=apartmentServiceBean.getApartment(apartmentId);

            if (Long.compare(apartment.getOwner().getId(), user.getId())==0) {
//authorized
                if (apartmentServiceBean.deleteImage(apartment, imageId)) {
                    return Response.status(Response.Status.OK).entity("removed").build();
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED).entity("image id does not belong to apartment with provided apartmentid").build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("provided apartment id does not belong to current user").build();
            }
            } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("id not numeric").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error removing file from disk").build();
        }
    }
}
