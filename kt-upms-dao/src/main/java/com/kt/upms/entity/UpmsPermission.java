package com.kt.upms.entity;

import com.kt.db.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpmsPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称
     */
    @TableField("name")
    private String name;

    /**
     * 权限类型 MENU-菜单权限；PAGE_ELEMENT-页面元素；FILE-文件；SER_API-内部服务API；OPEN_API-开放API
     */
    @TableField("type")
    private String type;

    /**
     * 权限编码
     */
    @TableField("code")
    private String code;

    /**
     * 权限状态 1-正常 2-禁用
     */
    @TableField("status")
    private Boolean status;


}
