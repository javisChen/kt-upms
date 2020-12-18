package com.kt.model.dto.usergroup;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserGroupRoleAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Add add;

    private Remove remove;

    @Data
    public class Add {

        private List<Long> roleIds;

        private Long userGroupId;
    }

    @Data
    public class Remove {

        private List<Long> roleIds;

        private Long userGroupId;
    }
}
