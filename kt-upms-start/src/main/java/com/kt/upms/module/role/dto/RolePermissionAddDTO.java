package com.kt.upms.module.role.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RolePermissionAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private List<Long> routePermissionIds;
    private List<Long> elementPermissionIds;

}
