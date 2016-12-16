package org.filestore.ejb.oauth2;

import com.google.gson.Gson;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.filestore.api.oauth2.Google;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jerome on 15/12/2016.
 */
@Stateless(name = "google")
public class GoogleBean extends GenericOAuthBean implements Google {

    private static final OAuthProviderType PROVIDER = OAuthProviderType.GOOGLE;

    @Override
    public OAuthClientRequest createTokenRequest(String code) throws OAuthSystemException {
        return OAuthClientRequest.tokenProvider(getProvider())
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(getClientId())
                .setClientSecret(getClientSecret())
                .setRedirectURI(getRedirectUri())
                .setCode(code)
                .buildBodyMessage();
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
        return googleAccount.email;
    }

    private class GoogleAccount{

        public String email;

    }

}
