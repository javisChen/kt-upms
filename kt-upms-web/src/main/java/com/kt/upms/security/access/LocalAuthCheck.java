package com.kt.upms.security.access;

import com.kt.upms.auth.core.check.AuthCheck;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;
import com.kt.upms.module.user.service.IUserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalAuthCheck implements AuthCheck {

    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private IUserPermissionService iUserPermissionService;

    @Override
    public AuthResponse checkPermission(AuthRequest request) {
        return iUserPermissionService.checkPermission(request);
    }
}
