package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.polimi.mrf.appart.MatchAdapter;
import com.polimi.mrf.appart.UserAdapter;
import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.*;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/reserved/getmatchedapartments")
public class GetMatchedApartments {
    @EJB
    UserServiceBean userServiceBean;

    @POST
    @Produces("application/json")
    public Response GetMatchedApartmentsResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");

        Date now=new Date();
        List<Match> matchedApartments=userServiceBean.getMatchedApartments(user);

        UserAdapter userAdapter=new UserAdapter();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(User.class, userAdapter)
                .registerTypeAdapter(CredentialsUser.class, userAdapter)
                .registerTypeAdapter(GoogleUser.class, userAdapter)
                .registerTypeAdapter(Match.class, new MatchAdapter())
                .create();

        /*
        JsonElement jsonElement=gson.toJsonTree(matchedApartments);
        jsonElement.addProperty("checkDate", now.getTime());
        String json=gson.toJson(jsonElement);*/
        String json=gson.toJson(matchedApartments);

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
}