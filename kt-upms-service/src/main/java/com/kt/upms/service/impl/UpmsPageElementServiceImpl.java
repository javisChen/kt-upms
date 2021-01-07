package com.kt.upms.service.impl;
import java.time.LocalDateTime;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.model.dto.pageelement.PageElementAddDTO;
import com.kt.model.dto.pageelement.PageElementQueryDTO;
import com.kt.model.dto.pageelement.PageElementUpdateDTO;
import com.kt.model.vo.pageelement.PageElementListVO;
import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsPageElementMapper;
import com.kt.upms.service.IUpmsPageElementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public List<PageElementListVO> listPageElement(PageElementQueryDTO dto) {
        List<UpmsPageElement> list = Optional.ofNullable(this.list()).orElseGet(ArrayList::new);
        return list.stream().map(this::assemblePageElementListVO).collect(Collectors.toList());
    }

    private PageElementListVO assemblePageElementListVO(UpmsPageElement item) {
        PageElementListVO vo = new PageElementListVO();
        vo.setRouteId(item.getRouteId());
        vo.setName(item.getName());
        vo.setType(item.getType());
        return vo;
    }
}
