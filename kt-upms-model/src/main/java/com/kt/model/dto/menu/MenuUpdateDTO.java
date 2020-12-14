package com.kt.model.dto.menu;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MenuUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类型 MENU-菜单权限；PAGE_ELEMENT-页面元素；FILE-文件；SER_API-内部服务API；OPEN_API-开放API
     */
    private String type;

}
