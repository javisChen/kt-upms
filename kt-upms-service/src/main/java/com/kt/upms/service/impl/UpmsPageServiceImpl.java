package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.model.dto.page.PageAddDTO;
import com.kt.model.dto.page.PageQueryDTO;
import com.kt.model.dto.page.PageUpdateDTO;
import com.kt.model.enums.BizEnum;
import com.kt.upms.entity.UpmsPage;
import com.kt.upms.enums.PageStatusEnum;
import com.kt.upms.mapper.UpmsPageMapper;
import com.kt.upms.service.IUpmsPageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 页面表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Service
public class UpmsPageServiceImpl extends ServiceImpl<UpmsPageMapper, UpmsPage> implements IUpmsPageService {

    @Override
    public void updateStatus(PageUpdateDTO dto) {
        updateStatus(dto, PageStatusEnum.DISABLED);
    }

    @Override
    public PageAddDTO savePage(PageAddDTO dto) {
        LambdaQueryWrapper<UpmsPage> queryWrapper = new LambdaQueryWrapper<UpmsPage>()
                .eq(UpmsPage::getName, dto.getName());
        UpmsPage upmsPermission = this.getOne(queryWrapper);
        if (upmsPermission != null) {
            throw new BizException(BizEnum.PERMISSION_ALREADY_EXISTS.getCode(),
                    BizEnum.PERMISSION_ALREADY_EXISTS.getMsg());
        }

        UpmsPage newPage = CglibUtil.copy(dto, UpmsPage.class);
        this.save(newPage);
        return dto;
    }

    @Override
    public PageResponse pageList(Page page, PageQueryDTO dto) {
        LambdaQueryWrapper<UpmsPage> queryWrapper = new LambdaQueryWrapper<UpmsPage>()
                .like(StrUtil.isNotBlank(dto.getName()), UpmsPage::getName, dto.getName());
        return PageResponse.success(this.page(page, queryWrapper));
    }

    @Override
    public void updatePage(PageUpdateDTO dto) {
        LambdaQueryWrapper<UpmsPage> queryWrapper = new LambdaQueryWrapper<UpmsPage>()
                .eq(UpmsPage::getName, dto.getName())
                .ne(UpmsPage::getId, dto.getId());
        UpmsPage upmsPage = this.getOne(queryWrapper);
        if (upmsPage != null) {
            throw new BizException(BizEnum.PERMISSION_ALREADY_EXISTS.getCode(),
                    BizEnum.PERMISSION_ALREADY_EXISTS.getMsg());
        }
        UpmsPage updateObj = CglibUtil.copy(dto, UpmsPage.class);
        this.updateById(updateObj);
    }


    private void updateStatus(PageUpdateDTO dto, PageStatusEnum statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsPage>()
                .eq(UpmsPage::getId, dto.getId())
                .set(UpmsPage::getStatus, statusEnum.getValue()));
    }
}
