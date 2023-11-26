package com.maingirder.oauth.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调用三方平台错误状态码对照表
 *
 * @author maxy
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum OAuthResponseResultEnum {
    /**
     * 2000：正常；
     * other：调用异常，具体异常内容见{@code msg}
     */
    SUCCESS(2000, "Success"),
    FAILURE(5000, "Failure"),
    UNIMPLEMENTED(5001, "Unimplemented"),
    UNSUPPORTED_OPERATION(5003, "Unsupported operation"),
    ;

    private Integer code;
    private String message;

}
