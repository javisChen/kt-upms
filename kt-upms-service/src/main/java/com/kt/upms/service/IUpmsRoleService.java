package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.role.RoleAddDTO;
import com.kt.model.dto.role.RolePermissionAddDTO;
import com.kt.model.dto.role.RoleQueryDTO;
import com.kt.model.dto.role.RoleUpdateDTO;
import com.kt.model.vo.permission.PermissionVO;
import com.kt.model.vo.role.RoleListVO;
import com.kt.upms.entity.UpmsRole;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsRoleService extends IService<UpmsRole> {

    PageResponse<RoleListVO> pageList(RoleQueryDTO dto);

    RoleAddDTO saveRole(RoleAddDTO userGroupAddDTO);

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

    void updateRoleRoutePermissions(RolePermissionAddDTO dto);

    List<PermissionVO> getRoleRoutePermissionById(Long id);

    List<PermissionVO> getRoleElementPermissionById(Long id);

    List<RoleListVO> listAllVos();

    List<String> getRoleNamesByUserId(Long userId);
}
