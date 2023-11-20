package com.maingirder.oauth.core.annotation;


import com.maingirder.oauth.core.enums.OAuthPlatformEnum;

import java.lang.annotation.*;

/**
 * @author maxy
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Platform {

    OAuthPlatformEnum name();

}
