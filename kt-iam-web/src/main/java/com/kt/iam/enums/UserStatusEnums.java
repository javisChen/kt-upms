package com.kt.iam.enums;

public enum UserStatusEnums {

    ENABLED(1, "已启用"),
    DISABLED(2, "已禁用");

    UserStatusEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private int value;
    private String desc;

    public int getValue() {
        return value;
    }
}