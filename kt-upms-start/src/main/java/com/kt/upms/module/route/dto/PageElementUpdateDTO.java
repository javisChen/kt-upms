package com.kt.upms.module.route.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class PageElementUpdateDTO {

    @NotNull(message = "routeId 不能为空")
    private Long id;

    private Long routeId;

    private String name;

    private Integer type;
}
