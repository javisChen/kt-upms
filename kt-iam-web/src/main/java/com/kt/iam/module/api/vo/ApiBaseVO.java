package com.kt.iam.module.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class ApiBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String url;

    private String method;

    private Integer authType;

    private Integer status;

}
