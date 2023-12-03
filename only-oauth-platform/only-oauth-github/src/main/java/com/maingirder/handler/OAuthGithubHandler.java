package com.maingirder.handler;

import com.maingirder.oauth.core.handler.OAuthHandler;
import com.maingirder.oauth.core.model.OAuthBaseToken;
import com.maingirder.oauth.core.model.OAuthProcess;
import com.maingirder.oauth.core.model.OAuthUser;

/**
 * @author maxy
 * @since 1.0.0
 */
public class OAuthGithubHandler implements OAuthHandler {

    @Override
    public String authorize(String state) {
        return null;
    }

    @Override
    public OAuthBaseToken getAccessToken(OAuthProcess authProcess) {
        return null;
    }

    @Override
    public OAuthUser getUserInfo(OAuthProcess authProcess) {
        return null;
    }
}
