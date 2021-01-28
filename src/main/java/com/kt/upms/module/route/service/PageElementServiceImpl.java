package com.kt.upms.module.route.service;


import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.route.dto.PageElementUpdateDTO;
import com.kt.upms.module.route.dto.RouteUpdateDTO;
import com.kt.upms.module.route.persistence.UpmsPageElement;
import com.kt.upms.module.route.persistence.dao.UpmsPageElementMapper;
import com.kt.upms.module.route.vo.PageElementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 页面元素表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
public class PageElementServiceImpl extends ServiceImpl<UpmsPageElementMapper, UpmsPageElement>
        implements IPageElementService {

    @Autowired
    private IPermissionService iUpmsPermissionService;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void savePageElement(PageElementUpdateDTO dto) {
        UpmsPageElement pageElement = CglibUtil.copy(dto, UpmsPageElement.class);
        this.save(pageElement);
        iUpmsPermissionService.addPermission(pageElement.getId(), PermissionTypeEnums.PAGE_ELEMENT);
    }

    private List<UpmsPageElement> listPageElements(Wrapper<UpmsPageElement> queryWrapper) {
        return Optional.ofNullable(this.list(queryWrapper)).orElseGet(ArrayList::new);
    }

    @Override
    public void batchSavePageElement(Long routeId, List<RouteUpdateDTO.Element> elements) {
        elements.forEach(item -> {
            PageElementUpdateDTO dto = new PageElementUpdateDTO();
            dto.setRouteId(routeId);
            dto.setName(item.getName());
            dto.setType(item.getType());
            this.savePageElement(dto);
        });
    }

    @Override
    public List<PageElementVO> getPageElementVOSByRouteId(Long routeId) {
        return getPageElementsByRouteId(routeId).stream().map(this::assemblePageElementVO).collect(Collectors.toList());
    }

    @Override
    public List<UpmsPageElement> getPageElementsByRouteId(Long routeId) {
        LambdaQueryWrapper<UpmsPageElement> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsPageElement::getRouteId, routeId);
        qw.eq(UpmsPageElement::getIsDeleted, DeletedEnums.NOT.getCode());
        return listPageElements(qw);
    }

    @Override
    public void removePageElementByRouteId(Long routeId) {
        this.update(new LambdaUpdateWrapper<UpmsPageElement>()
                .eq(UpmsPageElement::getRouteId, routeId)
                .set(UpmsPageElement::getIsDeleted, DeletedEnums.YET.getCode())
        );
    }

    @Override
    public List<UpmsPageElement> listElementsByRouteId(Long routeId) {
        LambdaQueryWrapper<UpmsPageElement> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsPageElement::getRouteId, routeId);
        qw.eq(UpmsPageElement::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.listPageElements(qw);
    }

    @Override
    public List<UpmsPageElement> getPageElementsByIds(List<Long> elementIds) {
        LambdaQueryWrapper<UpmsPageElement> qw = new LambdaQueryWrapper<>();
        qw.in(UpmsPageElement::getId, elementIds);
        qw.eq(UpmsPageElement::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.listPageElements(qw);
    }

    private PageElementVO assemblePageElementVO(UpmsPageElement item) {
        PageElementVO vo = new PageElementVO();
        vo.setId(item.getId());
        vo.setRouteId(item.getRouteId());
        vo.setName(item.getName());
        vo.setType(item.getType());
        return vo;
    }
}
