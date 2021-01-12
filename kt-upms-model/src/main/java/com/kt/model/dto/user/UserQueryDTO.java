package com.kt.model.dto.user;


import com.kt.component.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends PagingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态：1-正常；2-锁定；
     */
    private Integer status;
}
