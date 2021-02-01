package com.kt.upms.auth.core.cache;

import com.kt.upms.auth.core.model.LoginUserContext;

/**
 * 用户Token缓存
 */
public interface UserTokenCache {

    /**
     * 保存token
     */
    void save(String key, Object value, long expires);

    /**
     * 移除token
     *
     * @param key token key
     */
    void remove(String key);

    /**
     * 获取token
     *
     * @param key token key
     * @return TokenStorageDTO
     */
    LoginUserContext get(String key);

}
