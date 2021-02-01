package com.kt.upms.module.role.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 角色路由授权入参（直接授予应用下的所有api）
 */
@Data
public class RoleApplicationApiPermissionUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long applicationId;

}
