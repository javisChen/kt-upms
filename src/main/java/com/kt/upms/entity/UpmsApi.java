package com.kt.upms.entity;

import com.kt.db.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * api表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpmsApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * API名称
     */
    @TableField("name")
    private String name;

    /**
     * API编码
     */
    @TableField("code")
    private String code;

    /**
     * 状态 1-启用 2-禁用
     */
    @TableField("status")
    private Boolean status;


}
