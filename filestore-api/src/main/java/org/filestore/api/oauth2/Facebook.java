package org.filestore.api.oauth2;

import org.apache.oltu.oauth2.common.OAuthProviderType;

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
    public String getUserEmail() {
        return null;
    }
}
