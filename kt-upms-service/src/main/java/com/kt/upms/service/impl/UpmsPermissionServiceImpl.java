package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.permission.PermissionAddDTO;
import com.kt.model.dto.permission.PermissionQueryDTO;
import com.kt.model.dto.permission.PermissionUpdateDTO;
import com.kt.model.enums.BizEnums;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionStatusEnum;
import com.kt.upms.mapper.UpmsPermissionMapper;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.util.Assert;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Service
public class UpmsPermissionServiceImpl extends ServiceImpl<UpmsPermissionMapper, UpmsPermission> implements IUpmsPermissionService {

    @Override
    public PageResponse pageList(Page page, PermissionQueryDTO dto) {
        LambdaQueryWrapper<UpmsPermission> queryWrapper = new LambdaQueryWrapper<UpmsPermission>()
                .like(StrUtil.isNotBlank(dto.getName()), UpmsPermission::getName, dto.getName())
                .like(StrUtil.isNotBlank(dto.getCode()), UpmsPermission::getCode, dto.getCode())
                .eq(StrUtil.isNotBlank(dto.getType()), UpmsPermission::getType, dto.getType());
        return PageResponse.success(this.page(page, queryWrapper));
    }

    @Override
    public PermissionAddDTO savePermission(PermissionAddDTO dto) {
        int count = countPermissionByNameAndType(dto);
        Assert.isTrue(count > 0, BizEnums.PERMISSION_ALREADY_EXISTS);

        UpmsPermission permission = CglibUtil.copy(dto, UpmsPermission.class);
        permission.setCode(generatePermissionCode());
        this.save(permission);
        return dto;
    }

    private String generatePermissionCode() {
        // TODO 权限编码生成规则待定，暂时用uuid
        return StrUtil.uuid();
    }

    private int countPermissionByNameAndType(PermissionAddDTO dto) {
        LambdaQueryWrapper<UpmsPermission> queryWrapper = new LambdaQueryWrapper<UpmsPermission>()
                .eq(UpmsPermission::getName, dto.getName())
                .eq(UpmsPermission::getType, dto.getType());
        return this.count(queryWrapper);
    }

    @Override
    public void updatePermissionById(PermissionUpdateDTO dto) {
        LambdaQueryWrapper<UpmsPermission> queryWrapper = new LambdaQueryWrapper<UpmsPermission>()
                .eq(UpmsPermission::getName, dto.getName())
                .eq(UpmsPermission::getType, dto.getType())
                .ne(UpmsPermission::getId, dto.getId());
        int count = this.count(queryWrapper);
        Assert.isTrue(count > 0, BizEnums.PERMISSION_ALREADY_EXISTS);

        UpmsPermission permission = CglibUtil.copy(dto, UpmsPermission.class);
        this.updateById(permission);
    }

    @Override
    public void updateStatus(PermissionUpdateDTO dto) {
        updateStatus(dto, PermissionStatusEnum.DISABLED);
    }

    private void updateStatus(PermissionUpdateDTO dto, PermissionStatusEnum statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsPermission>()
                .eq(UpmsPermission::getId, dto.getId())
                .set(UpmsPermission::getStatus, statusEnum.getValue()));
    }
}
