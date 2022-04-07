package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.polimi.mrf.appartapp.beans.ApartmentsServiceBean;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reserved/getnextnewapartment")
public class GetNextNewApartmentResource {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentsServiceBean")
    ApartmentsServiceBean apartmentsServiceBean;

    @POST
    @Produces("application/json")
    public Response GetNextApartmentResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");

        HttpSession session = request.getSession(true);
        if ((session.isNew() || session.getAttribute("apartmentsservicebean")==null)) {
            apartmentsServiceBean.SearchNewApartments(user);
        } else {
            apartmentsServiceBean= (ApartmentsServiceBean) session.getAttribute("apartmentsservicebean");
        }

        Apartment nextapartment= apartmentsServiceBean.getNewApartmentNextResult();
        session.setAttribute("apartmentsservicebean", apartmentsServiceBean);
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(nextapartment)).build();
    }
}