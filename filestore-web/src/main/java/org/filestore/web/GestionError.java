package org.filestore.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by jerome on 16/12/2016.
 */
@Path("error")
public class GestionError {

    @GET
    @Path("/mail")
    public void errorMail(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException{
        request.getRequestDispatcher("/WEB-INF/errors/mail.jsp").forward( request, response );
    }
}
