package com.kt.model.dto.menu;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MenuAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元素名称
     */
    @NotBlank(message = "name 不能为空")
    private String name;

    /**
     * 父级菜单id
     */
    @NotNull(message = "pid 不能为空")
    private Integer pid;

    /**
     * 图标
     */
    private String icon;



}
