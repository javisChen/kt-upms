package com.kt.upms.module.route.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class RouteElementVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long routeId;
    private String name;
    private Long permissionId;
    private String permissionCode;
    private Integer type;

}
