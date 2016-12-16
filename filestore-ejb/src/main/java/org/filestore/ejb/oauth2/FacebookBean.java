package org.filestore.ejb.oauth2;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.filestore.api.oauth2.Facebook;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.io.IOException;

/**
 * Created by jerome on 15/12/2016.
 */
@Stateless(name = "facebook")
public class FacebookBean extends GenericOAuthBean implements Facebook {

    private static final OAuthProviderType PROVIDER = OAuthProviderType.FACEBOOK;

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
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest bearerClientRequest = null;
        OAuthResourceResponse resourceResponse = null;
        try {
            bearerClientRequest = new OAuthBearerClientRequest("https://graph.facebook.com/me?fields=email")
                    .setAccessToken(token).buildQueryMessage();
            resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject(resourceResponse.getBody());
        if(!jsonObj.get("email").equals("")){
            return (String) jsonObj.get("email");
        }
        else{
            return null;
        }
    }
}
