package com.kt.upms.module.application.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class ApplicationBaseVO {

    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用编码
     */
    private String code;

    /**
     * 状态 1-已上线；2-已下线；
     */
    @TableField("status")
    private Integer status;

    /**
     * 应用类型 1-业务系统（前后端）2-纯后台服务
     */
    @TableField("type")
    private Integer type;

}
