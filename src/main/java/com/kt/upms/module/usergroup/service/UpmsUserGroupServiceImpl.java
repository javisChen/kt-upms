package com.kt.upms.module.usergroup.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.entity.UpmsUserGroupRoleRel;
import com.kt.upms.entity.UpmsUserGroupUserRel;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.enums.UserGroupStatusEnums;
import com.kt.upms.mapper.UpmsUserGroupMapper;
import com.kt.upms.mapper.UpmsUserGroupRoleRelMapper;
import com.kt.upms.mapper.UpmsUserGroupUserRelMapper;
import com.kt.upms.module.usergroup.converter.UserGroupBeanConverter;
import com.kt.upms.module.usergroup.dto.UserGroupAddDTO;
import com.kt.upms.module.usergroup.dto.UserGroupQueryDTO;
import com.kt.upms.module.usergroup.dto.UserGroupUpdateDTO;
import com.kt.upms.module.usergroup.vo.UserGroupBaseVO;
import com.kt.upms.module.usergroup.vo.UserGroupListTreeVO;
import com.kt.upms.module.usergroup.vo.UserGroupTreeVO;
import com.kt.upms.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UpmsUserGroupServiceImpl extends ServiceImpl<UpmsUserGroupMapper, UpmsUserGroup>
        implements IUpmsUserGroupService {

    private final static Long DEFAULT_PID = 0L;
    private final static Integer FIRST_LEVEL = 1;

    @Autowired
    private UserGroupBeanConverter beanConverter;
    @Autowired
    private UpmsUserGroupRoleRelMapper upmsUserGroupRoleRelMapper;
    @Autowired
    private UpmsUserGroupUserRelMapper upmsUserGroupUserRelMapper;

    @Override
    public Page<UserGroupListTreeVO> pageList(UserGroupQueryDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .like(StrUtil.isNotBlank(dto.getName()), UpmsUserGroup::getName, dto.getName())
                .eq(UpmsUserGroup::getPid, DEFAULT_PID)
                .eq(UpmsUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
                .orderByAsc(UpmsUserGroup::getGmtCreate);
        Page<UpmsUserGroup> pageResult = this.page(new Page<>(dto.getCurrent(), dto.getSize()), queryWrapper);

        List<UpmsUserGroup> levelOneUserGroups = pageResult.getRecords();
        List<UpmsUserGroup> anotherUserGroups = this.list(new LambdaQueryWrapper<UpmsUserGroup>()
                .ne(UpmsUserGroup::getPid, DEFAULT_PID));

        List<UserGroupListTreeVO> vos = recursionUserGroups(levelOneUserGroups, anotherUserGroups);

        Page<UserGroupListTreeVO> pageVo = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        pageVo.setRecords(vos);
        return pageVo;
    }

    private List<UserGroupListTreeVO> recursionUserGroups(List<UpmsUserGroup> levelOneUserGroups, List<UpmsUserGroup> anotherUserGroups) {
        List<UserGroupListTreeVO> vos = CollectionUtil.newArrayList();
        for (UpmsUserGroup item : levelOneUserGroups) {
            UserGroupListTreeVO vo = beanConverter.convertToUserGroupListTreeVO(item);
            findChildren(vo, anotherUserGroups);
            vos.add(vo);
        }
        return vos;
    }

    private void findChildren(UserGroupListTreeVO parent, List<UpmsUserGroup> list) {
        for (UpmsUserGroup route : list) {
            if (parent.getId().equals(route.getPid())) {
                UserGroupListTreeVO item = beanConverter.convertToUserGroupListTreeVO(route);
                parent.getChildren().add(item);
                findChildren(item, list);
            }
        }
    }

    @Override
    public void saveUserGroup(UserGroupAddDTO dto) {
        int count = countUserGroupByName(dto.getName());
        Assert.isTrue(count > 0, BizEnums.USER_GROUP_ALREADY_EXISTS);

        UpmsUserGroup newUserGroup = CglibUtil.copy(dto, UpmsUserGroup.class);

        UpmsUserGroup parentRoute = null;
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
    }

    private void updateLevelPathAfterSave(UpmsUserGroup route, UpmsUserGroup parentRoute) {
        Long routeId = route.getId();
        String levelPath = isFirstLevelUserGroup(route)
                ? routeId + StrUtil.DOT
                : parentRoute.getLevelPath() + routeId + StrUtil.DOT;
        UpmsUserGroup entity = new UpmsUserGroup();
        entity.setId(routeId);
        entity.setLevelPath(levelPath);
        this.updateById(entity);
    }

    public UpmsUserGroup getUserGroupById(Long id) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getId, id);
        return this.getOne(queryWrapper);
    }

    private boolean isFirstLevelUserGroup(UpmsUserGroup userGroup) {
        return DEFAULT_PID.equals(userGroup.getPid()) || FIRST_LEVEL.equals(userGroup.getLevel());
    }

    private int countUserGroupByName(String name) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
                .eq(UpmsUserGroup::getName, name);
        return this.count(queryWrapper);
    }

    @Override
    public void updateUserGroupById(UserGroupUpdateDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
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
    public List<Long> getUserGroupIdsByUserId(Long userId) {
        LambdaQueryWrapper<UpmsUserGroupUserRel> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsUserGroupUserRel::getUserId, userId);
        return upmsUserGroupUserRelMapper.selectList(qw).stream().map(UpmsUserGroupUserRel::getUserGroupId)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserGroupTreeVO> getTree(UserGroupQueryDTO dto) {
        List<UpmsUserGroup> list = Optional.ofNullable(this.list()).orElseGet(ArrayList::new);
        return list.stream().map(assembleUserGroupUserGroupTreeVO()).collect(Collectors.toList());
    }

    @Override
    public List<UserGroupBaseVO> listAllVos() {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .orderByAsc(UpmsUserGroup::getGmtCreate)
                .orderByAsc(UpmsUserGroup::getLevel);
        return list(queryWrapper).stream().map(this::assembleUserGroupVO).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserGroupNamesByUserId(Long userId) {
        return this.baseMapper.selectUserGroupNamesByUserId(userId);
    }

    private Function<UpmsUserGroup, UserGroupTreeVO> assembleUserGroupUserGroupTreeVO() {
        return item -> {
            UserGroupTreeVO userGroupTreeVO = new UserGroupTreeVO();
            userGroupTreeVO.setTitle(item.getName());
            userGroupTreeVO.setKey(String.valueOf(item.getId()));
            return userGroupTreeVO;
        };
    }

    private void updateStatus(UserGroupUpdateDTO dto, UserGroupStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getIsDeleted, DeletedEnums.NOT.getCode())
                .eq(UpmsUserGroup::getStatus, dto.getId())
                .set(UpmsUserGroup::getStatus, statusEnum.getValue()));
    }

    private UserGroupBaseVO assembleUserGroupVO(UpmsUserGroup item) {
        UserGroupBaseVO vo = new UserGroupBaseVO();
        vo.setId(item.getId());
        vo.setPid(item.getPid());
        vo.setName(item.getName());
        vo.setLevel(item.getLevel());
        vo.setStatus(item.getStatus());
        return vo;
    }

}
