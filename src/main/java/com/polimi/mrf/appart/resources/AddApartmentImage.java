package com.polimi.mrf.appart.resources;

import com.polimi.mrf.appart.beans.ApartmentServiceBean;
import com.polimi.mrf.appart.entities.Apartment;
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

@WebServlet(name = "AddApartmentImage", value = "/api/reserved/addapartmentimage")
@MultipartConfig
public class AddApartmentImage extends HttpServlet {
    @EJB
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
