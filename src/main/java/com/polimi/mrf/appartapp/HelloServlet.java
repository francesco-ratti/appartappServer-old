package com.polimi.mrf.appartapp;

import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.User;

import java.io.*;
import javax.ejb.EJB;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    @EJB(name = "com.example.demo2.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}