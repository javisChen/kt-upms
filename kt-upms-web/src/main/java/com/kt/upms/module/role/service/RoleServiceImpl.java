package com.kt.upms.module.role.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.common.util.Assert;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.enums.RoleStatusEnums;
import com.kt.upms.module.permission.persistence.UpmsPermissionRoleRel;
import com.kt.upms.module.permission.persistence.dao.UpmsPermissionRoleRelMapper;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.converter.RoleBeanConverter;
import com.kt.upms.module.role.dto.*;
import com.kt.upms.module.role.persistence.UpmsRole;
import com.kt.upms.module.user.persistence.UpmsUserRoleRel;
import com.kt.upms.module.role.persistence.dao.UpmsRoleMapper;
import com.kt.upms.module.role.vo.RoleBaseVO;
import com.kt.upms.module.role.vo.RoleListVO;
import com.kt.upms.module.user.persistence.dao.UpmsUserRoleRelMapper;
import com.kt.upms.module.usergroup.persistence.UpmsUserGroupRoleRel;
import com.kt.upms.module.usergroup.persistence.dao.UpmsUserGroupRoleRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<UpmsRoleMapper, UpmsRole> implements IRoleService {

    @Autowired
    private UpmsPermissionRoleRelMapper upmsPermissionRoleRelMapper;
    @Autowired
    private UpmsUserGroupRoleRelMapper upmsUserGroupRoleRelMapper;
    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private RoleBeanConverter beanConverter;
    @Autowired
    private UpmsUserRoleRelMapper upmsUserRoleRelMapper;

    @Override
    public Page<RoleListVO> pageList(RoleQueryDTO params) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsRole::getName, params.getName());
        Page<UpmsRole> page = this.page(new Page<>(params.getCurrent(), params.getSize()), queryWrapper);

        List<RoleListVO> vos = page.getRecords().stream().map(beanConverter::convertToRoleListVO)
                .collect(Collectors.toList());
        Page<RoleListVO> pageVo = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        pageVo.setRecords(vos);
        return pageVo;
    }

    @Override
    public void saveRole(RoleUpdateDTO dto) {
        int count = countRoleByName(dto);
        Assert.isTrue(count > 0, BizEnums.ROLE_ALREADY_EXISTS);

        UpmsRole role = beanConverter.convertToDO(dto);
        this.save(role);
    }

    private int countRoleByName(RoleUpdateDTO dto) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .eq(UpmsRole::getName, dto.getName());
        return this.count(queryWrapper);
    }

    @Override
    public void updateRoleById(RoleUpdateDTO dto) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .eq(UpmsRole::getName, dto.getName())
                .ne(UpmsRole::getId, dto.getId());
        int count = this.count(queryWrapper);
        Assert.isTrue(count > 0, BizEnums.ROLE_ALREADY_EXISTS);

        UpmsRole role = beanConverter.convertToDO(dto);
        this.updateById(role);
    }

    @Override
    public void updateStatus(RoleUpdateDTO dto) {
        updateStatus(dto, RoleStatusEnums.DISABLED);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<UpmsUserRoleRel> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsUserRoleRel::getUserId, userId);
        return upmsUserRoleRelMapper.selectList(qw).stream().map(UpmsUserRoleRel::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getRoleIdsByUserGroupIds(List<Long> userGroupIds) {
        if (CollectionUtils.isEmpty(userGroupIds)) {
            return CollectionUtil.newArrayList();
        }
        return upmsUserGroupRoleRelMapper.selectRoleIdsByUserGroupIds(userGroupIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void updateRoleRoutePermissions(RoleRoutePermissionUpdateDTO dto) {
        Long roleId = dto.getRoleId();
        List<Long> toRemoveIds = CollectionUtil.newArrayList(dto.getToRemoveRoutePermissionIds());
        toRemoveIds.addAll(dto.getToRemoveElementPermissionIds());
        iPermissionService.removeRolePermission(roleId, toRemoveIds);

        List<Long> toAddIds = CollectionUtil.newArrayList(dto.getToAddRoutePermissionIds());
        toAddIds.addAll(dto.getToAddElementPermissionIds());
        iPermissionService.batchSaveRolePermissionRel(roleId, toAddIds);
    }

    @Override
    public List<PermissionVO> getRoleRoutePermissionById(Long roleId, Long applicationId) {
        return iPermissionService.getRolePermissionVos(applicationId, roleId, PermissionTypeEnums.FRONT_ROUTE.getType());
    }

    @Override
    public List<PermissionVO> getRoleElementPermissionById(Long roleId, Long applicationId) {
        return iPermissionService.getRolePermissionVos(applicationId, roleId, PermissionTypeEnums.PAGE_ELEMENT.getType());
    }

    @Override
    public List<PermissionVO> getRoleApiPermissionById(Long roleId, Long applicationId) {
        return iPermissionService.getRolePermissionVos(applicationId, roleId, PermissionTypeEnums.SER_API.getType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleApiPermissions(RoleApiPermissionUpdateDTO dto) {
        Long roleId = dto.getRoleId();
        iPermissionService.removeRolePermission(roleId, dto.getToRemoveApiPermissionIds());
        iPermissionService.batchSaveRolePermissionRel(roleId, dto.getToAddApiPermissionIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleById(Long id) {
        LambdaUpdateWrapper<UpmsRole> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsRole::getId, id);
        qw.eq(UpmsRole::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.set(UpmsRole::getIsDeleted, id);
        this.update(qw);

        // 移除用户与角色关联关系
        removeUserRoleRelByRoleId(id);
        // 移除用户组与角色关联关系
        removeUserGroupRoleRelByRoleId(id);
        // 角色与权限的关系
        removePermissionRoleRelByRoleId(id);
    }

    @Override
    public List<String> getRoleNamesByUserGroupId(Long userGroupId) {
        return upmsPermissionRoleRelMapper.selectRoleNamesByUserGroupId(userGroupId);
    }

    @Override
    public List<Long> getRoleIdsByUserGroupId(Long userGroupId) {
        return upmsPermissionRoleRelMapper.selectRoleIdsByUserGroupId(userGroupId);
    }

    @Override
    public void updateRoleApiPermissions(RoleApplicationApiPermissionUpdateDTO dto) {

    }

    private void removePermissionRoleRelByRoleId(Long roleId) {
        LambdaUpdateWrapper<UpmsPermissionRoleRel> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsPermissionRoleRel::getRoleId, roleId);
        upmsPermissionRoleRelMapper.delete(qw);
    }

    private void removeUserGroupRoleRelByRoleId(Long roleId) {
        LambdaUpdateWrapper<UpmsUserGroupRoleRel> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsUserGroupRoleRel::getRoleId, roleId);
        upmsUserGroupRoleRelMapper.delete(qw);
    }

    private void removeUserRoleRelByRoleId(Long roleId) {
        LambdaUpdateWrapper<UpmsUserRoleRel> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsUserRoleRel::getRoleId, roleId);
        upmsUserRoleRelMapper.delete(qw);
    }

    @Override
    public void removeUserRoleRelByUserId(Long userId) {
        LambdaUpdateWrapper<UpmsUserRoleRel> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsUserRoleRel::getUserId, userId);
        upmsUserRoleRelMapper.delete(qw);
    }

    @Override
    public List<RoleListVO> listAllVos() {
        return this.list().stream().map(beanConverter::convertToRoleListVO).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleNamesByUserId(Long userId) {
        return upmsPermissionRoleRelMapper.selectRoleNamesByUserId(userId);
    }

    @Override
    public RoleBaseVO getRoleVoById(String id) {
        UpmsRole upmsRole = getRoleById(id);
        return CglibUtil.copy(upmsRole, RoleBaseVO.class);
    }

    private UpmsRole getRoleById(String id) {
        return Optional.ofNullable(this.getById(id)).orElseGet(UpmsRole::new);
    }

    private void updateStatus(RoleUpdateDTO dto, RoleStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsRole>()
                .eq(UpmsRole::getStatus, dto.getId())
                .set(UpmsRole::getStatus, statusEnum.getValue()));
    }
}
