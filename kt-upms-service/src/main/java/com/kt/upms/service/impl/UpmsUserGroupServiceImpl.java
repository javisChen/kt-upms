package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.model.dto.usergroup.UserGroupAddDTO;
import com.kt.model.dto.usergroup.UserGroupQueryDTO;
import com.kt.model.dto.usergroup.UserGroupUpdateDTO;
import com.kt.model.enums.BizEnum;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.mapper.UpmsUserGroupMapper;
import com.kt.upms.service.IUpmsUserGroupService;
import org.springframework.stereotype.Service;

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
            throw new BizException(BizEnum.USER_GROUP_NAME_ALREADY_EXISTS.getCode(),
                    BizEnum.USER_GROUP_NAME_ALREADY_EXISTS.getMsg());
        }
        UpmsUserGroup newUserGroup = CglibUtil.copy(dto, UpmsUserGroup.class);
        this.save(newUserGroup);
        return dto;
    }

    @Override
    public void updateUserGroupById(UserGroupUpdateDTO dto) {
        LambdaQueryWrapper<UpmsUserGroup> queryWrapper = new LambdaQueryWrapper<UpmsUserGroup>()
                .eq(UpmsUserGroup::getName, dto.getName())
                .eq(UpmsUserGroup::getId, dto.getId());
        UpmsUserGroup upmsUserGroup = this.getOne(queryWrapper);
        if (upmsUserGroup != null) {
            throw new BizException(BizEnum.USER_GROUP_NAME_ALREADY_EXISTS.getCode(),
                    BizEnum.USER_GROUP_NAME_ALREADY_EXISTS.getMsg());
        }
        UpmsUserGroup update = CglibUtil.copy(dto, UpmsUserGroup.class);
        this.updateById(update);
    }
}
