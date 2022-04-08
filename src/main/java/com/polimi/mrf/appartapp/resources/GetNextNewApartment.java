package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.beans.ApartmentSearchServiceBean;
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
public class GetNextNewApartment {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentSearchServiceBean")
    ApartmentSearchServiceBean apartmentSearchServiceBean;

    @POST
    @Produces("application/json")
    public Response GetNextApartmentResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");

        HttpSession session = request.getSession(true);
        if ((session.isNew() || session.getAttribute("apartmentsservicebean")==null)) {
            apartmentSearchServiceBean.SearchNewApartments(user);
        } else {
            apartmentSearchServiceBean = (ApartmentSearchServiceBean) session.getAttribute("apartmentsservicebean");
        }

        Apartment nextapartment= apartmentSearchServiceBean.getNewApartmentNextResult();
        session.setAttribute("apartmentsservicebean", apartmentSearchServiceBean);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String json=gson.toJson(nextapartment);

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
}