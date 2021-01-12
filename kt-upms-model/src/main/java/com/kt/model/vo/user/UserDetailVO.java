package com.kt.model.vo.user;


import com.kt.model.validgroup.UpmsValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class UserDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String phone;
    private String name;
    private Integer status;
    private List<Long> roleIds;
    private List<Long> userGroupIds;;
}
