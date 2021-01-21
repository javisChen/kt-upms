package com.kt.upms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ApiAuthTypeStatusEnums {

    NO_NEED(1, "无需认证授权"),
    NEE_AUTHENTICATION(2, "只需认证无需授权"),
    NEED_ALL(3, "需要认证和授权");

    ApiAuthTypeStatusEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @EnumValue
    private int value;
    private String desc;

    public int getValue() {
        return value;
    }
}