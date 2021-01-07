package com.kt.model.vo.pageelement;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinitions;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class PageElementListVO {

    private Long routeId;
    private String name;
    private Integer type;
}
