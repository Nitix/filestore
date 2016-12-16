package org.filestore.client;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.filestore.api.FileService;
import org.filestore.api.FileServiceException;
import org.filestore.api.exception.UnimplementedProviderException;
import org.filestore.api.oauth2.Facebook;
import org.filestore.api.oauth2.GenericOAuth;
import org.filestore.api.oauth2.Github;
import org.filestore.api.oauth2.Google;

public class FileStoreClient {

    private static final Logger LOGGER = Logger.getLogger(FileStoreClient.class.getName());

    @Resource(lookup = "java:comp/InAppClientContainer")
    private static boolean isInAppclient;
    @EJB
    private static FileService service;


    @EJB
    private static Facebook facebook;

    @EJB
    private static Github github;

    @EJB
    private static Google google;


    private String host;

    public FileStoreClient(String host) {
        this.host = host;
    }

    public FileService getFileServiceRemote() throws NamingException {
        if (!Boolean.TRUE.equals(isInAppclient) && service == null) {
            LOGGER.log(Level.INFO, "getting FileSerive using remote-naming");
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            env.put(Context.PROVIDER_URL, "http-remoting://" + host + ":8080");
            InitialContext context = new InitialContext(env);
            service = (FileService) context.lookup("filestore-ear/filestore-ejb/fileservice!org.filestore.ejb.file.FileService");
            context.close();
        }
        return service;
    }

    public FileService getFileServiceEJB() throws NamingException {
        if (!Boolean.TRUE.equals(isInAppclient) && service == null) {
            LOGGER.log(Level.INFO, "getting FileSerive using ejb client");
            final Properties env = new Properties();
            env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            env.put("jboss.naming.client.ejb.context", true);
            InitialContext context = new InitialContext(env);
            service = (FileService) context.lookup("ejb:filestore-ear/filestore-ejb/fileservice!org.filestore.ejb.file.FileService");
            context.close();
        }
        return service;
    }

    public void postFile(String owner, List<String> receivers, String message,
                         String filename, Path file) throws FileServiceException,
            IOException, NamingException {
        if (Boolean.TRUE.equals(isInAppclient)) {
            LOGGER.log(Level.INFO, "We ARE in a client container");
        }
        byte[] content = Files.readAllBytes(file);
        //getFileServiceEJB().postFile(owner, receivers, message, filename, content);
        getFileServiceRemote().postFile(owner, receivers, message, filename, content);
    }

    public static void main(String args[]) throws FileServiceException,
            IOException, NamingException, ParseException {
        Options options = new Options();
        //options.addOption("s", "sender", true, "sender email adresse");
        Option r = new Option("r", "receivers", true, "receivers email adresses (coma separated)");
        r.setRequired(true);
        r.setValueSeparator(',');
        options.addOption(r);
        options.addOption("m", "message", true, "message for receivers");
        Option p = new Option("p", "path", true, "file path to send");
        p.setRequired(true);
        options.addOption(p);
        options.addOption("h", "host", true, "server hostname (default to localhost)");

        Option pro = new Option("pro", "provider", true, "OAuth2 provider (github, google, facebook)");
        pro.setRequired(true);
        options.addOption(pro);

        Option us = new Option("user", "user", true, "login");
        us.setRequired(true);
        options.addOption(us);


        Option pass = new Option("password", "password", true, "password");
        pass.setRequired(true);
        options.addOption(pass);


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        //String sender = cmd.getOptionValue("s", "root@localhost");
        String message = cmd.getOptionValue("m", "I have a file for you...");
        String host = cmd.getOptionValue("h", "localhost");
        String[] receivers = cmd.getOptionValues("r");
        Path path = Paths.get(cmd.getOptionValue("p"));
        String provider = cmd.getOptionValue("provider");
        String user = cmd.getOptionValue("user");
        String paswword = cmd.getOptionValue("password");


        GenericOAuth providerObject = null;
        try {
            providerObject = getGenericObject(provider);


            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthClientRequest request = providerObject.createTokenRequest(user, paswword);

            String accessToken = null;
            if (provider.equals("github") || provider.equals("facebook")) {
                GitHubTokenResponse gitoAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
                accessToken = gitoAuthResponse.getAccessToken();
            } else {
                OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);
                accessToken = oAuthResponse.getAccessToken();
            }
            //long expiresIn = oAuthResponse.getExpiresIn();


            String email = null;
            if (providerObject.getUserEmail(accessToken) != null) {
                email =  providerObject.getUserEmail(accessToken);
            } else {
                throw new Exception();
            }

            FileStoreClient client = new FileStoreClient(host);
            client.postFile(email, Arrays.asList(receivers), message, path.getFileName().toString(), path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GenericOAuth getGenericObject(String provider) throws IOException, UnimplementedProviderException {
        GenericOAuth providerObject = null;
        switch (OAuthProviderType.valueOf(provider.toUpperCase())) {
            case GITHUB:
                providerObject = github;
                break;
            case FACEBOOK:
                providerObject = facebook;
                break;
            case GOOGLE:
                providerObject = google;
                break;
            default: // should never go here
                throw new UnimplementedProviderException("The provider is unimplemented, gg");
        }
        return providerObject;
    }

}
