package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.usergroup.*;
import com.kt.model.enums.BizEnums;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.entity.UpmsUserGroupRoleRel;
import com.kt.upms.entity.UpmsUserGroupUserRel;
import com.kt.upms.enums.UserGroupStatusEnums;
import com.kt.upms.mapper.*;
import com.kt.upms.service.IUpmsUserGroupService;
import com.kt.upms.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户组表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
public class UpmsUserGroupServiceImpl extends ServiceImpl<UpmsUserGroupMapper, UpmsUserGroup>
        implements IUpmsUserGroupService {

    private final UpmsUserGroupUserRelMapper userGroupUserRelMapper;
    private final UpmsUserGroupRoleRelMapper upmsUserGroupRoleRelMapper;
    private final UpmsUserMapper upmsUserMapper;
    private final UpmsRoleMapper upmsRoleMapper;

    public UpmsUserGroupServiceImpl(UpmsUserGroupUserRelMapper userGroupUserRelMapper, UpmsUserGroupRoleRelMapper
            upmsUserGroupRoleRelMapper, UpmsUserMapper upmsUserMapper, UpmsRoleMapper upmsRoleMapper) {
        this.userGroupUserRelMapper = userGroupUserRelMapper;
        this.upmsUserGroupRoleRelMapper = upmsUserGroupRoleRelMapper;
        this.upmsUserMapper = upmsUserMapper;
        this.upmsRoleMapper = upmsRoleMapper;
    }


    @Override
    public PageResponse pageList(Page page, UserGroupQueryDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .like(StrUtil.isNotBlank(dto.getName()), UpmsUserGroup::getName, dto.getName());
        return PageResponse.success(this.page(page, queryWrapper));
    }

    @Override
    public UserGroupAddDTO saveUserGroup(UserGroupAddDTO dto) {
        int count = countUserGroupByName(dto.getName());
        Assert.isTrue(count > 0, BizEnums.USER_GROUP_ALREADY_EXISTS);

        UpmsUserGroup newUserGroup = CglibUtil.copy(dto, UpmsUserGroup.class);
        this.save(newUserGroup);
        return dto;
    }

    private int countUserGroupByName(String name) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getName, name);
        return this.count(queryWrapper);
    }

    @Override
    public void updateUserGroupById(UserGroupUpdateDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getName, dto.getName())
                .ne(UpmsUserGroup::getId, dto.getId());
        int count = this.count(queryWrapper);

        Assert.isTrue(count > 0, BizEnums.USER_GROUP_ALREADY_EXISTS);

        UpmsUserGroup update = CglibUtil.copy(dto, UpmsUserGroup.class);
        this.updateById(update);
    }


    @Override
    public void updateStatus(UserGroupUpdateDTO dto) {
        updateStatus(dto, UserGroupStatusEnums.DISABLED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrRemoveUserInUserGroup(UserGroupUserAddDTO dto) {

        doAddUserToUserGroup(dto.getAdd());

        doRemoveUserFromUserGroup(dto.getRemove());
    }

    private void doRemoveUserFromUserGroup(UserGroupUserAddDTO.Remove dto) {
        if (dto != null) {
            userGroupUserRelMapper.deleteByUserIdsAndUserGroupId(dto.getUserGroupId(), dto.getUserIds());
        }
    }

    private void doAddUserToUserGroup(UserGroupUserAddDTO.Add dto) {
        if (dto != null) {
            List<Long> userIds = dto.getUserIds();
            Long userGroupId = dto.getUserGroupId();
            Assert.isTrue(countUserGroupById(userGroupId) == 0, BizEnums.USER_GROUP_NOT_EXISTS);

            List<UpmsUserGroupUserRel> rels = userIds.stream()
                    .map(item -> assembleUserGroupUserRel(userGroupId, item))
                    .collect(Collectors.toList());
            userGroupUserRelMapper.insertBatch(rels);
        }
    }

    private UpmsUserGroupUserRel assembleUserGroupUserRel(Long userGroupId, Long item) {
        UpmsUserGroupUserRel upmsUserGroupUserRel = new UpmsUserGroupUserRel();
        upmsUserGroupUserRel.setUserGroupId(userGroupId);
        upmsUserGroupUserRel.setUserId(item);
        return upmsUserGroupUserRel;
    }

    @Override
    public PageResponse getUsersUnderUserGroupPageList(Page page, UserGroupUserQueryDTO dto) {
        return PageResponse.success(upmsUserMapper.selectByUserGroupId(page, dto.getId()));
    }

    @Override
    public void addOrRemoveRoleInUserGroup(UserGroupRoleAddDTO dto) {

        doAddRoleToUserGroup(dto.getAdd());

        doRemoveRoleFromUserGroup(dto.getRemove());
    }

    private void doAddRoleToUserGroup(UserGroupRoleAddDTO.Add dto) {
        if (dto != null) {
            upmsUserGroupRoleRelMapper.deleteByRoleIdsAndUserGroupId(dto.getUserGroupId(), dto.getRoleIds());
        }
    }

    private void doRemoveRoleFromUserGroup(UserGroupRoleAddDTO.Remove dto) {
        if (dto != null) {
            List<Long> userIds = dto.getRoleIds();
            Long userGroupId = dto.getUserGroupId();

            Assert.isTrue(countUserGroupById(userGroupId) == 0, BizEnums.USER_GROUP_NOT_EXISTS);

            List<UpmsUserGroupRoleRel> rels = userIds.stream()
                    .map(item -> assembleUserGroupRoleRel(userGroupId, item))
                    .collect(Collectors.toList());
            upmsUserGroupRoleRelMapper.insertBatch(rels);
        }
    }

    private int countUserGroupById(Long userGroupId) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getId, userGroupId);
        return this.count(queryWrapper);
    }

    private UpmsUserGroupRoleRel assembleUserGroupRoleRel(Long userGroupId, Long item) {
        UpmsUserGroupRoleRel entity = new UpmsUserGroupRoleRel();
        entity.setUserGroupId(userGroupId);
        entity.setRoleId(item);
        return entity;
    }

    @Override
    public PageResponse getRolesUnderUserGroupPageList(Page page, UserGroupRoleQueryDTO dto) {
        return PageResponse.success(upmsRoleMapper.selectByUserGroupId(page, dto.getId()));
    }

    @Override
    public List<Long> getUserGroupIdsByUserId(Long userId) {
        return this.baseMapper.selectUserGroupIdsByUserId(userId);
    }

    private void updateStatus(UserGroupUpdateDTO dto, UserGroupStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getStatus, dto.getId())
                .set(UpmsUserGroup::getStatus, statusEnum.getValue()));
    }
}
