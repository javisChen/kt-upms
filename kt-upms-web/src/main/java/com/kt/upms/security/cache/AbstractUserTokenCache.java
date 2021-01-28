package com.kt.upms.security.cache;

import com.kt.upms.security.common.RedisKeyConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractUserTokenCache implements UserTokenCache {

    public String createKey(String accessToken) {
        return RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX + accessToken;
    }
}
