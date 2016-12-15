package org.filestore.web;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.filestore.api.exception.UnimplementedProviderException;
import org.filestore.api.oauth2.*;
import org.xml.sax.SAXException;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public Response redirectLogin(@PathParam("provider") String provider, @Context HttpServletRequest httpRequest) throws UnimplementedProviderException {
        try {
            HttpSession session = httpRequest.getSession();
            session.setAttribute("provider", provider);

            Generic providerObject = getGenericObject(provider);
            OAuthClientRequest request = providerObject.createCodeRequest();

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
    public Response getAuthentification(@QueryParam("code") String code, @Context HttpServletRequest httpRequest) throws UnimplementedProviderException {
        try {
            HttpSession session = httpRequest.getSession();
            String provider = (String) session.getAttribute("provider");

            Generic providerObject = getGenericObject(provider);

            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthClientRequest request = providerObject.createTokenRequest(code);

            GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);

            String accessToken = oAuthResponse.getAccessToken();
            //long expiresIn = oAuthResponse.getExpiresIn();

            session.setAttribute("token-value", accessToken);
            //session.setAttribute("token-expire", expiresIn);

            LOGGER.log(Level.INFO, accessToken);

            LOGGER.log(Level.INFO, providerObject.getUserEmail(accessToken));
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

    private Generic getGenericObject(String provider) throws ParserConfigurationException, SAXException, IOException, UnimplementedProviderException {
        Generic providerObject = null;
        switch(OAuthProviderType.valueOf(provider.toUpperCase())) {
            case GITHUB:
                providerObject = new Github(new Configuration());
                break;
            case FACEBOOK:
                providerObject = new Facebook(new Configuration());
                break;
            case GOOGLE:
                providerObject = new Google(new Configuration());
                break;
            default: // should never go here
                throw new UnimplementedProviderException("The provider is unimplemented, gg");
        }
        return providerObject;
    }

    @Path("/postfiles")
    public void redirectTo(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        context.getRequestDispatcher("/WEB-INF/postfile.jsp").forward( request, response );
    }
}
