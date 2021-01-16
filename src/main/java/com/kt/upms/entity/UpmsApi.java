package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * api表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * API名称
     */
    @TableField("name")
    private String name;

    /**
     * 应用id
     */
    @TableField("application_id")
    private Long applicationId;

    /**
     * 接口地址
     */
    @TableField("url")
    private String url;

    /**
     * http方法
     */
    @TableField("method")
    private Integer method;

    /**
     * 认证授权类型
     */
    @TableField("auth_type")
    private Integer authType;

    /**
     * 状态 1-已启用；2-已禁用；
     */
    @TableField("status")
    private Integer status;

    @TableField(value = "is_deleted")
    private Long isDeleted;
}
