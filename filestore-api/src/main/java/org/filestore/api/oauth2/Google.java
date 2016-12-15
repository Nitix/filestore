package org.filestore.api.oauth2;

import org.apache.oltu.oauth2.common.OAuthProviderType;

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
        return "userinfo.email";
    }

    @Override
    public String getUserEmail(String token) {
        return null;
    }
}
