package com.kt.upms.module.usergroup.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserGroupUserAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Add add;

    private Remove remove;

    @Data
    public class Add {

        private List<Long> userIds;

        private Long userGroupId;
    }

    @Data
    public class Remove {

        private List<Long> userIds;

        private Long userGroupId;
    }
}
