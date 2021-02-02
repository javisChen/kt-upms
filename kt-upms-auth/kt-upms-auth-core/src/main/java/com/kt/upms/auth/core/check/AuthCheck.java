package com.kt.upms.auth.core.check;

import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;

/**
 * 权限校验接口
 */
public interface AuthCheck {

    AuthResponse checkPermission(AuthRequest authRequest);
}
