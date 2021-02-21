package com.kt.iam.enums;

public enum UserGroupInheritTypeEnums {

    NO_INHERIT(0, "不继承"),
    INHERIT_PARENT(1, "继承上级用户组"),
    INHERIT_ALL(2, "继承所有用户组");

    UserGroupInheritTypeEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private int value;
    private String desc;

    public int getValue() {
        return value;
    }
}