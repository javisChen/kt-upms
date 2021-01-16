package com.kt.upms.module.api.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiListVO extends ApiBaseVO {

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
