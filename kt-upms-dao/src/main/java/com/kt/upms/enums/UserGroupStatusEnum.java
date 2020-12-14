package com.kt.upms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserGroupStatusEnum {

    ENABLED(1, "已启用"),
    DISABLED(2, "已禁用");

    UserGroupStatusEnum(int value, String desc) {
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