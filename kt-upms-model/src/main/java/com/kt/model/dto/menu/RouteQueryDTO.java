package com.kt.model.dto.menu;


import lombok.Data;

import java.io.Serializable;

@Data
public class RouteQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Long pid;

    private String path;

    private Integer status;

}
