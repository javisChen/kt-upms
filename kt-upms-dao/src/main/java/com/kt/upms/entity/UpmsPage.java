package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 页面表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpmsPage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 页面名称
     */
    @TableField("name")
    private String name;


}
