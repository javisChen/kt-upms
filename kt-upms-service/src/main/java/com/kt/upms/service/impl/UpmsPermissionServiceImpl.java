package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.permission.PermissionQueryDTO;
import com.kt.model.dto.permission.PermissionUpdateDTO;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionStatusEnum;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsPermissionMapper;
import com.kt.upms.service.IUpmsPermissionService;
import org.apache.commons.lang3.StringUtils;
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
                .like(StrUtil.isNotBlank(dto.getCode()), UpmsPermission::getCode, dto.getCode())
                .eq(StrUtil.isNotBlank(dto.getType()), UpmsPermission::getType, dto.getType());
        return PageResponse.success(this.page(page, queryWrapper));
    }

    @Override
    public void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        UpmsPermission permission = new UpmsPermission();
        permission.setType(permissionTypeEnums.getType());
        permission.setResourceId(resourceId);
        permission.setStatus(PermissionStatusEnum.ENABLED.getValue());
        permission.setCode(generatePermissionCode(permissionTypeEnums.getTag(), resourceId));
        this.save(permission);
    }

    /**
     * 生成权限编码
     * 规则：类型首字母+资源id（五位，不足左补0） EXAMPLE：P00001
     */
    private String generatePermissionCode(String tag, long id) {
        return tag + StringUtils.leftPad(String.valueOf(id), 5, "0");
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
