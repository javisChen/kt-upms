package com.kt.upms.module.route.dto;

import com.kt.component.validator.ValidateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class PageElementUpdateDTO {

    @NotNull(message = "routeId 不能为空", groups = ValidateGroup.Update.class)
    private Long id;

    @NotNull(message = "routeId 不能为空", groups = {ValidateGroup.Add.class})
    private Long routeId;

    @NotBlank(message = "name 不能为空", groups = {ValidateGroup.Add.class})
    private String name;

    @NotNull(message = "type 不能为空", groups = {ValidateGroup.Add.class})
    private Integer type;
}
