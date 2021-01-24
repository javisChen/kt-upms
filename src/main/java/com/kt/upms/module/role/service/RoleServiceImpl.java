package com.kt.upms.module.role.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.entity.UpmsRole;
import com.kt.upms.entity.UpmsUserRoleRel;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.enums.RoleStatusEnums;
import com.kt.upms.mapper.UpmsPermissionRoleRelMapper;
import com.kt.upms.mapper.UpmsRoleMapper;
import com.kt.upms.mapper.UpmsUserGroupRoleRelMapper;
import com.kt.upms.mapper.UpmsUserRoleRelMapper;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.converter.RoleBeanConverter;
import com.kt.upms.module.role.dto.RoleApiPermissionUpdateDTO;
import com.kt.upms.module.role.dto.RoleQueryDTO;
import com.kt.upms.module.role.dto.RoleRoutePermissionUpdateDTO;
import com.kt.upms.module.role.dto.RoleUpdateDTO;
import com.kt.upms.module.role.vo.RoleBaseVO;
import com.kt.upms.module.role.vo.RoleListVO;
import com.kt.upms.util.Assert;
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

        List<RoleListVO> vos = page.getRecords().stream().map(beanConverter::convertToRoleListVO).collect(Collectors.toList());
        Page<RoleListVO> pageVo = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        pageVo.setRecords(vos);
        return pageVo;
    }

    @Override
    public void saveRole(RoleUpdateDTO dto) {
        int count = countRoleByName(dto);
        Assert.isTrue(count > 0, BizEnums.ROLE_ALREADY_EXISTS);

        UpmsRole role = CglibUtil.copy(dto, UpmsRole.class);
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

        UpmsRole role = CglibUtil.copy(dto, UpmsRole.class);
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
