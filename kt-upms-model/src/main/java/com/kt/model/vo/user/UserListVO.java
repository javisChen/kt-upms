package com.kt.model.vo.user;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String phone;
    private String name;
    private Integer status;
    private List<String> roles;
    private List<String> userGroups;
}
