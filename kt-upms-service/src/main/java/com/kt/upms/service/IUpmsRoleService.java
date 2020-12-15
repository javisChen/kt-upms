package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.role.RoleAddDTO;
import com.kt.model.dto.role.RoleQueryDTO;
import com.kt.model.dto.role.RoleUpdateDTO;
import com.kt.upms.entity.UpmsRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsRoleService extends IService<UpmsRole> {

    PageResponse pageList(Page page, RoleQueryDTO params);

    RoleAddDTO saveRole(RoleAddDTO userGroupAddDTO);

    void updateRoleById(RoleUpdateDTO upmsRole);

    void updateStatus(RoleUpdateDTO dto);

}
