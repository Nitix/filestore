package org.filestore.api.oauth2;

import org.apache.oltu.oauth2.common.OAuthProviderType;

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
    public String getUserEmail() {
        return null;
    }

}
