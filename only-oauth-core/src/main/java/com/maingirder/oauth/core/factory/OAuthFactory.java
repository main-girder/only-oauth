package com.maingirder.oauth.core.factory;

import com.maingirder.oauth.core.enums.OAuthPlatformEnum;
import com.maingirder.oauth.core.handler.OAuthHandler;
import com.maingirder.oauth.core.model.OAuthProcess;

public interface OAuthFactory {
    /**
     * 根据平台名称获取对应平台
     *
     * @param platformName
     * @return
     * @throws Exception
     */
    OAuthHandler getPlatformHandler(OAuthPlatformEnum platformName, OAuthProcess process) throws Exception;
}
