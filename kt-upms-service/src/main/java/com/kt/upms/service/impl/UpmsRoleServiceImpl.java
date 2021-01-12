package com.kt.upms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.role.RoleAddDTO;
import com.kt.model.dto.role.RolePermissionAddDTO;
import com.kt.model.dto.role.RoleQueryDTO;
import com.kt.model.dto.role.RoleUpdateDTO;
import com.kt.model.enums.BizEnums;
import com.kt.model.vo.permission.PermissionVO;
import com.kt.model.vo.role.RoleListVO;
import com.kt.upms.entity.UpmsPermissionRoleRel;
import com.kt.upms.entity.UpmsRole;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.enums.RoleStatusEnums;
import com.kt.upms.mapper.UpmsPermissionRoleRelMapper;
import com.kt.upms.mapper.UpmsRoleMapper;
import com.kt.upms.mapper.UpmsUserGroupRoleRelMapper;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.service.IUpmsRoleService;
import com.kt.upms.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
public class UpmsRoleServiceImpl extends ServiceImpl<UpmsRoleMapper, UpmsRole> implements IUpmsRoleService {

    @Autowired
    private UpmsPermissionRoleRelMapper upmsPermissionRoleRelMapper;
    @Autowired
    private UpmsUserGroupRoleRelMapper upmsUserGroupRoleRelMapper;
    @Autowired
    private IUpmsPermissionService iUpmsPermissionService;

    @Override
    public PageResponse<RoleListVO> pageList(RoleQueryDTO params) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsRole::getName, params.getName());
        Page<UpmsRole> page = this.page(new Page<>(params.getCurrent(), params.getSize()), queryWrapper);

        List<RoleListVO> vos = page.getRecords().stream().map(this::assembleRoleListVO).collect(Collectors.toList());
        return PageResponse.success(page.getCurrent(), page.getSize(), page.getTotal(), vos);
    }

    private RoleListVO assembleRoleListVO(UpmsRole item) {
        RoleListVO roleListVO = new RoleListVO();
        roleListVO.setId(item.getId());
        roleListVO.setName(item.getName());
        roleListVO.setCreateTime(item.getGmtCreate());
        roleListVO.setUpdateTime(item.getGmtModified());
        return roleListVO;
    }

    @Override
    public RoleAddDTO saveRole(RoleAddDTO dto) {
        int count = countRoleByName(dto);
        Assert.isTrue(count > 0, BizEnums.ROLE_ALREADY_EXISTS);

        UpmsRole role = CglibUtil.copy(dto, UpmsRole.class);
        this.save(role);

        return dto;
    }

    private int countRoleByName(RoleAddDTO dto) {
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
        return upmsPermissionRoleRelMapper.selectRoleIdsByUserId(userId);
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
    public void updateRoleRoutePermissions(RolePermissionAddDTO dto) {
        Long roleId = dto.getRoleId();
        String frontRouteType = PermissionTypeEnums.FRONT_ROUTE.getType();
        String pageElementType = PermissionTypeEnums.PAGE_ELEMENT.getType();
        LambdaQueryWrapper<UpmsPermissionRoleRel> qw = new LambdaQueryWrapper<UpmsPermissionRoleRel>()
                .eq(UpmsPermissionRoleRel::getRoleId, roleId)
                .and(wrapper -> wrapper.eq(UpmsPermissionRoleRel::getType, frontRouteType)
                        .or().eq(UpmsPermissionRoleRel::getType, pageElementType));
        upmsPermissionRoleRelMapper.delete(qw);
        if (CollectionUtil.isNotEmpty(dto.getRoutePermissionIds())) {
            upmsPermissionRoleRelMapper.batchInsert(roleId, frontRouteType, dto.getRoutePermissionIds());
        }
        if (CollectionUtil.isNotEmpty(dto.getElementPermissionIds())) {
            upmsPermissionRoleRelMapper.batchInsert(roleId, frontRouteType, dto.getElementPermissionIds());
        }
    }

    @Override
    public List<PermissionVO> getRoleRoutePermissionById(Long id) {
        return iUpmsPermissionService.getPermissionVOSByRoleIdAndType(id, PermissionTypeEnums.FRONT_ROUTE.getType());
    }

    @Override
    public List<PermissionVO> getRoleElementPermissionById(Long id) {
        return iUpmsPermissionService.getPermissionVOSByRoleIdAndType(id, PermissionTypeEnums.PAGE_ELEMENT.getType());
    }

    @Override
    public List<RoleListVO> listAllVos() {
        return this.list().stream().map(this::assembleRoleListVO).collect(Collectors.toList());
    }

    private void updateStatus(RoleUpdateDTO dto, RoleStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsRole>()
                .eq(UpmsRole::getStatus, dto.getId())
                .set(UpmsRole::getStatus, statusEnum.getValue()));
    }
}
