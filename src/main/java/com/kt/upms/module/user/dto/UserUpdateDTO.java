package com.kt.upms.module.user.dto;


import com.kt.upms.validgroup.UpmsValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    private Long id;

    private String name;

    @NotNull(groups = UpmsValidateGroup.UpdateStatus.class, message = "status 不能为空")
    @Range(min = 1, max = 2)
    private Integer status;

    public List<Long> roleIds;

    public List<Long> userGroupIds;
}
