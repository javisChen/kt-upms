package com.kt.upms.module.role.converter;

import com.kt.upms.entity.UpmsRole;
import com.kt.upms.module.role.vo.RoleListVO;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class RoleBeanConverter {

    public RoleListVO convertToRoleListVO(UpmsRole item) {
        RoleListVO roleListVO = new RoleListVO();
        roleListVO.setId(item.getId());
        roleListVO.setName(item.getName());
        roleListVO.setCreateTime(item.getGmtCreate());
        roleListVO.setUpdateTime(item.getGmtModified());
        return roleListVO;
    }
}
