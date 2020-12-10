package com.kt.upms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpmsUserRoleRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id，关联upms_role.id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色id，关联upms_role.id
     */
    @TableField("role_id")
    private Long roleId;


}
