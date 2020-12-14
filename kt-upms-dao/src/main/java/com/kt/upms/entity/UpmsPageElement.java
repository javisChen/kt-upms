package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 页面元素表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsPageElement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 页面id，关联upms_page.id
     */
    @TableField("page_id")
    private Long pageId;

    /**
     * 元素名称
     */
    @TableField("name")
    private String name;

}
