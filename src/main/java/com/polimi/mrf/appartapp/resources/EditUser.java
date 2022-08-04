package com.polimi.mrf.appartapp.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appartapp.enums.Gender;
import com.polimi.mrf.appartapp.enums.Month;
import com.polimi.mrf.appartapp.enums.TemporalQ;
import com.polimi.mrf.appartapp.UserAdapter;
import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.CredentialsUser;
import com.polimi.mrf.appartapp.entities.GoogleUser;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("/reserved/edituser")
public class EditUser {
    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @POST
    @Produces("application/json")
    public Response edit(@Context HttpServletRequest request) {
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String birthdayStr = request.getParameter("birthday");
        String genderStr = request.getParameter("gender");

        //tenants attributes
        String bioStr=request.getParameter("bio");
        String reasonStr=request.getParameter("reason");
        String monthStr=request.getParameter("month");
        String jobStr=request.getParameter("job");
        String incomeStr=request.getParameter("income");
        String smokerStr=request.getParameter("smoker");
        String petsStr=request.getParameter("pets");

        User user= (User) request.getAttribute("user");

        if (name!=null && name.trim().length()>0)
            user.setName(name.trim());

        if (surname!=null && surname.trim().length()>0)
            user.setSurname(surname.trim());

        if (birthdayStr!=null && birthdayStr.trim().length()>0)
            user.setBirthday(new Date(Long.parseLong(birthdayStr)));

        if (genderStr != null && (genderStr.equals("M") || genderStr.equals("F") || genderStr.equals("NB")))
            user.setGender(Gender.valueOf(genderStr));

        //tenants:
        if (bioStr!=null && bioStr.trim().length()>0)
            user.setBio(bioStr.trim());

        if (reasonStr!=null && reasonStr.trim().length()>0)
            user.setReason(reasonStr.trim());

        if (monthStr != null && monthStr.length()>0)
            user.setMonth(Month.valueOf(monthStr.trim()));

        if (jobStr!=null && jobStr.trim().length()>0)
            user.setJob(jobStr.trim());

        if (incomeStr!=null && incomeStr.trim().length()>0)
            user.setIncome(incomeStr.trim());

        if (smokerStr!=null && smokerStr.trim().length()>0)
            user.setSmoker(TemporalQ.valueOf(smokerStr.trim()));

        if (petsStr!=null && petsStr.trim().length()>0)
            user.setPets(petsStr.trim());

        userServiceBean.updateUser(user);

        HttpSession session=request.getSession();
        if (session!=null)
            session.setAttribute("loggeduser", user);

        UserAdapter userAdapter=new UserAdapter();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(User.class, userAdapter)
                .registerTypeAdapter(CredentialsUser.class, userAdapter)
                .registerTypeAdapter(GoogleUser.class, userAdapter)
                .create();
        String json=gson.toJson(user);

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
}
