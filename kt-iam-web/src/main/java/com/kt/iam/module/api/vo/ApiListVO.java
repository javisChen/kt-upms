package com.kt.iam.module.api.vo;

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
    private Long permissionId;
    private String permissionCode;

}
