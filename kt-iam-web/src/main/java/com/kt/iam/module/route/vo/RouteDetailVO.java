package com.kt.iam.module.route.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteDetailVO extends RouteBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String parentRouteName;
    private List<PageElementVO> elements;

}
