package com.kt.iam.module.route.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteListTreeVO extends RouteBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer level;
    private Long permissionId;
    private String permissionCode;
    private List<RouteListTreeVO> children;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
