package com.kt.iam.module.permission.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.iam.enums.PermissionStatusEnums;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.module.application.persistence.IamApplication;
import com.kt.iam.module.application.service.IApplicationService;
import com.kt.iam.module.permission.bo.ApiPermissionBO;
import com.kt.iam.module.permission.converter.PermissionBeanConverter;
import com.kt.iam.module.permission.persistence.IamPermission;
import com.kt.iam.module.permission.persistence.IamPermissionRoleRel;
import com.kt.iam.module.permission.persistence.dao.IamPermissionMapper;
import com.kt.iam.module.permission.persistence.dao.IamPermissionRoleRelMapper;
import com.kt.iam.module.permission.vo.PermissionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PermissionServiceImpl extends ServiceImpl<IamPermissionMapper, IamPermission>
        implements IPermissionService {

    @Autowired
    private PermissionBeanConverter beanConverter;

    @Autowired
    private IApplicationService iApplicationService;

    @Autowired
    private IamPermissionRoleRelMapper permissionRoleRelMapper;

    @Override
    public void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        IamPermission permission = new IamPermission();
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
            LambdaQueryWrapper<IamPermissionRoleRel> qw = new LambdaQueryWrapper<IamPermissionRoleRel>()
                    .eq(IamPermissionRoleRel::getRoleId, roleId)
                    .in(IamPermissionRoleRel::getPermissionId, permissionIds);
            permissionRoleRelMapper.delete(qw);
        }
    }

    /**
     * 生成权限编码
     * 规则：类型首字母+资源id（十位，不足左补0） EXAMPLE：P0000000001
     */
    private String generatePermissionCode(String tag, long id) {
        return tag + StringUtils.leftPad(String.valueOf(id), 9, "0");
    }

    @Override
    public List<IamPermission> getPermissionByRoleIds(Set<Long> roleIds, PermissionTypeEnums permissionTypeEnums) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return CollectionUtil.newArrayList();
        }
        return this.baseMapper.selectByRoleIdsAndType(roleIds, permissionTypeEnums.getType());
    }

    @Override
    public List<IamPermission> getAllPermissionsByType(PermissionTypeEnums type) {
        LambdaQueryWrapper<IamPermission> qw = new LambdaQueryWrapper<>();
        qw.eq(IamPermission::getType, type);
        qw.eq(IamPermission::getStatus, PermissionStatusEnums.ENABLED.getValue());
        return this.list(qw);
    }

    private IamPermission getPermissionById(Long permissionId) {
        return this.getOne(new LambdaQueryWrapper<IamPermission>().eq(IamPermission::getId, permissionId));
    }

    @Override
    public IamPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        final LambdaQueryWrapper<IamPermission> eq = new LambdaQueryWrapper<IamPermission>()
                .eq(IamPermission::getResourceId, resourceId)
                .eq(IamPermission::getType, permissionTypeEnums.getType());
        return Optional.ofNullable(this.getOne(eq)).orElseGet(IamPermission::new);
    }

    @Override
    public List<PermissionVO> getRolePermissionVos(Long applicationId, Long roleId, String permissionType) {
        List<IamPermission> permissions = this.baseMapper.selectByRoleIdAndType(applicationId, roleId, permissionType);
        return permissions.stream().map(beanConverter::convertToVO).collect(Collectors.toList());
    }

    @Override
    public IamPermission getPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums) {
        LambdaQueryWrapper<IamPermission> qw = new LambdaQueryWrapper<>();
        qw.select(IamPermission::getId, IamPermission::getCode);
        qw.eq(IamPermission::getResourceId, resourceId);
        qw.eq(IamPermission::getType, permissionTypeEnums.getType());
        return Optional.ofNullable(this.getOne(qw)).orElseGet(IamPermission::new);
    }

    @Override
    public List<ApiPermissionBO> getApiPermissionByIds(List<Long> permissionIds) {
        return this.baseMapper.selectApiPermissionByIds(permissionIds);
    }

    @Override
    public Set<ApiPermissionBO> getApiPermissionByRoleIdsAndApplicationCode(String applicationCode, Set<Long> roleIdSet) {
        if (CollectionUtil.isEmpty(roleIdSet)) {
            return CollectionUtil.newHashSet();
        }
        IamApplication application = iApplicationService.getApplicationByCode(applicationCode);
        if (application == null) {
            return CollectionUtil.newHashSet();
        }
        return this.baseMapper.selectApiPermissionsByRoleIdsAndApplicationId(application.getId(), roleIdSet);
    }


    @Override
    public void removeByResourceIds(List<Long> ids) {
        LambdaQueryWrapper<IamPermission> qw = new LambdaQueryWrapper<>();
        qw.in(IamPermission::getResourceId, ids);
        this.remove(qw);
    }

}
