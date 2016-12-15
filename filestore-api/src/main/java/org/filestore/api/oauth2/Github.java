package org.filestore.api.oauth2;

import com.google.gson.Gson;
import org.apache.oltu.oauth2.common.OAuthProviderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * Created by nitix on 15/12/16.
 */
public class Github extends Generic {

    private static final OAuthProviderType PROVIDER = OAuthProviderType.GITHUB;

    public Github(Configuration configuration) {
        super(configuration);
    }

    @Override
    public OAuthProviderType getProvider() {
        return PROVIDER;
    }

    @Override
    protected String getScope() {
        return "user:email";
    }

    @Override
    public String getUserEmail(String token) throws IOException {
        URL url = new URL("https://api.github.com/user/emails?access_token=" + token);
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
        GithubEmail[] ges = new Gson().fromJson(outputString.toString(),GithubEmail[].class);
        String email = "";
        for(GithubEmail ge : ges) {
            if(ge.primary){
                email = ge.email;
                break;
            }
        }
        return email;
    }


    private class GithubEmail {
        public String email;

        public boolean verified;

        public boolean primary;
    }

}
