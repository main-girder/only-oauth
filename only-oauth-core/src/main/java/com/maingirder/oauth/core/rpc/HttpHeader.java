package com.maingirder.oauth.core.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * http header
 * @author maxy
 * @since 1.0.0
 */
public class HttpHeader {

    private final Map<String, String> headers;

    public HttpHeader() {
        this.headers = new HashMap<>();
    }

    public HttpHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpHeader add(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpHeader addAll(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }
}