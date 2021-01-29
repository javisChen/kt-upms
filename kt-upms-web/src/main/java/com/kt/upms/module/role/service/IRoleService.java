package com.kt.upms.module.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.module.role.persistence.UpmsRole;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.dto.RoleApiPermissionUpdateDTO;
import com.kt.upms.module.role.dto.RoleRoutePermissionUpdateDTO;
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
public interface IRoleService extends IService<UpmsRole> {

    Page<RoleListVO> pageList(RoleQueryDTO dto);

    void saveRole(RoleUpdateDTO dto);

    void updateRoleById(RoleUpdateDTO upmsRole);

    void updateStatus(RoleUpdateDTO dto);

    /**
     * 根据用户id查询下面的所有角色id
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 根据用户组id查询下面的所有角色id
     */
    List<Long> getRoleIdsByUserGroupIds(List<Long> userGroupIds);

    void updateRoleRoutePermissions(RoleRoutePermissionUpdateDTO dto);

    List<PermissionVO> getRoleRoutePermissionById(Long roleId, Long applicationId);

    List<PermissionVO> getRoleElementPermissionById(Long roleId, Long applicationId);

    void removeUserRoleRelByUserId(Long userId);

    List<RoleListVO> listAllVos();

    List<String> getRoleNamesByUserId(Long userId);

    RoleBaseVO getRoleVoById(String id);

    List<PermissionVO> getRoleApiPermissionById(Long roleId, Long applicationId);

    void updateRoleApiPermissions(RoleApiPermissionUpdateDTO dto);

    void removeRoleById(Long id);
}
