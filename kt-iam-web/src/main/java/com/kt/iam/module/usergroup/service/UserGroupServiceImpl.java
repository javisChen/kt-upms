package com.kt.iam.module.usergroup.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.iam.common.util.Assert;
import com.kt.iam.enums.BizEnums;
import com.kt.iam.enums.DeletedEnums;
import com.kt.iam.module.usergroup.converter.UserGroupBeanConverter;
import com.kt.iam.module.usergroup.dto.UserGroupQueryDTO;
import com.kt.iam.module.usergroup.dto.UserGroupUpdateDTO;
import com.kt.iam.module.usergroup.persistence.IamUserGroup;
import com.kt.iam.module.usergroup.persistence.IamUserGroupRoleRel;
import com.kt.iam.module.usergroup.persistence.IamUserGroupUserRel;
import com.kt.iam.module.usergroup.persistence.dao.IamUserGroupMapper;
import com.kt.iam.module.usergroup.persistence.dao.IamUserGroupRoleRelMapper;
import com.kt.iam.module.usergroup.persistence.dao.IamUserGroupUserRelMapper;
import com.kt.iam.module.usergroup.vo.UserGroupBaseVO;
import com.kt.iam.module.usergroup.vo.UserGroupDetailVO;
import com.kt.iam.module.usergroup.vo.UserGroupListTreeVO;
import com.kt.iam.module.usergroup.vo.UserGroupTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
public class UserGroupServiceImpl extends ServiceImpl<IamUserGroupMapper, IamUserGroup>
        implements IUserGroupService {

    private final static Long DEFAULT_PID = 0L;
    private final static Integer FIRST_LEVEL = 1;

    @Autowired
    private UserGroupBeanConverter beanConverter;
    @Autowired
    private IamUserGroupUserRelMapper iamUserGroupUserRelMapper;
    @Autowired
    private IamUserGroupRoleRelMapper iamUserGroupRoleRelMapper;

    @Override
    public Page<UserGroupListTreeVO> pageList(UserGroupQueryDTO dto) {
        LambdaQueryWrapper<IamUserGroup> queryWrapper = new LambdaQueryWrapper<IamUserGroup>()
                .like(StrUtil.isNotBlank(dto.getName()), IamUserGroup::getName, dto.getName())
                .eq(IamUserGroup::getPid, DEFAULT_PID)
                .eq(IamUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
                .orderByAsc(IamUserGroup::getGmtCreate);
        Page<IamUserGroup> pageResult = this.page(new Page<>(dto.getCurrent(), dto.getSize()), queryWrapper);

        List<IamUserGroup> levelOneUserGroups = pageResult.getRecords();
        List<IamUserGroup> anotherUserGroups = this.list(new LambdaQueryWrapper<IamUserGroup>()
                .ne(IamUserGroup::getPid, DEFAULT_PID));

        List<UserGroupListTreeVO> vos = recursionUserGroups(levelOneUserGroups, anotherUserGroups);

        Page<UserGroupListTreeVO> pageVo = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        pageVo.setRecords(vos);
        return pageVo;
    }

    private List<UserGroupListTreeVO> recursionUserGroups(List<IamUserGroup> levelOneUserGroups, List<IamUserGroup> anotherUserGroups) {
        List<UserGroupListTreeVO> vos = CollectionUtil.newArrayList();
        for (IamUserGroup item : levelOneUserGroups) {
            UserGroupListTreeVO vo = beanConverter.convertToUserGroupListTreeVO(item);
            findChildren(vo, anotherUserGroups);
            vos.add(vo);
        }
        return vos;
    }

    private void findChildren(UserGroupListTreeVO parent, List<IamUserGroup> list) {
        for (IamUserGroup route : list) {
            if (parent.getId().equals(route.getPid())) {
                UserGroupListTreeVO item = beanConverter.convertToUserGroupListTreeVO(route);
                parent.getChildren().add(item);
                findChildren(item, list);
            }
        }
    }

    @Override
    @Transactional
    public void saveUserGroup(UserGroupUpdateDTO dto) {
        int count = getUserGroupByName(dto.getName());
        Assert.isTrue(count > 0, BizEnums.USER_GROUP_ALREADY_EXISTS);

        IamUserGroup newUserGroup = beanConverter.convertToDO(dto);

        IamUserGroup parentRoute = null;
        if (this.isFirstLevelUserGroup(newUserGroup)) {
            newUserGroup.setLevel(FIRST_LEVEL);
        } else {
            parentRoute = getUserGroupById(newUserGroup.getPid());
            Assert.isTrue(parentRoute == null, BizEnums.PARENT_ROUTE_NOT_EXISTS);
            newUserGroup.setLevel(parentRoute.getLevel() + 1);
        }
        this.save(newUserGroup);

        // 新增完路由记录后再更新层级信息
        updateLevelPathAfterSave(newUserGroup, parentRoute);

        updateUserGroupRoleRel(newUserGroup.getId(), dto.getRoleIds());
    }

    private void updateUserGroupRoleRel(Long userGroupId, List<Long> roleIds) {
        if (CollectionUtil.isNotEmpty(roleIds)) {
            iamUserGroupRoleRelMapper.insertBatch(userGroupId, roleIds);
        }
    }

    private void updateLevelPathAfterSave(IamUserGroup route, IamUserGroup parentRoute) {
        Long routeId = route.getId();
        String levelPath = isFirstLevelUserGroup(route)
                ? routeId + StrUtil.DOT
                : parentRoute.getLevelPath() + routeId + StrUtil.DOT;
        IamUserGroup entity = new IamUserGroup();
        entity.setId(routeId);
        entity.setLevelPath(levelPath);
        this.updateById(entity);
    }

    public IamUserGroup getUserGroupById(Long id) {
        LambdaQueryWrapper<IamUserGroup> queryWrapper = new LambdaQueryWrapper<IamUserGroup>()
                .eq(IamUserGroup::getId, id);
        return this.getOne(queryWrapper);
    }

    private boolean isFirstLevelUserGroup(IamUserGroup userGroup) {
        return DEFAULT_PID.equals(userGroup.getPid()) || FIRST_LEVEL.equals(userGroup.getLevel());
    }

    private int getUserGroupByName(String name) {
        LambdaQueryWrapper<IamUserGroup> queryWrapper = new LambdaQueryWrapper<IamUserGroup>()
                .eq(IamUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
                .eq(IamUserGroup::getName, name);
        return this.count(queryWrapper);
    }

    @Override
    @Transactional
    public void updateUserGroupById(UserGroupUpdateDTO dto) {
        IamUserGroup userGroup = getUserGroupByName(dto);

        Assert.isTrue(userGroup != null && !userGroup.getId().equals(dto.getId()),
                BizEnums.USER_GROUP_ALREADY_EXISTS);

        IamUserGroup update = beanConverter.convertToDO(dto);
        this.updateById(update);

        Long userGroupId = update.getId();
        removeUserGroupRoleRelByUserGroupId(userGroupId);

        updateUserGroupRoleRel(userGroupId, dto.getRoleIds());
    }

    private IamUserGroup getUserGroupByName(UserGroupUpdateDTO dto) {
        LambdaQueryWrapper<IamUserGroup> queryWrapper = new LambdaQueryWrapper<IamUserGroup>()
                .eq(IamUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
                .eq(IamUserGroup::getName, dto.getName());
        return this.getOne(queryWrapper);
    }

    private void removeUserGroupRoleRelByUserGroupId(Long id) {
        LambdaUpdateWrapper<IamUserGroupRoleRel> qw = new LambdaUpdateWrapper<>();
        qw.eq(IamUserGroupRoleRel::getUserGroupId, id);
        iamUserGroupRoleRelMapper.delete(qw);
    }

    @Override
    public List<Long> getUserGroupIdsByUserId(Long userId) {
        LambdaQueryWrapper<IamUserGroupUserRel> qw = new LambdaQueryWrapper<>();
        qw.eq(IamUserGroupUserRel::getUserId, userId);
        return iamUserGroupUserRelMapper.selectList(qw).stream().map(IamUserGroupUserRel::getUserGroupId)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserGroupTreeVO> getTree(UserGroupQueryDTO dto) {
        List<IamUserGroup> list = Optional.ofNullable(this.list()).orElseGet(ArrayList::new);
        return list.stream().map(assembleUserGroupUserGroupTreeVO()).collect(Collectors.toList());
    }

    @Override
    public List<UserGroupBaseVO> listAllVos() {
        LambdaQueryWrapper<IamUserGroup> queryWrapper = new LambdaQueryWrapper<IamUserGroup>()
                .orderByAsc(IamUserGroup::getGmtCreate)
                .orderByAsc(IamUserGroup::getLevel);
        return list(queryWrapper).stream().map(this::assembleUserGroupVO).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserGroupNamesByUserId(Long userId) {
        return this.baseMapper.selectUserGroupNamesByUserId(userId);
    }

    @Override
    public void removeUserGroupUserRelByUserId(Long userId) {
        LambdaQueryWrapper<IamUserGroupUserRel> qw = new LambdaQueryWrapper<>();
        qw.eq(IamUserGroupUserRel::getUserId, userId);
        iamUserGroupUserRelMapper.delete(qw);
    }

    @Override
    public UserGroupDetailVO getUserGroupVOById(Long id) {
        IamUserGroup userGroup = getUserGroupById(id);
        return beanConverter.convertToUserGroupDetailVO(userGroup);
    }

    private Function<IamUserGroup, UserGroupTreeVO> assembleUserGroupUserGroupTreeVO() {
        return item -> {
            UserGroupTreeVO userGroupTreeVO = new UserGroupTreeVO();
            userGroupTreeVO.setTitle(item.getName());
            userGroupTreeVO.setKey(String.valueOf(item.getId()));
            return userGroupTreeVO;
        };
    }

    private UserGroupBaseVO assembleUserGroupVO(IamUserGroup item) {
        UserGroupBaseVO vo = new UserGroupBaseVO();
        vo.setId(item.getId());
        vo.setPid(item.getPid());
        vo.setName(item.getName());
        vo.setLevel(item.getLevel());
        vo.setStatus(item.getStatus());
        return vo;
    }

}
