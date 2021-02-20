package com.kt.iam.module.user.persistence;


import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class IamUserRoleRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id，关联iam_role.id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色id，关联iam_role.id
     */
    @TableField("role_id")
    private Long roleId;


}
