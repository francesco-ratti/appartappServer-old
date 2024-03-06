package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.ApartmentServiceBean;
import com.polimi.mrf.appart.entities.Apartment;
import com.polimi.mrf.appart.entities.User;

import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reserved/ignoreuser")
public class IgnoreUser {
    @EJB(name = "com.polimi.mrf.appart.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("text/plain")
    public Response IgnoreUser(@Context HttpServletRequest request) {
        String apartmentIdStr=request.getParameter("apartmentid");
        String userIdStr=request.getParameter("userid");
        if (userIdStr == null || apartmentIdStr==null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        try {
            long apartmentId=Long.parseLong(apartmentIdStr);
            Apartment apartment=apartmentServiceBean.getApartment(apartmentId);
            
            User user= (User) request.getAttribute("user");

            if (user.getOwnedApartments().contains(apartment)) {
                long userId = Long.parseLong(userIdStr);
                apartmentServiceBean.ignoreUser(apartment, userId);
                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("ignored").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity("unauthorized").build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        }
    }
}
