package com.kt.upms.module.application.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationListVO extends ApplicationBaseVO {

    private Integer apiCount;
    private Integer routeCount;

}
