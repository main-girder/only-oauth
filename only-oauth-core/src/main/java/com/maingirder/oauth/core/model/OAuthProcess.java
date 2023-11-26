package com.maingirder.oauth.core.model;

import com.maingirder.oauth.core.enums.OAuthPlatformEnum;
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
     * 平台信息枚举（编译期读取配置文件动态生成枚举）
     */
    private OAuthPlatformEnum authPlatformEnum;
    /**
     * 基础配置类对象
     */
    private OAuthBaseConfig authBaseConfig;
    /**
     * 回到状态缓存
     */
    private OAuthStateCache oAuthStateCache;
}
