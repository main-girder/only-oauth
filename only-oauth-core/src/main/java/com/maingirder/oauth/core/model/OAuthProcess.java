package com.maingirder.oauth.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权流程对象
 * @author maxy
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthProcess {

    /**
     * 基础配置类对象
     */
    private OAuthBaseConfig authBaseConfig;
    /**
     * 状态缓存
     */
    private OAuthStateCache oAuthStateCache;
}
