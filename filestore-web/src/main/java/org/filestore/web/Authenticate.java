package org.filestore.web;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.filestore.api.oauth2.Configuration;
import org.filestore.api.oauth2.Generic;
import org.filestore.api.oauth2.Github;
import org.xml.sax.SAXException;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yhugo on 13/12/2016.
 */
@Path("authentification")
@RequestScoped
@Produces({ MediaType.APPLICATION_JSON })
public class Authenticate {

    private static final Logger LOGGER = Logger.getLogger(FileItemsResource.class.getName());
    @Context ServletContext context ;


    @GET
    @Path("/login/{provider}")
    public Response redirectLogin(@PathParam("provider") String provider) {
        try {
            Github github = new Github(new Configuration());
            OAuthClientRequest request = github.createCodeRequest();

            return Response.seeOther(URI.create(request.getLocationUri())).build();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.serverError().build();

    }

    @GET
    @Path("/redirect")
    public Response getAuthentification(@QueryParam("code") String code, @Context HttpServletRequest httpRequest) {
        try {
            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            Generic github = new Github(new Configuration());
            OAuthClientRequest request = github.createTokenRequest(code);

            GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);

            String accessToken = oAuthResponse.getAccessToken();
            //long expiresIn = oAuthResponse.getExpiresIn();

            HttpSession session = httpRequest.getSession();

            session.setAttribute("token-value", accessToken);
            //session.setAttribute("token-expire", expiresIn);

            LOGGER.log(Level.INFO, accessToken);

            return Response.seeOther(URI.create("./postfiles")).build();
        } catch (OAuthSystemException | OAuthProblemException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.serverError().build();

    }
}
