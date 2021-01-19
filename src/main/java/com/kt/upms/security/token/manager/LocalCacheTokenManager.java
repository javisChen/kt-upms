package com.kt.upms.security.token.manager;

import com.alibaba.fastjson.JSONObject;
import com.kt.upms.security.login.LoginUserDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存Token管理器
 */
@Slf4j
public class LocalCacheTokenManager implements UserTokenManager {

    public LocalCacheTokenManager() {
      log.info("UserTokenManager ---------> LocalCacheTokenManager");
    }

    private final Map<String, Object> caches = new ConcurrentHashMap<>(16);

    @Override
    public void save(String key, Object value, long expires) {
        caches.put(key, value);
    }

    @Override
    public void remove(String key) {
        caches.remove(key);
    }

    @Override
    public LoginUserDetails get(String key) {
        return JSONObject.parseObject((String) caches.get(key), LoginUserDetails.class);
    }

}
