package com.kt.model.dto.user;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(message = "id 不能为空")
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户状态：1-正常；2-锁定；
     */
    private Integer status;
}
