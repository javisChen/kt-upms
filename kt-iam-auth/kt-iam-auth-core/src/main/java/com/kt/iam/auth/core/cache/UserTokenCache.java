package com.kt.iam.auth.core.cache;

import com.kt.iam.auth.core.model.LoginUserContext;

/**
 * 用户Token缓存
 */
public interface UserTokenCache {

    UserCacheInfo save(LoginUserContext value);

    void remove(String accessToken);

    void remove(Long userId);

    LoginUserContext get(String accessToken);

}
