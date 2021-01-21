package com.kt.upms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色与权限关联表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsPermissionRoleRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id，关联upms_permission.id
     */
    @TableField("permission_id")
    private Long permissionId;

    /**
     * 角色id，关联upms_role.id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 应用id,关联upms_application.id
     */
    @TableField("application_id")
    private Long applicationId;

    /**
     * 权限类型 FRONT_ROUTE-前端路由；PAGE_ELEMENT-页面元素；FILE-文件；INTERNAL_API-内部服务API；OPEN_API-开放API
     */
    @TableField("type")
    private String type;


}
