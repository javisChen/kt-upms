package com.kt.model.vo.permission;


import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionElementsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long routeId;
    private String name;
    private String code;
    private Integer type;

}
