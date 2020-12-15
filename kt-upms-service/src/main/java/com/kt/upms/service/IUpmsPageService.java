package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.page.PageAddDTO;
import com.kt.model.dto.page.PageQueryDTO;
import com.kt.model.dto.page.PageUpdateDTO;
import com.kt.upms.entity.UpmsPage;

/**
 * <p>
 * 页面表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsPageService extends IService<UpmsPage> {

    void updateStatus(PageUpdateDTO dto);

    PageAddDTO savePage(PageAddDTO dto);

    PageResponse pageList(Page page, PageQueryDTO params);

    void updatePage(PageUpdateDTO dto);
}
