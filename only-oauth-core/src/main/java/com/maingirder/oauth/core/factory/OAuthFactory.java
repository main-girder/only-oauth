package com.maingirder.oauth.core.factory;

import com.maingirder.oauth.core.annotation.Platform;
import com.maingirder.oauth.core.enums.OAuthPlatformEnum;
import com.maingirder.oauth.core.handler.OAuthHandler;
import com.maingirder.oauth.core.utils.ClassLoaderUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maxy
 * @since 1.0.0
 */
public class OAuthFactory {

    private static final Map<OAuthPlatformEnum, Class<?>> CONTAINER = new HashMap<>();

    /**
     * 获取所有实现类并装入容器中
     *
     * @throws ClassNotFoundException
     */
    private static Map<OAuthPlatformEnum, Class<?>> load() throws ClassNotFoundException {
        List<Class<?>> implClassList = ClassLoaderUtils.getListByClass(OAuthHandler.class);
        for (Class<?> clazz : implClassList) {
            if (clazz.isAnnotationPresent(Platform.class)) {
                Platform platform = clazz.getAnnotation(Platform.class);
                CONTAINER.put(platform.name(), clazz);
            }
        }
        return CONTAINER;
    }

    /**
     * 根据平台名称获取具体平台对象
     *
     * @param platformName
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static OAuthHandler get(OAuthPlatformEnum platformName) throws InstantiationException, IllegalAccessException {
        return (OAuthHandler) CONTAINER.get(platformName).newInstance();
    }


}
