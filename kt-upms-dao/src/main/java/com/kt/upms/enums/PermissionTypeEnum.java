package com.kt.upms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PermissionTypeEnum {

    MENU("MENU", "菜单权限"),
    PAGE_ELEMENT("PAGE_ELEMENT", "页面元素"),
    FILE("FILE", "文件"),
    SER_API("SER_API", "内部服务API"),
    OPEN_API("OPEN_API", "开放API");

    PermissionTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @EnumValue
    private String value;
    private String desc;

}