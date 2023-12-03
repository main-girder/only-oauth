package com.maingirder.oauth.core.handler;

import com.maingirder.oauth.core.enums.OAuthResponseResultEnum;
import com.maingirder.oauth.core.exception.OAuthException;
import com.maingirder.oauth.core.model.OAuthBaseToken;
import com.maingirder.oauth.core.model.OAuthProcess;
import com.maingirder.oauth.core.model.OAuthResponse;
import com.maingirder.oauth.core.model.OAuthUser;

/**
 * * OAuth平台的API地址的统一接口，提供以下方法：
 * 1) {@link OAuthHandler#authorize}: 获取授权url. 必须实现
 * 2) {@link OAuthHandler#getAccessToken}: 获取accessToken的url. 必须实现
 * 3) {@link OAuthHandler#getUserInfo}: 获取用户信息. 必须实现
 * 4) {@link OAuthHandler#cancel}: 取消授权的url. 非必须实现接口（部分平台不支持）
 * 5) {@link OAuthHandler#refresh}: 刷新授权的url. 非必须实现接口（部分平台不支持）
 *
 * @author max
 * @since 1.0.0
 */
public interface OAuthHandler {

    String authorize(String state);

    OAuthBaseToken getAccessToken(OAuthProcess authProcess);

    OAuthUser getUserInfo(OAuthProcess authProcess);

    default OAuthResponse login(OAuthProcess authProcess) {
        throw new OAuthException(OAuthResponseResultEnum.UNIMPLEMENTED);
    }

    default OAuthResponse cancel(OAuthProcess authProcess) {
        throw new OAuthException(OAuthResponseResultEnum.UNIMPLEMENTED);
    }

    default OAuthResponse refresh(OAuthProcess authProcess) {
        throw new OAuthException(OAuthResponseResultEnum.UNIMPLEMENTED);
    }

}
