package com.kt.model.enums;

public enum BizEnum {

    /**
     * A0101 ~ A0200是upms系统编码
     */
    PHONE_ALREADY_EXISTS("A0101", "手机号已存在"),
    USER_GROUP_NAME_ALREADY_EXISTS("A0102", "用户组名称已存在"),
    ROLE_NAME_ALREADY_EXISTS("A0103", "角色名称已存在");

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
