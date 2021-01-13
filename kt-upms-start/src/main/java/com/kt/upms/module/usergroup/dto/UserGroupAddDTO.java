package com.kt.upms.module.usergroup.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserGroupAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "name 不能为空")
    private String name;

    private Long pid;

}
