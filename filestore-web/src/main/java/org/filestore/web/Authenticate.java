package org.filestore.web;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yhugo on 13/12/2016.
 */

@Path("/authentification")
@RequestScoped
@Produces({ MediaType.APPLICATION_JSON })
public class Authenticate {

    private static final Logger LOGGER = Logger.getLogger(FileItemsResource.class.getName());
    @Context ServletContext context ;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postAuthentification(MultipartFormDataInput input) throws IOException {
        // log de la requête
        LOGGER.log(Level.INFO, "POST (multipart/form-data) /authentification");

        // récupération du formulaire
        Map<String, List<InputPart>> form = input.getFormDataMap();

        // Vérification formulaire
        String provider = null;
        if ( !form.containsKey("provider") ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parameter 'Provider' is mandatory.").build();
        } else {
            provider = form.get("provider").get(0).getBodyAsString();
        }
        String user = null;
        if ( !form.containsKey("user") ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parameter 'User' is mandatory.").build();
        } else {
            user = form.get("user").get(0).getBodyAsString();
        }
        String mdp = null;
        if ( !form.containsKey("mdp") ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parameter 'Mdp' is mandatory.").build();
        } else {
            mdp = form.get("mdp").get(0).getBodyAsString();
        }

        //if true renvoyer la page
        //TODO appel à une nouvelle interface API connexion
        if(true){
            UriBuilder builder = UriBuilder.fromPath("");
            builder.path("postAction");
            return Response.seeOther(builder.build()).build();
        }else{
            return Response.status(Response.Status.FORBIDDEN).entity("Forbidden access, provider cannot confirm your existence").build();
        }
    }
}
