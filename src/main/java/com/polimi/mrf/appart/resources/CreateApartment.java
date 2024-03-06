package com.polimi.mrf.appart.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimi.mrf.appart.beans.ApartmentServiceBean;
import com.polimi.mrf.appart.beans.UserServiceBean;
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "CreateApartment", value = "/api/reserved/createapartment")
@MultipartConfig
public class CreateApartment extends HttpServlet {
    @EJB(name = "com.polimi.mrf.appart.beans/ApartmentServiceBean")
    ApartmentServiceBean apartmentServiceBean;

    /*
    @EJB(name = "com.polimi.mrf.appart.beans/UserServiceBean")
    UserServiceBean userServiceBean;
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String listingTitle=request.getParameter("listingtitle");
        String description=request.getParameter("description");
        String address=request.getParameter("address");
        String additionalExpenseDetail=request.getParameter("additionalexpensedetail");
        String priceStr=request.getParameter("price");

        /*
        String email=request.getParameter("email");
        String password=request.getParameter("password");

        if (email==null || password==null || email.length()==0 || password.length()==0) {
            response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            return;
        }

        User user=userServiceBean.getUser(email, password);
        if (user==null) {
            response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            return;
        }
         */

        if (listingTitle==null || description==null || address==null || additionalExpenseDetail==null || priceStr==null || listingTitle.length()==0 || description.length()==0 || address.length()==0 || additionalExpenseDetail.length()==0 || priceStr.length()==0) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), "missing parameters");
            return;
        }
        try {
            int price = Integer.parseInt(priceStr);

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

            Apartment apartment=apartmentServiceBean.createApartment(user, listingTitle, description, price, address, additionalExpenseDetail, images);
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String json=gson.toJson(apartment);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (NumberFormatException e) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), "missing parameters");
        } catch (IOException e) {
            response.sendError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "INTERNAL_SERVER_ERROR");
        }

    }
}
