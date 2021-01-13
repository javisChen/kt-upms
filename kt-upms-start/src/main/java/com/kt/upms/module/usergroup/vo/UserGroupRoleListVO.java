package com.kt.upms.module.usergroup.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserGroupRoleListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long userGroupId;

    private String name;

    private String phone;
}
