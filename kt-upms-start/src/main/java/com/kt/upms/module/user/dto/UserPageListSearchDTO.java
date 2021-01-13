package com.kt.upms.module.user.dto;


import com.kt.component.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询用户列表搜索入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPageListSearchDTO extends PagingDTO {

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
