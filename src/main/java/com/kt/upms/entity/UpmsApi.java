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
     * 接口分类id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 接口地址
     */
    @TableField("url")
    private String url;

    /**
     * http方法
     */
    @TableField("method")
    private String method;

    /**
     * 认证授权类型 1-无需认证授权 2-只需认证无需授权 3-需要认证和授权
     */
    @TableField("auth_type")
    private Integer authType;

    /**
     * url是否包含路径参数，例： /user/{userId}/api/{apiId} 0-不包含 1-包含
     */
    @TableField("has_path_variable")
    private Boolean hasPathVariable;

    /**
     * 状态 1-已启用；2-已禁用；
     */
    @TableField("status")
    private Integer status;

    @TableField(value = "is_deleted")
    private Long isDeleted;
}
