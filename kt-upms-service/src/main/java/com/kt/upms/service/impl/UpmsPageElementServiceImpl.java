package com.kt.upms.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.model.dto.route.RouteAddDTO;
import com.kt.model.dto.pageelement.PageElementAddDTO;
import com.kt.model.dto.pageelement.PageElementQueryDTO;
import com.kt.model.dto.pageelement.PageElementUpdateDTO;
import com.kt.model.vo.pageelement.PageElementVO;
import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsPageElementMapper;
import com.kt.upms.service.IUpmsPageElementService;
import com.kt.upms.service.IUpmsPermissionService;
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
public class UpmsPageElementServiceImpl extends ServiceImpl<UpmsPageElementMapper, UpmsPageElement> implements IUpmsPageElementService {

    @Autowired
    private IUpmsPermissionService iUpmsPermissionService;

    @Override
    public void updatePageElement(PageElementUpdateDTO dto) {
        UpmsPageElement entity = new UpmsPageElement();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        this.updateById(entity);
    }

    @Override
    public void removePageElement(PageElementUpdateDTO dto) {
        UpmsPageElement entity = new UpmsPageElement();
        entity.setId(dto.getId());
        entity.setIsDeleted(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void savePageElement(PageElementAddDTO dto) {
        UpmsPageElement pageElement = CglibUtil.copy(dto, UpmsPageElement.class);
        this.save(pageElement);
        iUpmsPermissionService.addPermission(pageElement.getId(), PermissionTypeEnums.PAGE_ELEMENT);
    }

    @Override
    public List<PageElementVO> listPageElement(PageElementQueryDTO dto) {
        List<UpmsPageElement> list = Optional.ofNullable(this.list()).orElseGet(ArrayList::new);
        return list.stream().map(this::assemblePageElementVO).collect(Collectors.toList());
    }

    @Override
    public void batchSavePageElement(Long routeId, List<RouteAddDTO.Element> elements) {
        elements.forEach(item -> {
            PageElementAddDTO dto = new PageElementAddDTO();
            dto.setRouteId(routeId);
            dto.setName(item.getName());
            dto.setType(item.getType());
            this.savePageElement(dto);
        });
    }

    @Override
    public List<PageElementVO> getPageElementsByRouteId(Long routeId) {
        LambdaQueryWrapper<UpmsPageElement> qw = new LambdaQueryWrapper<UpmsPageElement>().eq(UpmsPageElement::getRouteId, routeId);
        List<UpmsPageElement> list = Optional.ofNullable(this.list(qw)).orElseGet(ArrayList::new);
        return list.stream().map(this::assemblePageElementVO).collect(Collectors.toList());
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
