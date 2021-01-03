package com.kt.model.dto.menu;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RouteAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "name 不能为空")
    private String name;

    @NotNull(message = "pid 不能为空")
    private Long pid;

    @NotBlank(message = "code 不能为空")
    private String code;

    @NotNull(message = "status 不能为空")
    @Range(min = 1, max = 2)
    private Integer status;

    private String component;

    private Boolean hideChildren;

    private String path;

    private String icon;

    private Integer sequence;

}
