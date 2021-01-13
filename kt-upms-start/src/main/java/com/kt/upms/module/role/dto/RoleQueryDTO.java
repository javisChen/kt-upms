package com.kt.upms.module.role.dto;


import com.kt.component.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryDTO extends PagingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer status;

}
