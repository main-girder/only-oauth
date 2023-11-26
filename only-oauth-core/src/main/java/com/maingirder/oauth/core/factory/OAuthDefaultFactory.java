package com.maingirder.oauth.core.factory;

import com.maingirder.oauth.core.annotation.Platform;
import com.maingirder.oauth.core.enums.OAuthPlatformEnum;
import com.maingirder.oauth.core.handler.OAuthHandler;
import com.maingirder.oauth.core.model.OAuthProcess;
import com.maingirder.oauth.core.utils.ClassLoaderUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 通过工厂来获取平台
 *
 * @author maxy
 * @since 1.0.0
 */
public final class OAuthDefaultFactory implements OAuthFactory {

    private static final Map<OAuthPlatformEnum, Class<?>> CONTAINER = new EnumMap<>(OAuthPlatformEnum.class);

    public OAuthDefaultFactory() {
        load();
    }

    /**
     * 获取所有实现类并装入容器中
     */
    private void load() {
        List<Class<?>> implClassList = ClassLoaderUtils.getListByClass(OAuthHandler.class);
        for (Class<?> clazz : implClassList) {
            if (clazz.isAnnotationPresent(Platform.class)) {
                Platform platform = clazz.getAnnotation(Platform.class);
                CONTAINER.put(platform.name(), clazz);
            }
        }
    }

    @Override
    public OAuthHandler getPlatformHandler(OAuthPlatformEnum platformName, OAuthProcess process) throws Exception {
        return (OAuthHandler) CONTAINER.get(platformName).getConstructor(OAuthProcess.class).newInstance(process);
    }
}
