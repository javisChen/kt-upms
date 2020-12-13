package com.kt.model.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态：1-正常；2-锁定；
     */
    private Integer status;
}
