package com.maingirder.oauth.core.factory;

import com.maingirder.oauth.core.enums.OAuthPlatformEnum;
import com.maingirder.oauth.core.handler.OAuthHandler;

public interface OAuthFactory {
    /**
     * 根据平台名称获取对应平台
     * @param platformName
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    OAuthHandler getPlatformHandler(OAuthPlatformEnum platformName) throws InstantiationException, IllegalAccessException;
}
