package com.kt.upms.module.permission.converter;

import com.kt.upms.module.permission.persistence.UpmsPermission;
import com.kt.upms.module.permission.vo.PermissionVO;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class PermissionBeanConverter {

    public PermissionVO convertToVO(UpmsPermission upmsPermission) {
        PermissionVO vo = new PermissionVO();
        vo.setPermissionId(upmsPermission.getId());
        return vo;
    }
}
