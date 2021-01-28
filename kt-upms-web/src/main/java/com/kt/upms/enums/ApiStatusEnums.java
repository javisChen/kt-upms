package com.kt.upms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ApiStatusEnums {

    ENABLED(1, "已启用"),
    DISABLED(2, "已禁用");

    ApiStatusEnums(int value, String desc) {
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