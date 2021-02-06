package com.kt.upms.auth.core.cache;

import com.kt.upms.auth.core.common.RedisKeyConst;
import com.kt.upms.auth.core.model.LoginUserContext;
import com.kt.upms.auth.core.token.UUIDUserTokenGenerator;
import com.kt.upms.auth.core.token.UserTokenGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractUserTokenCache implements UserTokenCache {

    private final UserTokenGenerator userTokenGenerator = new UUIDUserTokenGenerator();

    private int expires = (60 * 60 * 24 * 7) * 1000;

    public AbstractUserTokenCache() {
    }

    public AbstractUserTokenCache(int expires) {
        this.expires = expires;
    }

    private String createKey(String accessToken) {
        return RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX + accessToken;
    }

    @Override
    public final void remove(String accessToken) {
        removeCache(createKey(accessToken));
    }

    @Override
    public final LoginUserContext get(String accessToken) {
        return getCache(createKey(accessToken));
    }

    @Override
    public final UserCacheInfo save(Object value) {
        String accessToken = genAccessToken();
        saveCache(createKey(accessToken), value, expires);
        return new UserCacheInfo(accessToken, expires);
    }

    private String genAccessToken() {
        String accessToken;
        do {
            accessToken = userTokenGenerator.generate();
        } while (!checkAccessTokenIsNotExists(accessToken));
        return accessToken;
    }

    private boolean checkAccessTokenIsNotExists(String accessToken) {
        LoginUserContext loginUserContext = get(accessToken);
        return loginUserContext == null;
    }


    abstract void saveCache(String key, Object value, long expires);

    abstract LoginUserContext getCache(String key);

    abstract void removeCache(String key);

}
