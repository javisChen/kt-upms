package com.kt.upms.module.route.vo;

import lombok.Data;

@Data
public class RouteBaseVO {

    private Long id;
    private Long pid;
    private Long permissionId;
    private String permissionCode;
    private Integer sequence;
    private String code;
    private String name;
    private String icon;
    private String component;
    private String levelPath;
    private Integer status;
    private String path;
    private Integer type;
    private Boolean hideChildren;
}
