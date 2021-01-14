package com.kt.upms.module.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsApplication;
import com.kt.upms.entity.UpmsRole;
import com.kt.upms.module.application.dto.ApplicationQueryDTO;
import com.kt.upms.module.application.dto.ApplicationUpdateDTO;
import com.kt.upms.module.application.vo.ApplicationBaseVO;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.dto.RolePermissionUpdateDTO;
import com.kt.upms.module.role.dto.RoleQueryDTO;
import com.kt.upms.module.role.dto.RoleUpdateDTO;
import com.kt.upms.module.role.vo.RoleBaseVO;
import com.kt.upms.module.role.vo.RoleListVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IApplicationService extends IService<UpmsApplication> {

    void updateApplication(ApplicationUpdateDTO dto);

    void saveApplication(ApplicationUpdateDTO dto);

    List<ApplicationBaseVO> listVos(ApplicationQueryDTO dto);
}
