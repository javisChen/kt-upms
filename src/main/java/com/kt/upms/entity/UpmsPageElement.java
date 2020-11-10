package com.kt.upms.entity;

import com.kt.db.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
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

    /**
     * 状态 1-正常 2-禁用
     */
    @TableField("status")
    private Boolean status;


}
