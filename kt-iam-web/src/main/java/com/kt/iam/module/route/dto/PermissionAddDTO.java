package com.kt.iam.module.route.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限类型 MENU-菜单权限；PAGE_ELEMENT-页面元素；FILE-文件；SER_API-内部服务API；OPEN_API-开放API
     */
    private String type;


}
