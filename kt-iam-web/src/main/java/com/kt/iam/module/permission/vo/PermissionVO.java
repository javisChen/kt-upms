package com.kt.iam.module.permission.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long permissionId;

    private String permissionCode;
}
