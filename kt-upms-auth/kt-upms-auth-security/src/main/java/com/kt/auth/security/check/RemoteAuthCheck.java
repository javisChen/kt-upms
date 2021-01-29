package com.kt.auth.security.check;

import com.kt.upms.auth.core.check.AuthCheck;
import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;

public class RemoteAuthCheck implements AuthCheck {

    @Override
    public AuthResponse checkPermission(AuthRequest request) {
        return AuthResponse.create(true);
    }
}
