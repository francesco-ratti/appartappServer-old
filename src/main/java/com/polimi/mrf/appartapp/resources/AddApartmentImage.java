package com.polimi.mrf.appartapp.resources;

import com.polimi.mrf.appartapp.beans.ApartmentServiceBean;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "AddApartmentImage", value = "/api/reserved/addapartmentimage")
@MultipartConfig
public class AddApartmentImage extends HttpServlet {
    @EJB(name = "com.polimi.mrf.appartapp.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user= (User) request.getAttribute("user");
        String idStr=request.getParameter("id");

        try {
            long id=Long.parseLong(idStr);

            Collection<Part> parts = request.getParts();
            List<InputStream> images = new ArrayList<>();

            for (Part p : parts) {
                System.out.println(p.getName());
                if (p.getName().equals("images")) {
                    images.add(p.getInputStream());
                }
            }

            if (images.size() == 0) {
                response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), "missing images");
                return;
            }

            Apartment apartment=apartmentServiceBean.getApartment(id);

            if (Long.compare(apartment.getOwner().getId(), user.getId())==0) { //if logged user is the owner of apartment
                apartmentServiceBean.addImage(apartment, images);
                response.setStatus(Response.Status.OK.getStatusCode());
            } else {
                response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            }
        } catch (NumberFormatException e) {
            response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        }
    }
}
