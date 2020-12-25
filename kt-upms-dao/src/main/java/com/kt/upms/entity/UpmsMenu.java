package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class UpmsMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public UpmsMenu(Long id, String name, Long pid, String levelPath, Integer level, String icon) {
        super(id);
        this.name = name;
        this.pid = pid;
        this.levelPath = levelPath;
        this.level = level;
        this.icon = icon;
    }

    /**
     * 元素名称
     */
    @TableField("name")
    private String name;

    /**
     * 唯一键
     */
    @TableField("code")
    private String code;

    /**
     * 路由类型 1-普通菜单；2-路由菜单
     */
    @TableField("type")
    private Integer type;

    /**
     * 隐藏子菜单
     */
    @TableField("hide_children")
    private Boolean hideChildren;

    /**
     * 路由id，关联upms_route.id；type=2时，需要绑定路由
     */
    @TableField("route_id")
    private Long routeId;

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
     * 图标
     */
    @TableField("icon")
    private String icon;


}
