package com.maingirder.oauth.core.model;

import com.maingirder.oauth.core.enums.OAuthResponseResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthResponse<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public boolean success() {
        return this.code == OAuthResponseResultEnum.SUCCESS.getCode();
    }
}
