package com.maingirder.oauth.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 关于token相关公共授权所需字段
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthBaseToken implements Serializable {

    private String accessToken;
    private int expireIn;
    private String refreshToken;
    private int refreshTokenExpireIn;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;


}
