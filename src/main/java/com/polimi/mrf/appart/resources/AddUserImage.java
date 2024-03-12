package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.UserServiceBean;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "AddUserImage", value = "/api/reserved/adduserimage")
@MultipartConfig
public class AddUserImage extends HttpServlet {
    @EJB
    UserServiceBean userServiceBean;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user= (User) request.getAttribute("user");

        Collection<Part> parts=request.getParts();
        List<InputStream> images= new ArrayList<>();

        for (Part p: parts) {
            System.out.println(p.getName());
            if (p.getName().equals("images")) {
                images.add(p.getInputStream());
            }
        }

        if (images.size()==0) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), "missing images");
            return;
        }

        userServiceBean.addImage(user, images);
        response.setStatus(Response.Status.OK.getStatusCode());
    }
}
