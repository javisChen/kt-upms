package com.kt.iam.module.api.cache;

import com.kt.iam.module.api.persistence.IamApi;
import com.kt.iam.enums.ApiAuthTypeEnums;
import com.kt.iam.module.api.service.IApiService;
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
public class ApiCacheHolder implements InitializingBean {

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

    private ApiCacheHolder() {

    }

    public void init() {
        List<IamApi> apis = iApiService.listAll();
        noNeedAuthorizationApiCache = filterNoNeedAuthorizationApis(apis);
        noNeedAuthenticationApiCache = filterNoNeedAuthenticationApis(apis);
        hasPathVariableApiCache = filterHasPathVariableApis(apis);
    }

    private Map<String, String> filterNoNeedAuthorizationApis(List<IamApi> apis) {
        return apis.stream()
                .filter(item -> item.getAuthType().equals(ApiAuthTypeEnums.NEED_AUTHENTICATION.getValue())
                        || item.getAuthType().equals(ApiAuthTypeEnums.NO_AUTHENTICATION_AND_AUTHORIZATION.getValue()))
                .collect(Collectors.toMap(this::createKey, IamApi::getUrl));
    }

    private Map<String, String> filterNoNeedAuthenticationApis(List<IamApi> apis) {
        return apis.stream()
                .filter(item -> item.getAuthType().equals(ApiAuthTypeEnums.NO_AUTHENTICATION_AND_AUTHORIZATION.getValue()))
                .collect(Collectors.toMap(this::createKey, IamApi::getUrl));
    }

    private String createKey(IamApi api) {
        return api.getUrl() + ":" + api.getMethod() + ":" + api.getApplicationId();
    }

    private List<String> filterHasPathVariableApis(List<IamApi> apis) {
        return apis.stream()
                .filter(item -> item.getHasPathVariable().equals(true))
                .map(IamApi::getUrl)
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
