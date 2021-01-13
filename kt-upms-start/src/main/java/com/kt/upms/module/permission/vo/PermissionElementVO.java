package com.kt.upms.module.permission.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionElementVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long routeId;
    private String name;
    private String code;
    private Integer type;

}