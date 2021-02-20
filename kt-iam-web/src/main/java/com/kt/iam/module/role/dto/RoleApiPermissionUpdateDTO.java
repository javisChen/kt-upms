package com.kt.iam.module.role.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色路由授权入参
 */
@Data
public class RoleApiPermissionUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private List<Long> toAddApiPermissionIds;
    private List<Long> toRemoveApiPermissionIds;

}
