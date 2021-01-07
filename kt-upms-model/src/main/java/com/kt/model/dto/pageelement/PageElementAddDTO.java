package com.kt.model.dto.pageelement;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class PageElementAddDTO {

    @NotNull(message = "routeId 不能为空")
    private Long routeId;

    @NotBlank(message = "name 不能为空")
    private String name;

    @NotNull(message = "type 不能为空")
    private Integer type;
}
