package com.kt.model.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserGroupQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户名称
     */
    private String name;

}
