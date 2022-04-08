package com.polimi.mrf.appartapp.filter;

import com.polimi.mrf.appartapp.beans.UserServiceBean;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.EJB;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

@WebFilter(filterName="UserManagerFilter", urlPatterns="/api/reserved/*")
public class UserManager extends HttpFilter {

    @EJB(name = "com.polimi.mrf.appartapp.beans/UserServiceBean")
    UserServiceBean userServiceBean;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException, ServletException {
        String email=req.getParameter("email");
        String password=req.getParameter("password");

        if (email==null || password==null || email.length()==0 || password.length()==0) {
            res.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
            return;
        }

        User user=userServiceBean.getUser(email, password);
        if (user==null) {
            res.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
        } else {
            req.setAttribute("user", user);
            chain.doFilter(req, res);
        }
    }
}
