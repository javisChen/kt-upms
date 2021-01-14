package com.kt.upms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户组表
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsUserGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组名称
     */
    @TableField("name")
    private String name;

    /**
     * 用户组名称
     */
    @TableField("pid")
    private Long pid;

    /**
     * 状态 1-已启用；2-已禁用；
     */
    @TableField("status")
    private Integer status;

    /**
     * 状态 1-已启用；2-已禁用；
     */
    @TableField("level")
    private Integer level;

    @TableField("level_path")
    private String levelPath;

    @TableField(value = "is_deleted")
    @TableLogic
    private Long isDeleted;


}
