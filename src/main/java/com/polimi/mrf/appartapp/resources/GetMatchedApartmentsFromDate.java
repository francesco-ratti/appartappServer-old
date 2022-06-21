package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.polimi.mrf.appartapp.MatchAdapter;
import com.polimi.mrf.appartapp.UserAdapter;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/reserved/getmatchedapartmentsfromdate")
public class GetMatchedApartmentsFromDate {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @POST
    @Produces("application/json")
    public Response GetMatchedApartmentsResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");
        String dateStr=request.getParameter("date");

        if (dateStr==null || dateStr.length()==0)
            return Response.status(Response.Status.BAD_REQUEST).build();

        try {
            Date now=new Date();
            List<Apartment> matchedApartments = userServiceBean.getMatchedApartmentsFromDate(user, new Date(Long.parseLong(dateStr)));

            /*
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(User.class, new UserAdapter())
                    .create();
            String json = gson.toJson(matchedApartments);
             */

            UserAdapter userAdapter=new UserAdapter();
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(User.class, userAdapter)
                    .registerTypeAdapter(CredentialsUser.class, userAdapter)
                    .registerTypeAdapter(GoogleUser.class, userAdapter)
                    .registerTypeAdapter(Match.class, new MatchAdapter())
                    .create();

            JsonElement jsonElement=gson.toJsonTree(matchedApartments);
            /*
            jsonElement.getAsJsonObject().addProperty("checkDate", now.getTime());
            String json=gson.toJson(jsonElement);*/

            String json=gson.toJson(matchedApartments);

            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}