package com.kt.upms.module.api.dto;

import com.kt.component.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiQueryDTO extends PagingDTO {

    private Long applicationId;

    private String name;

    /**
     * 状态 1-已上线；2-已下线；
     */
    private Integer status;

    /**
     * 应用类型 1-业务系统（前后端）2-纯后台服务
     */
    private Integer type;

}
