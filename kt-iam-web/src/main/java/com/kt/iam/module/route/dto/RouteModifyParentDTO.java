package com.kt.iam.module.route.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RouteModifyParentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    private Long id;

    /**
     * 父级菜单id
     */
    @NotNull(message = "pid 不能为空")
    private Long pid;

}
