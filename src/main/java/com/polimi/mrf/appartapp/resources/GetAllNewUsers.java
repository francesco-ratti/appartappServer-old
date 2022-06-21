package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.UserAdapter;
import com.polimi.mrf.appartapp.beans.ApartmentSearchServiceBean;
import com.polimi.mrf.appartapp.beans.UserSearchServiceBean;
import com.polimi.mrf.appartapp.entities.*;

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

@Path("/reserved/getallnewusers")
public class GetAllNewUsers {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserSearchServiceBean")
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