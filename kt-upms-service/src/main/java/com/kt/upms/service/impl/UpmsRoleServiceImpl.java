package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.model.dto.role.RoleAddDTO;
import com.kt.model.dto.role.RoleQueryDTO;
import com.kt.model.dto.role.RoleUpdateDTO;
import com.kt.model.enums.BizEnum;
import com.kt.upms.entity.UpmsRole;
import com.kt.upms.enums.RoleStatusEnum;
import com.kt.upms.mapper.UpmsRoleMapper;
import com.kt.upms.service.IUpmsRoleService;
import org.springframework.stereotype.Service;

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

    @Override
    public PageResponse pageList(Page page, RoleQueryDTO params) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsRole::getName, params.getName());
        return PageResponse.success(this.page(page, queryWrapper));
    }

    @Override
    public RoleAddDTO saveRole(RoleAddDTO dto) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .eq(UpmsRole::getName, dto.getName());
        UpmsRole role = this.getOne(queryWrapper);
        if (role != null) {
            throw new BizException(BizEnum.ROLE_ALREADY_EXISTS.getCode(), BizEnum.ROLE_ALREADY_EXISTS.getMsg());
        }
        UpmsRole newUserGroup = CglibUtil.copy(dto, UpmsRole.class);
        this.save(newUserGroup);
        return dto;
    }

    @Override
    public void updateRoleById(RoleUpdateDTO dto) {
        LambdaQueryWrapper<UpmsRole> queryWrapper = new LambdaQueryWrapper<UpmsRole>()
                .eq(UpmsRole::getName, dto.getName())
                .ne(UpmsRole::getId, dto.getId());
        UpmsRole upmsRole = this.getOne(queryWrapper);
        if (upmsRole != null) {
            throw new BizException(BizEnum.ROLE_ALREADY_EXISTS.getCode(),
                    BizEnum.ROLE_ALREADY_EXISTS.getMsg());
        }
        UpmsRole updateUpmsRole = CglibUtil.copy(dto, UpmsRole.class);
        this.updateById(updateUpmsRole);
    }

    @Override
    public void disableRole(RoleUpdateDTO dto) {
        updateUserStatus(dto, RoleStatusEnum.DISABLED);
    }

    @Override
    public void enableRole(RoleUpdateDTO dto) {
        updateUserStatus(dto, RoleStatusEnum.ENABLED);
    }

    private void updateUserStatus(RoleUpdateDTO dto, RoleStatusEnum roleStatusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsRole>()
                .eq(UpmsRole::getStatus, dto.getId())
                .set(UpmsRole::getStatus, roleStatusEnum.getValue()));
    }
}
