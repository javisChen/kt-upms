package com.kt.upms.module.permission.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionStatusEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsPermissionMapper;
import com.kt.upms.module.permission.vo.PermissionElementVO;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.route.dto.PermissionQueryDTO;
import com.kt.upms.module.route.dto.PermissionUpdateDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Service
public class UpmsPermissionServiceImpl extends ServiceImpl<UpmsPermissionMapper, UpmsPermission>
        implements IUpmsPermissionService {

    @Override
    public PageResponse pageList(Page page, PermissionQueryDTO dto) {
        LambdaQueryWrapper<UpmsPermission> queryWrapper = new LambdaQueryWrapper<UpmsPermission>()
                .like(StrUtil.isNotBlank(dto.getCode()), UpmsPermission::getCode, dto.getCode())
                .eq(StrUtil.isNotBlank(dto.getType()), UpmsPermission::getType, dto.getType());
        Page pageResult = this.page(page, queryWrapper);
        return PageResponse.success(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), page.getRecords());
    }

    @Override
    public void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        UpmsPermission permission = new UpmsPermission();
        permission.setType(permissionTypeEnums.getType());
        permission.setResourceId(resourceId);
        permission.setStatus(PermissionStatusEnums.ENABLED.getValue());
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
        updateStatus(dto, PermissionStatusEnums.DISABLED);
    }

    @Override
    public List<UpmsPermission> getPermissionByRoleIds(Set<Long> roleIds) {
        return this.baseMapper.selectByRoleIds(roleIds);
    }

    @Override
    public List<PermissionElementVO> getPermissionElements(Long routePermissionId) {
        UpmsPermission permission = getPermissionById(routePermissionId);
        return this.baseMapper.selectPageElementPermissionsByRouteId(permission.getResourceId());
    }

    private UpmsPermission getPermissionById(Long routePermissionId) {
        return this.getOne(new LambdaQueryWrapper<UpmsPermission>().eq(UpmsPermission::getId, routePermissionId));
    }

    @Override
    public UpmsPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        final LambdaQueryWrapper<UpmsPermission> eq = new LambdaQueryWrapper<UpmsPermission>()
                .eq(UpmsPermission::getResourceId, resourceId)
                .eq(UpmsPermission::getType, permissionTypeEnums.getType());
        return Optional.ofNullable(this.getOne(eq)).orElseGet(UpmsPermission::new);
    }

    @Override
    public List<PermissionVO> getPermissionVOSByRoleIdAndType(Long roleId, String type) {
        List<UpmsPermission> permissions = this.baseMapper.selectByRoleIdAndType(roleId, type);
        return permissions.stream().map(this::assembleVo).collect(Collectors.toList());
    }

    private PermissionVO assembleVo(UpmsPermission upmsPermission) {
        PermissionVO vo = new PermissionVO();
        vo.setPermissionId(upmsPermission.getId());
        return vo;
    }

    private void updateStatus(PermissionUpdateDTO dto, PermissionStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsPermission>()
                .eq(UpmsPermission::getId, dto.getId())
                .set(UpmsPermission::getStatus, statusEnum.getValue()));
    }
}
