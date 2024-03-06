package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appart.UserAdapter;
import com.polimi.mrf.appart.beans.ApartmentSearchServiceBean;
import com.polimi.mrf.appart.beans.UserSearchServiceBean;
import com.polimi.mrf.appart.entities.*;


import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/reserved/getallnewusers")
public class GetAllNewUsers {
    @EJB(name = "com.polimi.mrf.appart.beans/UserSearchServiceBean")
    UserSearchServiceBean userSearchServiceBean;

    @POST
    @Produces("application/json")
    public Response GetAllUsersResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user= (User) request.getAttribute("user");

        HttpSession session = request.getSession(true);
        if ((session.isNew() || session.getAttribute("usersearchservicebean")==null)) {
            userSearchServiceBean.SearchNewUsers(user);
        } else {
            userSearchServiceBean=(UserSearchServiceBean) session.getAttribute("usersearchservicebean");
        }

        List<UserApartmentContainer> userList= userSearchServiceBean.getNewUserList();
        session.setAttribute("usersearchservicebean", userSearchServiceBean);

        UserAdapter userAdapter=new UserAdapter();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(User.class, userAdapter)
                .registerTypeAdapter(CredentialsUser.class, userAdapter)
                .registerTypeAdapter(GoogleUser.class, userAdapter)
                .create();
        String json=gson.toJson(userList);

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
}