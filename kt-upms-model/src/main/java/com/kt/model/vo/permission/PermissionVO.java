package com.kt.model.vo.permission;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long permissionId;
}
