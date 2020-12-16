package com.kt.model.vo.usergroup;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserGroupUserListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long userGroupId;

    private String name;

    private String phone;
}
