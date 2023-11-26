package com.maingirder.oauth.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 基础配置类（通过继承扩展）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthBaseConfig {

    /**
     * 客户端id：对应各平台的appKey
     */
    private String clientId;

    /**
     * 客户端Secret：对应各平台的appSecret
     */
    private String clientSecret;

    /**
     * 登录成功后的回调地址
     */
    private String redirectUri;

    /**
     * 是否需要申请unionid，目前只针对qq登录
     */
    private boolean unionId;

    /**
     * Stack Overflow Key
     */
    private String stackOverflowKey;

    /**
     * 企业微信，授权方的网页应用ID
     */
    private String agentId;

    /**
     * 使用 Coding 登录时，需要传该值。
     */
    private String codingGroupName;
    /**
     * 忽略校验
     */
    private boolean ignoreCheckState;

    /**
     * 支持自定义授权平台的 scope 内容
     */
    private List<String> scopes;

    /**
     * 设备ID, 设备唯一标识ID
     */
    private String deviceId;

}
