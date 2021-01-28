package com.kt.upms.module.permission.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.enums.PermissionStatusEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.bo.ApiPermissionBO;
import com.kt.upms.module.permission.converter.PermissionBeanConverter;
import com.kt.upms.module.permission.persistence.UpmsPermission;
import com.kt.upms.module.permission.persistence.UpmsPermissionRoleRel;
import com.kt.upms.module.permission.persistence.dao.UpmsPermissionMapper;
import com.kt.upms.module.permission.persistence.dao.UpmsPermissionRoleRelMapper;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.route.dto.PermissionUpdateDTO;
import com.kt.upms.module.user.persistence.UpmsUser;
import com.kt.upms.module.user.service.IUserPermissionService;
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
    private PermissionBeanConverter beanConverter;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IUserPermissionService iUserPermissionService;

    @Autowired
    private UpmsPermissionRoleRelMapper permissionRoleRelMapper;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        UpmsPermission permission = new UpmsPermission();
        permission.setType(permissionTypeEnums.getType());
        permission.setResourceId(resourceId);
        permission.setStatus(PermissionStatusEnums.ENABLED.getValue());
        permission.setCode(generatePermissionCode(permissionTypeEnums.getTag(), resourceId));
        this.save(permission);
    }

    @Override
    public void batchSaveRolePermissionRel(Long roleId, List<Long> permissionIds) {
        if (CollectionUtil.isNotEmpty(permissionIds)) {
            permissionRoleRelMapper.batchInsert(roleId, permissionIds);
        }
    }

    @Override
    public void removeRolePermission(Long roleId, List<Long> permissionIds) {
        if (CollectionUtil.isNotEmpty(permissionIds)) {
            LambdaQueryWrapper<UpmsPermissionRoleRel> qw = new LambdaQueryWrapper<UpmsPermissionRoleRel>()
                    .eq(UpmsPermissionRoleRel::getRoleId, roleId)
                    .in(UpmsPermissionRoleRel::getPermissionId, permissionIds);
            permissionRoleRelMapper.delete(qw);
        }
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
    public List<UpmsPermission> getPermissionByRoleIds(Set<Long> roleIds, PermissionTypeEnums permissionTypeEnums) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return CollectionUtil.newArrayList();
        }
        return this.baseMapper.selectByRoleIdsAndType(roleIds, permissionTypeEnums.getType());
    }

    @Override
    public List<UpmsPermission> getAllPermissionsByType(PermissionTypeEnums type) {
        LambdaQueryWrapper<UpmsPermission> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsPermission::getType, type);
        qw.eq(UpmsPermission::getStatus, PermissionStatusEnums.ENABLED.getValue());
        return this.list(qw);
    }

    private UpmsPermission getPermissionById(Long permissionId) {
        return this.getOne(new LambdaQueryWrapper<UpmsPermission>().eq(UpmsPermission::getId, permissionId));
    }

    private List<ApiPermissionBO> getApiPermissionByPermissionIds(List<Long> permissionIds) {
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
    public List<PermissionVO> getRolePermissionVos(Long applicationId, Long roleId, String permissionType) {
        List<UpmsPermission> permissions = this.baseMapper.selectByRoleIdAndType(applicationId, roleId, permissionType);
        return permissions.stream().map(beanConverter::convertToVO).collect(Collectors.toList());
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
        return apiPermissions
                .stream()
                .anyMatch(item -> {
                    boolean methodEquals = item.getApiMethod().equalsIgnoreCase(method);
                    boolean urlEquals = antPathMatcher.match(item.getApiUrl(), url);
                    return methodEquals && urlEquals;
                });
    }

    @Override
    public boolean hasApiPermission(String application, String userCode, String url, String method) {
        UpmsUser user = iUserService.getUserByCode(userCode);
        return hasApiPermission(application, user, url, method);
    }

    @Override
    public void removeByResourceIds(List<Long> ids) {
        LambdaQueryWrapper<UpmsPermission> qw = new LambdaQueryWrapper<>();
        qw.in(UpmsPermission::getResourceId, ids);
        this.remove(qw);
    }

    private boolean hasApiPermission(String application, UpmsUser user, String url, String method) {
        if (iUserPermissionService.isSuperAdmin(user.getCode())) {
            return true;
        }
        return hasApiPermission(application, user.getId(), url, method);
    }

    /**
     * 获取用户可访问的api并且需要授权认证的API
     */
    private List<ApiPermissionBO> getUserCanAccessApi(String application, Long userId) {
        // 先查出所有权限
        List<UpmsPermission> permissions = iUserPermissionService.getUserPermissions(userId, PermissionTypeEnums.SER_API);
        // 根据权限id查出所有api
        List<Long> permissionIds = permissions.stream().map(UpmsPermission::getId).collect(Collectors.toList());
        return getApiPermissionByPermissionIds(permissionIds);
    }

    private void updateStatus(PermissionUpdateDTO dto, PermissionStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsPermission>()
                .eq(UpmsPermission::getId, dto.getId())
                .set(UpmsPermission::getStatus, statusEnum.getValue()));
    }
}
