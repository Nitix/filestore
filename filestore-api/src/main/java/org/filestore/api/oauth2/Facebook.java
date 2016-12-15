package org.filestore.api.oauth2;

import com.google.gson.Gson;
import org.apache.oltu.oauth2.common.OAuthProviderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jerome on 15/12/2016.
 */
public class Facebook extends Generic {

    private static final OAuthProviderType PROVIDER = OAuthProviderType.FACEBOOK;

    public Facebook(Configuration configuration) {
        super(configuration);
    }

    @Override
    public OAuthProviderType getProvider() {
        return PROVIDER;
    }

    @Override
    protected String getScope() {
        return "email";
    }

    @Override
    public String getUserEmail(String token) throws IOException {
        URL url = new URL("https://graph.facebook.com/me?fields=email?access_token=" + token);
        URLConnection connection = url.openConnection();
        StringBuilder outputString = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            outputString.append(line);
        }
        reader.close();
        System.out.println(outputString);
        Facebook.FacebookEmail[] ges = new Gson().fromJson(outputString.toString(),Facebook.FacebookEmail[].class);
        System.out.println(ges);
        String email = "defaultmail@test.com";
        for(FacebookEmail ge : ges) {
            if(ge.primary){
                email = ge.email;
                break;
            }
        }
        return email;
    }


    private class FacebookEmail {
        public String email;

        public boolean verified;

        public boolean primary;
    }
}
