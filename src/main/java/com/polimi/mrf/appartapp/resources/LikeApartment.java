package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reserved/likeapartment")
public class LikeApartment {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentServiceBean")
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