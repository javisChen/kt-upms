package com.kt.iam.module.usergroup.dto;


import com.kt.component.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserGroupQueryDTO extends PagingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

}
