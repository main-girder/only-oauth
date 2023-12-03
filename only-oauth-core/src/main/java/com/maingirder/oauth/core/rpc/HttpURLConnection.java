package com.maingirder.oauth.core.rpc;

import java.util.Map;

/**
 * @author maxy
 * @since 1.0.0
 */
public class HttpURLConnection implements Http<String> {

    @Override
    public String get(String url) {
        return null;
    }

    @Override
    public String get(String url, Map<String, String> params, HttpHeader header, Boolean encode) {
        return null;
    }

    @Override
    public String post(String url) {
        return null;
    }

    @Override
    public String post(String url, String data) {
        return null;
    }

    @Override
    public String post(String url, String data, HttpHeader header) {
        return null;
    }

    @Override
    public String post(String url, Map<String, String> params, Boolean encode) {
        return null;
    }

    @Override
    public String post(String url, Map<String, String> params, HttpHeader header, Boolean encode) {
        return null;
    }
}
