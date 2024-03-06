package com.polimi.mrf.appart.resources;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
