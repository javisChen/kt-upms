package com.kt.upms.module.role.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleListVO extends RoleBaseVO {

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
