package com.kt.model.dto.usergroup;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserGroupUserAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Long> userIds;

    private Long userGroupId;
}
