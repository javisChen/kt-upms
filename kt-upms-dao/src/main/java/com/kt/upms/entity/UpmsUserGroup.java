package com.kt.upms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
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


}
