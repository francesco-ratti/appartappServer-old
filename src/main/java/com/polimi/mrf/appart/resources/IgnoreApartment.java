package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.ApartmentServiceBean;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reserved/ignoreapartment")
public class IgnoreApartment {
    @EJB
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("text/plain")
    public Response IgnoreApartment(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String apartmentIdStr=request.getParameter("apartmentid");
            if (apartmentIdStr == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        try {
            long apartmentId=Long.parseLong(apartmentIdStr);
            apartmentServiceBean.ignoreApartment((User) request.getAttribute("user"), apartmentId);
            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("ignored").build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        }
    }
}