package com.polimi.mrf.appartapp.resources;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reserved/createapartment")
public class CreateApartment {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    @POST
    @Produces("application/json")
    public Response CreateApartment(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String listingTitle=request.getParameter("listingtitle");
        String description=request.getParameter("description");
        String address=request.getParameter("address");
        String additionalExpenseDetail=request.getParameter("additionalexpensedetail");
        String priceStr=request.getParameter("price");

        if (listingTitle==null || description==null || address==null || additionalExpenseDetail==null || priceStr==null || listingTitle.length()==0 || description.length()==0 || address.length()==0 || additionalExpenseDetail.length()==0 || priceStr.length()==0)
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        try {
            int price = Integer.parseInt(priceStr);
            User user=(User) request.getAttribute("user");
            Apartment apartment=apartmentServiceBean.createApartment(user, listingTitle, description, price, address, additionalExpenseDetail);
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String json=gson.toJson(apartment);
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("missing parameters").build();
        }
    }
}