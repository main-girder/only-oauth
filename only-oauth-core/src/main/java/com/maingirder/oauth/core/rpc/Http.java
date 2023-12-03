package com.maingirder.oauth.core.rpc;

import java.util.Map;


/**
 * @author maxy
 * @since 1.0.0
 */
public interface Http<T> {


    /**
     * GET 请求
     *
     * @param url URL
     * @return 结果
     */
    T get(String url);


    /**
     * GET 请求
     *
     * @param url    URL
     * @param params 参数
     * @param header 请求头
     * @param encode 是否需要 url encode
     * @return 结果
     */
    T get(String url, Map<String, String> params, HttpHeader header, Boolean encode);


    /**
     * POST 请求
     *
     * @param url URL
     * @return 结果
     */
    T post(String url);

    /**
     * POST 请求
     *
     * @param url  URL
     * @param data JSON 参数
     * @return 结果
     */
    T post(String url, String data);


    /**
     * POST 请求
     *
     * @param url    URL
     * @param data   JSON 参数
     * @param header 请求头
     * @return 结果
     */
    T post(String url, String data, HttpHeader header);


    /**
     * POST 请求
     *
     * @param url    URL
     * @param params form 参数
     * @param encode 是否需要 url encode
     * @return 结果
     */
    T post(String url, Map<String, String> params, Boolean encode);


    /**
     * POST 请求
     *
     * @param url    URL
     * @param params form 参数
     * @param header 请求头
     * @param encode 是否需要 url encode
     * @return 结果
     */
    T post(String url, Map<String, String> params, HttpHeader header, Boolean encode);


}   