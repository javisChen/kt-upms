package com.kt.upms.module.permission.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.redis.RedisService;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionStatusEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsPermissionMapper;
import com.kt.upms.module.permission.bo.ApiPermissionBO;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.route.dto.PermissionQueryDTO;
import com.kt.upms.module.route.dto.PermissionUpdateDTO;
import com.kt.upms.module.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

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
public class PermissionServiceImpl extends ServiceImpl<UpmsPermissionMapper, UpmsPermission>
        implements IPermissionService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IUserService iUserService;

    @Override
    public PageResponse pageList(Page page, PermissionQueryDTO dto) {
        LambdaQueryWrapper<UpmsPermission> queryWrapper = new LambdaQueryWrapper<UpmsPermission>()
                .like(StrUtil.isNotBlank(dto.getCode()), UpmsPermission::getCode, dto.getCode())
                .eq(StrUtil.isNotBlank(dto.getType()), UpmsPermission::getType, dto.getType());
        Page pageResult = this.page(page, queryWrapper);
        return PageResponse.build(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), page.getRecords());
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
        if (CollectionUtil.isEmpty(roleIds)) {
            return CollectionUtil.newArrayList();
        }
        return this.baseMapper.selectByRoleIds(roleIds);
    }

    private UpmsPermission getPermissionById(Long permissionId) {
        return this.getOne(new LambdaQueryWrapper<UpmsPermission>().eq(UpmsPermission::getId, permissionId));
    }

    private List<ApiPermissionBO> getPermissionByIds(List<Long> permissionIds) {
        if (CollectionUtil.isEmpty(permissionIds)) {
            return CollectionUtil.newArrayList();
        }
        return this.baseMapper.selectApiPermissionByIds(permissionIds);
    }

    @Override
    public UpmsPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        final LambdaQueryWrapper<UpmsPermission> eq = new LambdaQueryWrapper<UpmsPermission>()
                .eq(UpmsPermission::getResourceId, resourceId)
                .eq(UpmsPermission::getType, permissionTypeEnums.getType());
        return Optional.ofNullable(this.getOne(eq)).orElseGet(UpmsPermission::new);
    }

    @Override
    public List<PermissionVO> getPermissionVOSByRoleIdAndType(Long permissionId, String permissionType) {
        List<UpmsPermission> permissions = this.baseMapper.selectByRoleIdAndType(permissionId, permissionType);
        return permissions.stream().map(this::assembleVo).collect(Collectors.toList());
    }

    @Override
    public UpmsPermission getPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        LambdaQueryWrapper<UpmsPermission> qw = new LambdaQueryWrapper<>();
        qw.select(UpmsPermission::getId, UpmsPermission::getCode);
        qw.eq(UpmsPermission::getResourceId, resourceId);
        qw.eq(UpmsPermission::getType, permissionTypeEnums.getType());
        return Optional.ofNullable(this.getOne(qw)).orElseGet(UpmsPermission::new);
    }

    @Override
    public boolean hasApiPermission(String application, Long userId, String url, String method) {
        List<ApiPermissionBO> apiPermissions = getUserCanAccessApi(application, userId);
        return apiPermissions.stream().anyMatch(item -> {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            if (item.getApiMethod().equalsIgnoreCase(method)) {
            }
            return true;
        });
    }

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        final boolean match = antPathMatcher.match("/admin/user", "/admin/user");
        System.out.println(match);
    }

    private List<ApiPermissionBO> getUserCanAccessApi(String application, Long userId) {
        // 先查出所有API权限
        List<UpmsPermission> permissions = iUserService.getUserPermissions(userId);
        // 根据权限id查出所有api
        List<Long> permissionIds = permissions.stream().map(UpmsPermission::getId).collect(Collectors.toList());
        return getPermissionByIds(permissionIds);
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
