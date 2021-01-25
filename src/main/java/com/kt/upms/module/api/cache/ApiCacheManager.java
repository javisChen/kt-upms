package com.kt.upms.module.api.cache;

import com.kt.upms.entity.UpmsApi;
import com.kt.upms.enums.ApiAuthTypeEnums;
import com.kt.upms.module.api.service.IApiService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Api缓存
 */
@Component
public class ApiCacheManager implements InitializingBean {

    @Autowired
    private IApiService iApiService;

    /**
     * 无需授权api缓存
     */
    private Map<String, String> noNeedAuthorizationApiCache;

    /**
     * 无需认证api缓存
     */
    private Map<String, String> noNeedAuthenticationApiCache;

    /**
     * 包含路径参数的api缓存
     */
    private List<String> hasPathVariableApiCache;

    private ApiCacheManager() {

    }

    public void init() {
        List<UpmsApi> apis = iApiService.listAll();
        noNeedAuthorizationApiCache = filterNoNeedAuthorizationApis(apis);
        noNeedAuthenticationApiCache = filterNoNeedAuthenticationApis(apis);
        hasPathVariableApiCache = filterHasPathVariableApis(apis);
    }

    private Map<String, String> filterNoNeedAuthorizationApis(List<UpmsApi> apis) {
        return apis.stream()
                .filter(item -> item.getAuthType().equals(ApiAuthTypeEnums.NEED_AUTHENTICATION.getValue())
                        || item.getAuthType().equals(ApiAuthTypeEnums.NO_AUTHENTICATION_AND_AUTHORIZATION.getValue()))
                .collect(Collectors.toMap(this::createKey, UpmsApi::getUrl));
    }

    private Map<String, String> filterNoNeedAuthenticationApis(List<UpmsApi> apis) {
        return apis.stream()
                .filter(item -> item.getAuthType().equals(ApiAuthTypeEnums.NO_AUTHENTICATION_AND_AUTHORIZATION.getValue()))
                .collect(Collectors.toMap(this::createKey, UpmsApi::getUrl));
    }

    private String createKey(UpmsApi api) {
        return api.getUrl() + ":" + api.getMethod() + ":" + api.getApplicationId();
    }

    private List<String> filterHasPathVariableApis(List<UpmsApi> apis) {
        return apis.stream()
                .filter(item -> item.getHasPathVariable().equals(true))
                .map(UpmsApi::getUrl)
                .collect(Collectors.toList());
    }

    public Map<String, String> getNoNeedAuthorizationApiCache() {
        return noNeedAuthorizationApiCache;
    }

    public Map<String, String> getNoNeedAuthenticationApiCache() {
        return noNeedAuthenticationApiCache;
    }

    public List<String> getHasPathVariableApiCache() {
        return hasPathVariableApiCache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public void update() {
        init();
    }
}
