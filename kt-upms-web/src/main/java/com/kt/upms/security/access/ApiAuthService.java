package com.kt.upms.security.access;

import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.security.model.AuthenticationRequest;
import com.kt.upms.security.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthService implements IApiAuthService {

    @Autowired
    private IPermissionService iPermissionService;

    @Override
    public AuthenticationResponse auth(AuthenticationRequest request) {
        boolean hasPermission = iPermissionService
                .hasApiPermission(request.getApplication(), request.getUserCode(), request.getUrl(), request.getMethod());
        return AuthenticationResponse.create(hasPermission);
    }
}