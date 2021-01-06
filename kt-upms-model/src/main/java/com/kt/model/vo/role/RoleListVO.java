package com.kt.model.vo.role;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleListVO {

    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
