package com.kt.upms.module.application.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class ApplicationListVO extends ApplicationBaseVO {

    private Integer apiCount;
    private Integer routeCount;

}
