package com.maingirder.oauth.core.model;

/**
 * State缓存接口 (如需扩展请实现接口)
 *
 * @author maxy
 * @since 1.0.0
 */
public interface OAuthStateCache {
    /**
     * 写入缓存
     */
    void set(String key, String value);

    /**
     * 写入缓存并设置时间(毫秒)
     */
    void set(String key, String value, long timeout);

    /**
     * 获取缓存内容
     */
    String get(String key);

    /**
     * 是否存在key true 存在 false 不存在
     */
    Boolean hasKey(String key);
}
