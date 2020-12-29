package com.kt.model.vo.route;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RouteDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long pid;
    private Integer sequence;
    private String code;
    private String name;
    private String icon;
    private String component;
    private Integer status;
    private String path;
    private String levelPath;
}
