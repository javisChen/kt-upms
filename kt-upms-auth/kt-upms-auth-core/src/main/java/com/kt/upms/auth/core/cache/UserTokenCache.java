package com.kt.upms.auth.core.cache;

import com.kt.upms.auth.core.model.LoginUserContext;

/**
 * 用户Token缓存
 */
public interface UserTokenCache {

    /**
     * 保存用户信息缓存
     * @return 保存成功后换回Token
     */
    UserCacheInfo save(Object value);

    /**
     * 移除token
     * @param accessToken token key
     */
    void remove(String accessToken);

    /**
     * 获取token
     *
     * @param accessToken token key
     * @return TokenStorageDTO
     */
    LoginUserContext get(String accessToken);

}
