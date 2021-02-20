package com.kt.iam.module.usergroup.persistence;


import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户组与角色关联表
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IamUserGroupRoleRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组id，关联iam_user_group.id
     */
    @TableField("user_group_id")
    private Long userGroupId;

    /**
     * 角色id，关联iam_role.id
     */
    @TableField("role_id")
    private Long roleId;


}
