package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

}
