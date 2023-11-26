package com.maingirder.oauth.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum UserGenderEnum {

    MALE("1", "男"),
    FEMALE("2", "女"),
    UNKNOWN("-1", "未知");

    private String code;
    private String desc;
}