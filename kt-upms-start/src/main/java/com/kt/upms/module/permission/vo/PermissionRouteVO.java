package com.kt.upms.module.permission.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRouteVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private List<PermissionRouteVO> children;
    private Boolean group;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
