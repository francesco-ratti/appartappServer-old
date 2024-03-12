package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appart.UserAdapter;
import com.polimi.mrf.appart.beans.ApartmentSearchServiceBean;
import com.polimi.mrf.appart.entities.Apartment;
import com.polimi.mrf.appart.entities.CredentialsUser;
import com.polimi.mrf.appart.entities.GoogleUser;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reserved/getnextnewapartment")
public class GetNextNewApartment {
    @EJB
    ApartmentSearchServiceBean apartmentSearchServiceBean;

    @POST
    @Produces("application/json")
    public Response GetNextApartmentResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");

        HttpSession session = request.getSession(true);
        if ((session.isNew() || session.getAttribute("apartmentsearchservicebean")==null)) {
            apartmentSearchServiceBean.SearchNewApartments(user);
        } else {
            apartmentSearchServiceBean = (ApartmentSearchServiceBean) session.getAttribute("apartmentsearchservicebean");
        }

        Apartment nextapartment= apartmentSearchServiceBean.getNewApartmentNextResult();
        if (nextapartment==null) {
            apartmentSearchServiceBean.SearchNewApartments(user);
            nextapartment= apartmentSearchServiceBean.getNewApartmentNextResult();
        }
        session.setAttribute("apartmentsearchservicebean", apartmentSearchServiceBean);

        UserAdapter userAdapter=new UserAdapter();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(User.class, userAdapter)
                .registerTypeAdapter(CredentialsUser.class, userAdapter)
                .registerTypeAdapter(GoogleUser.class, userAdapter)
                .create();
        String json=gson.toJson(nextapartment);

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
}