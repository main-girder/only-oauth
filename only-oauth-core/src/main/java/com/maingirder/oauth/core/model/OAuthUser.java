package com.maingirder.oauth.core.model;


import com.maingirder.oauth.core.enums.UserGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 授权成功后的用户信息，根据授权平台的不同，获取的数据完整性也不同
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUser implements Serializable {
    /**
     * 用户第三方系统唯一标识
     */
    private String uuid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户网址
     */
    private String blog;
    /**
     * 所在公司
     */
    private String company;
    /**
     * 位置
     */
    private String location;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户介绍信息
     */
    private String remark;
    /**
     * 性别
     */
    private UserGenderEnum gender;
    /**
     * 用户来源
     */
    private String source;
    /**
     * 授权的token信息
     */
    private OAuthBaseToken token;
}
