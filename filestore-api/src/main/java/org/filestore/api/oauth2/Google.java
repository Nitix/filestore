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
public class Google extends Generic {

    private static final OAuthProviderType PROVIDER = OAuthProviderType.GOOGLE;

    public Google(Configuration configuration) {
        super(configuration);
    }

    @Override
    public OAuthProviderType getProvider() {
        return PROVIDER;
    }

    @Override
    protected String getScope() {
        return "https://www.googleapis.com/auth/userinfo.email";
    }

    @Override
    public String getUserEmail(String token) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token);
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
        GoogleAccount googleAccount = new Gson().fromJson(outputString.toString(), GoogleAccount.class);
        String email = "";
        for(GoogleAccount.GoogleEmail ge : googleAccount.emails) {
            if(ge.type.equals("account")){
                email = ge.value;
                break;
            }
        }
        return email;
    }

    private class GoogleAccount{

        public GoogleEmail[] emails;

        private class GoogleEmail {
            public String value;

            public String type;

        }
    }

}
