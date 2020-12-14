package com.kt.model.enums;

public enum BizEnum {

    /**
     * A0101 ~ A0200是upms系统编码
     */
    USER_ALREADY_EXISTS("A0101", "手机号已存在"),
    USER_GROUP_ALREADY_EXISTS("A0102", "用户组名称已存在"),
    ROLE_ALREADY_EXISTS("A0103", "角色名称已存在"),
    PERMISSION_ALREADY_EXISTS("A0104", "权限已存在"),
    MENU_ALREADY_EXISTS("A0105", "菜单已存在"),
    PARENT_MENU_NOT_EXISTS("A0106", "父级菜单不存在");

    private String code;
    private String msg;

    BizEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
