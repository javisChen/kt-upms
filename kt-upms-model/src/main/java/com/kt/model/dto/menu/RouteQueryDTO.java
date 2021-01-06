package com.kt.model.dto.menu;


import com.kt.component.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteQueryDTO extends PagingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Long pid;

    private String path;

    private Integer status;

}
