package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.ApartmentServiceBean;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reserved/likeapartment")
public class LikeApartment {
    @EJB
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("text/plain")
    public Response LikeApartment(@Context HttpServletRequest request) {
        String apartmentIdStr=request.getParameter("apartmentid");
        if (apartmentIdStr == null)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        try {
            long apartmentId=Long.parseLong(apartmentIdStr);
            apartmentServiceBean.likeApartment((User) request.getAttribute("user"), apartmentId);
            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("liked").build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        }
    }
}