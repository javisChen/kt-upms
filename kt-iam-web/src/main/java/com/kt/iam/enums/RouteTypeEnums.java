package com.kt.iam.enums;

public enum RouteTypeEnums {

    MENU(1, "菜单路由"),
    PAGE(2, "页面路由");

    RouteTypeEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    private int value;
    private String desc;

    public int getValue() {
        return value;
    }
}