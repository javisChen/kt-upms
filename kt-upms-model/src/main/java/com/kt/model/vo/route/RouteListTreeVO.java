package com.kt.model.vo.route;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteListTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long pid;
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
    private List<RouteListTreeVO> children;
    private Boolean group;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
