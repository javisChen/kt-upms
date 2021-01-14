package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UpmsRoute extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 元素名称
     */
    @TableField("name")
    private String name;

    /**
     * 应用id
     */
    @TableField("application_id")
    private Long applicationId;

    /**
     * 唯一键
     */
    @TableField("code")
    private String code;

    /**
     * 组件名
     */
    @TableField("component")
    private String component;

    /**
     * 路由类型 1：菜单路由 2：页面路由
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否隐藏子路由 0-否 1-是
     */
    @TableField("hide_children")
    private Boolean hideChildren;

    /**
     * 父级菜单id
     */
    @TableField("pid")
    private Long pid;

    /**
     * 菜单层级路径，例如：0.1.2 代表该菜单是三级菜单，上级菜单的id是1,再上级的菜单id是0
     */
    @TableField("level_path")
    private String levelPath;

    /**
     * 菜单层级
     */
    @TableField("level")
    private Integer level;

    /**
     * 排序序号
     */
    @TableField("sequence")
    private Integer sequence;

    /**
     * 路径
     */
    @TableField("path")
    private String path;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 状态 1-已启用；2-已禁用；
     */
    @TableField("status")
    private Integer status;

    @TableField(value = "is_deleted")
    @TableLogic
    private Long isDeleted;

    public UpmsRoute(Long id, String name, Long pid, String levelPath, Integer level, String path, String icon) {
        super(id);
        this.name = name;
        this.pid = pid;
        this.levelPath = levelPath;
        this.level = level;
        this.path = path;
        this.icon = icon;
    }

}
