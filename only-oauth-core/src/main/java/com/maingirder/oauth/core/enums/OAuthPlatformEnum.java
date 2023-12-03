package com.maingirder.oauth.core.enums;

import com.maingirder.oauth.process.annotation.DynamicEnum;

/**
 * oauth platform general config
 *
 * @author jason-guo-cr
 * @version 1.0.0
 * @since 2023/11/20 22:00
 */
@DynamicEnum
public enum OAuthPlatformEnum {

    /**
     * 给一个默认值
     */
    UNKNOWN("unknown", "unknown", "unknown", "unknown", "unknown");;

    private final String authorizeUrl;

    private final String accessTokenUrl;

    private final String userInfoUrl;

    private final String revokeUrl;

    private final String refreshTokenUrl;


    OAuthPlatformEnum(String authorizeUrl, String accessTokenUrl, String userInfoUrl, String revokeUrl, String refreshTokenUrl) {
        this.authorizeUrl = authorizeUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.revokeUrl = revokeUrl;
        this.refreshTokenUrl = refreshTokenUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getRevokeUrl() {
        return revokeUrl;
    }

    public String getRefreshTokenUrl() {
        return refreshTokenUrl;
    }
}
