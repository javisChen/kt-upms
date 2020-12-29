package com.kt.model.enums;

public enum BizEnums {

    /**
     * A0101 ~ A0199是upms系统编码
     */
    USER_ALREADY_EXISTS("A0101", "手机号已存在"),
    USER_GROUP_ALREADY_EXISTS("A0102", "用户组名称已存在"),
    ROLE_ALREADY_EXISTS("A0103", "角色名称已存在"),
    PERMISSION_ALREADY_EXISTS("A0104", "权限已存在"),
    ROUTE_ALREADY_EXISTS("A0105", "路由已存在"),
    PARENT_ROUTE_NOT_EXISTS("A0106", "父级路由不存在"),
    ROUTE_NOT_EXISTS("A0107", "路由不存在"),
    USER_GROUP_NOT_EXISTS("A0108", "用户组不存在"),
    ROUTE_COMPONENT_IS_REQUIRE("A0109", "组件名不能为空"),
    ROUTE_CODE_ALREADY_EXISTS("A0110", "路由编码已存在"),

    USER_LOGIN_INVALID("A0200", "用户名或密码错误");

    private String code;
    private String msg;

    BizEnums(String code, String msg) {
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
