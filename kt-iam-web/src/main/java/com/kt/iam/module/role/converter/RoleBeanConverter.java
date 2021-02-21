package com.kt.iam.module.role.converter;

import com.kt.iam.module.role.persistence.IamRole;
import com.kt.iam.module.role.dto.RoleUpdateDTO;
import com.kt.iam.module.role.vo.RoleListVO;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class RoleBeanConverter {

    public RoleListVO convertToRoleListVO(IamRole item) {
        RoleListVO roleListVO = new RoleListVO();
        roleListVO.setId(item.getId());
        roleListVO.setName(item.getName());
        roleListVO.setCreateTime(item.getGmtCreate());
        roleListVO.setUpdateTime(item.getGmtModified());
        return roleListVO;
    }

    public IamRole convertToDO(RoleUpdateDTO dto) {
        IamRole iamRole = new IamRole();
        iamRole.setId(dto.getId());
        iamRole.setName(dto.getName());
        iamRole.setStatus(dto.getStatus());
        return iamRole;
    }
}
