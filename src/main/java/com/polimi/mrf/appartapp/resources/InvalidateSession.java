package com.polimi.mrf.appartapp.resources;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/invalidatesession")
public class InvalidateSession {
    @GET
    @Produces("text/plain")
    public Response Invalidate(@Context HttpServletRequest request) {
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null){
            existingSession.invalidate();
        }
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("invalidated").build();
    }
}
