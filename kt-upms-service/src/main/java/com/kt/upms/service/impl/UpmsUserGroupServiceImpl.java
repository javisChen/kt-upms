package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.model.dto.usergroup.*;
import com.kt.model.enums.BizEnum;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.entity.UpmsUserGroupUserRel;
import com.kt.upms.enums.UserGroupStatusEnum;
import com.kt.upms.mapper.UpmsUserGroupMapper;
import com.kt.upms.mapper.UpmsUserGroupUserRelMapper;
import com.kt.upms.mapper.UpmsUserMapper;
import com.kt.upms.service.IUpmsUserGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
public class UpmsUserGroupServiceImpl extends ServiceImpl<UpmsUserGroupMapper, UpmsUserGroup> implements IUpmsUserGroupService {

    private final UpmsUserGroupUserRelMapper upmsUserGroupMapper;
    private final UpmsUserMapper upmsUserMapper;

    public UpmsUserGroupServiceImpl(UpmsUserGroupUserRelMapper upmsUserGroupMapper, UpmsUserMapper upmsUserMapper) {
        this.upmsUserGroupMapper = upmsUserGroupMapper;
        this.upmsUserMapper = upmsUserMapper;
    }

    @Override
    public PageResponse pageList(Page page, UserGroupQueryDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .like(StrUtil.isNotBlank(dto.getName()), UpmsUserGroup::getName, dto.getName());
        return PageResponse.success(this.page(page, queryWrapper));
    }

    @Override
    public UserGroupAddDTO saveUserGroup(UserGroupAddDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getName, dto.getName());
        UpmsUserGroup upmsUserGroup = this.getOne(queryWrapper);
        if (upmsUserGroup != null) {
            throw new BizException(BizEnum.USER_GROUP_ALREADY_EXISTS.getCode(),
                    BizEnum.USER_GROUP_ALREADY_EXISTS.getMsg());
        }
        UpmsUserGroup newUserGroup = CglibUtil.copy(dto, UpmsUserGroup.class);
        this.save(newUserGroup);
        return dto;
    }

    @Override
    public void updateUserGroupById(UserGroupUpdateDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getName, dto.getName())
                .ne(UpmsUserGroup::getId, dto.getId());
        UpmsUserGroup upmsUserGroup = this.getOne(queryWrapper);
        if (upmsUserGroup != null) {
            throw new BizException(BizEnum.USER_GROUP_ALREADY_EXISTS.getCode(),
                    BizEnum.USER_GROUP_ALREADY_EXISTS.getMsg());
        }
        UpmsUserGroup update = CglibUtil.copy(dto, UpmsUserGroup.class);
        this.updateById(update);
    }


    @Override
    public void updateStatus(UserGroupUpdateDTO dto) {
        updateStatus(dto, UserGroupStatusEnum.DISABLED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrRemoveUserInUserGroup(UserGroupUserAddDTO dto) {

        doAddUserToUserGroup(dto.getAdd());

        doRemoveUserFromUserGroup(dto.getRemove());
    }

    private void doRemoveUserFromUserGroup(UserGroupUserAddDTO.Remove dto) {
        if (dto != null) {
            upmsUserGroupMapper.deleteByUserIdsAndUserGroupId(dto.getUserGroupId(), dto.getUserIds());
        }
    }

    private void doAddUserToUserGroup(UserGroupUserAddDTO.Add dto) {
        if (dto != null) {
            List<Long> userIds = dto.getUserIds();
            Long userGroupId = dto.getUserGroupId();
            LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                    .eq(UpmsUserGroup::getId, userGroupId);
            UpmsUserGroup upmsUserGroup = this.getOne(queryWrapper);
            if (upmsUserGroup == null) {
                throw new BizException(BizEnum.USER_GROUP_NOT_EXISTS.getCode(),
                        BizEnum.USER_GROUP_NOT_EXISTS.getMsg());
            }
            List<UpmsUserGroupUserRel> upmsUserGroupUserRels = userIds.stream()
                    .map(item -> {
                        UpmsUserGroupUserRel upmsUserGroupUserRel = new UpmsUserGroupUserRel();
                        upmsUserGroupUserRel.setUserGroupId(userGroupId);
                        upmsUserGroupUserRel.setUserId(item);
                        upmsUserGroupUserRel.setGmtCreate(LocalDateTime.now());
                        upmsUserGroupUserRel.setGmtModified(LocalDateTime.now());
                        upmsUserGroupUserRel.setCreator(-1L);
                        upmsUserGroupUserRel.setModifier(-1L);
                        return upmsUserGroupUserRel;
                    })
                    .collect(Collectors.toList());
            upmsUserGroupMapper.insertBatch(upmsUserGroupUserRels);
        }
    }

    @Override
    public PageResponse getUsersUnderUserGroupPageList(Page page, UserGroupUserQueryDTO dto) {
        return PageResponse.success(upmsUserMapper.selectByUserGroupId(page, dto.getId()));
    }

    private void updateStatus(UserGroupUpdateDTO dto, UserGroupStatusEnum roleStatusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getStatus, dto.getId())
                .set(UpmsUserGroup::getStatus, roleStatusEnum.getValue()));
    }
}
