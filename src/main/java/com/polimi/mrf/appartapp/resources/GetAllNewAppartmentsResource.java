package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.polimi.mrf.appartapp.beans.AppartmentsServiceBean;
import com.polimi.mrf.appartapp.entities.Appartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/reserved/getallnewappartments")
public class GetAllNewAppartmentsResource {
    @EJB(name = "com.polimi.mrf.appartapp.beans/AppartmentsServiceBean")
    AppartmentsServiceBean appartmentsServiceBean;

    @POST
    @Produces("application/json")
    public Response GetAllAppartmentsResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");

        HttpSession session = request.getSession(true);
        if ((session.isNew() || session.getAttribute("appartmentsservicebean")==null)) {
            appartmentsServiceBean.SearchNewAppartments(user);
        }

        List<Appartment> appartmentList= appartmentsServiceBean.getNewAppartmentList();
        session.setAttribute("appartmentsservicebean", appartmentsServiceBean);
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(appartmentList)).build();
    }
}