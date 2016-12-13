package org.filestore.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Yhugo on 13/12/2016.
 */
@WebServlet(name = "posting", urlPatterns={"/api/postAction"})
public class PosterAction extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String VUE ="/WEB-INF/postfile.jsp";
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}
