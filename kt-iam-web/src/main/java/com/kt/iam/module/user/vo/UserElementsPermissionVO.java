package com.kt.iam.module.user.vo;

import com.kt.iam.module.permission.vo.PermissionVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserElementsPermissionVO implements Serializable {

    List<PermissionVO> permissions;
}
