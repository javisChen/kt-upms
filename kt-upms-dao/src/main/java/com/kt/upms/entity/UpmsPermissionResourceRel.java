package com.kt.upms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限与资源关联表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsPermissionResourceRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id，关联upms_permission.id
     */
    @TableField("permission_id")
    private Long permissionId;

    /**
     * 资源id，关联各种不同资源（菜单、菜单元素、文件、API统称为资源）主表的id
     */
    @TableField("resource_id")
    private Long resourceId;

    /**
     * 权限类型 MENU-菜单权限；PAGE_ELEMENT-页面元素；FILE-文件；SER_API-内部服务API；OPEN_API-开放API
     */
    @TableField("resource_type")
    private String resourceType;


}
