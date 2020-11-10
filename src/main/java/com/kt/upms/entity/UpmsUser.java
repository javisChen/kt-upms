package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.db.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

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
@Accessors(chain = true)
public class UpmsUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @NotBlank(message = "name 不能为空")
    @TableField("name")
    private String name;

    /**
     * 手机号码
     */
    @NotBlank(message = "phone 不能为空")
    @TableField("phone")
    private String phone;

    /**
     * 用户密码
     */
    @NotBlank(message = "phone 不能为空")
    @TableField("password")
    private String password;

    /**
     * 用户状态：1-正常；2-锁定；
     */
    @NotBlank(message = "status 不能为空")
    @TableField("status")
    private Integer status;


}
