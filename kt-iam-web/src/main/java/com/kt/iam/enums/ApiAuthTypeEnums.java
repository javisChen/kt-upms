package com.kt.iam.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ApiAuthTypeEnums {

    NO_AUTHENTICATION_AND_AUTHORIZATION(1, "无需认证授权"),
    NEED_AUTHENTICATION(2, "只需认证无需授权"),
    NEED_AUTHENTICATION_AND_AUTHORIZATION(3, "需要认证和授权");

    ApiAuthTypeEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    private int value;
    private String desc;

    public int getValue() {
        return value;
    }
}