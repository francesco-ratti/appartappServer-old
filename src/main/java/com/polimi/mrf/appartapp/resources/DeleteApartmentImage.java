package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.ApartmentServiceBean;
import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.Apartment;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@Path("/reserved/deleteapartmentimage")
public class DeleteApartmentImage {
    @EJB(name = "com.polimi.mrf.appart.beans/ApartmentServiceBean")
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
