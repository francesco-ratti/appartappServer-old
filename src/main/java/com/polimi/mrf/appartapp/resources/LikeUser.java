package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reserved/likeuser")
public class LikeUser {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("text/plain")
    public Response LikeUser(@Context HttpServletRequest request) {
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
                apartmentServiceBean.likeUser(apartment, userId);
                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("liked").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity("unauthorized").build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        }
    }
}
