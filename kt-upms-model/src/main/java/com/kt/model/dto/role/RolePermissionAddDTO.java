package com.kt.model.dto.role;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RolePermissionAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private List<Long> permissionIds;

}
