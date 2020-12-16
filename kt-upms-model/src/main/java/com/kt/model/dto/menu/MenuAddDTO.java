package com.kt.model.dto.menu;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MenuAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "name 不能为空")
    private String name;

    @NotNull(message = "pid 不能为空")
    private Long pid;

    private String path;

    private String icon;



}