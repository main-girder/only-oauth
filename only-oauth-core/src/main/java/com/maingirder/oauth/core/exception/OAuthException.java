package com.maingirder.oauth.core.exception;

import com.maingirder.oauth.core.enums.OAuthResponseResultEnum;
import lombok.Getter;

/**
 * Only-OAuth 通用异常类
 *
 * @author maxy
 * @since 1.0.0
 */
@Getter
public class OAuthException extends RuntimeException {

    private int errorCode;

    private String errorMsg;

    public OAuthException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public OAuthException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public OAuthException(OAuthResponseResultEnum e) {
        super(e.getMessage());
        this.errorCode = e.getCode();
        this.errorMsg = e.getMessage();
    }

}
