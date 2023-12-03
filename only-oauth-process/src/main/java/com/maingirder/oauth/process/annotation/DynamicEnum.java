package com.maingirder.oauth.process.annotation;

/**
 * dynamic enum
 *
 * @author jishan.guo
 * @version 1.0.0
 * @since 2023/11/20 16:55
 */
public @interface DynamicEnum {

    String config() default "oauth-platform-config.json";

}
