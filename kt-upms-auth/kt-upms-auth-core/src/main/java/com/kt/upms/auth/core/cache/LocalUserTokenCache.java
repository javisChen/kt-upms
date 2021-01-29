package com.kt.upms.auth.core.cache;

import com.alibaba.fastjson.JSONObject;
import com.kt.upms.auth.core.model.LoginUserContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存Token管理器
 */
@Slf4j
public class LocalUserTokenCache extends AbstractUserTokenCache {

    public LocalUserTokenCache() {
        log.info("UserTokenCache ---------> LocalUserTokenCache");
    }

    private final Map<String, Object> caches = new ConcurrentHashMap<>(16);

    @Override
    public void save(String key, Object value, long expires) {
        caches.put(createKey(key), value);
    }

    @Override
    public void remove(String key) {
        caches.remove(createKey(key));
    }

    @Override
    public LoginUserContext get(String key) {
        return JSONObject.parseObject((String) caches.get(createKey(key)), LoginUserContext.class);
    }

}
