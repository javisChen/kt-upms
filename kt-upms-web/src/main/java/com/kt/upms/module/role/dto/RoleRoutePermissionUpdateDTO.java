package com.kt.upms.module.role.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色路由授权入参
 */
@Data
public class RoleRoutePermissionUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private List<Long> toAddRoutePermissionIds;
    private List<Long> toRemoveRoutePermissionIds;

    private List<Long> toAddElementPermissionIds;
    private List<Long> toRemoveElementPermissionIds;

}
