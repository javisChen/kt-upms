package com.kt.model.dto.role;


import lombok.Data;

import java.io.Serializable;

@Data
public class RoleQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer status;

}
