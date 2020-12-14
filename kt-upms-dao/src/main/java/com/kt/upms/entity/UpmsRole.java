package com.kt.upms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

}
