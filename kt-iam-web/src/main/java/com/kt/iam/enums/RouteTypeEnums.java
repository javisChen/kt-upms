package com.kt.iam.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum RouteTypeEnums {

    MENU(1, "菜单路由"),
    PAGE(2, "页面路由");

    RouteTypeEnums(int value, String desc) {
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