package com.kt.upms.module.permission.bo;

import lombok.Data;

@Data
public class ApiPermissionBO {

    private String permissionType;
    private String permissionCode;
    private String permissionId;
    private String apiId;
    private String apiName;
    private String apiUrl;
    private String apiMethod;
    private String apiAuthType;
}
