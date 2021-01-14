package com.kt.upms.module.application.converter;

import com.kt.upms.entity.UpmsApplication;
import com.kt.upms.entity.UpmsRole;
import com.kt.upms.module.application.dto.ApplicationUpdateDTO;
import com.kt.upms.module.application.vo.ApplicationBaseVO;
import com.kt.upms.module.role.vo.RoleListVO;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class ApplicationBeanConverter {

    public ApplicationBaseVO convertForApplicationBaseVO(UpmsApplication upmsApplication) {
        ApplicationBaseVO vo = new ApplicationBaseVO();
        vo.setName(upmsApplication.getName());
        vo.setCode(upmsApplication.getCode());
        vo.setStatus(upmsApplication.getStatus());
        vo.setType(upmsApplication.getType());
        return vo;
    }

    public UpmsApplication convertForInsert(ApplicationUpdateDTO dto) {
        return convertForEntity(dto);
    }

    public UpmsApplication convertForUpdate(ApplicationUpdateDTO dto) {
        return convertForEntity(dto);
    }

    private UpmsApplication convertForEntity(ApplicationUpdateDTO dto) {
        UpmsApplication application = new UpmsApplication();
        application.setId(dto.getId());
        application.setName(dto.getName());
        application.setCode(dto.getCode());
        application.setStatus(dto.getStatus());
        application.setType(dto.getType());
        return application;
    }
}
