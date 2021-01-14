package com.kt.upms.module.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 20)
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
