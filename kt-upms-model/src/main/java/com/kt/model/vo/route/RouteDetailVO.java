package com.kt.model.vo.route;


import com.kt.model.vo.pageelement.PageElementVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RouteDetailVO implements Serializable {

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
    private Boolean group;
    private List<PageElementVO> elements;

}
