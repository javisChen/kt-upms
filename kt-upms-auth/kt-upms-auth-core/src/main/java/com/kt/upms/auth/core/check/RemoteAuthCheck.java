package com.kt.upms.auth.core.check;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;
import com.kt.upms.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RemoteAuthCheck implements AuthCheck {

    private AuthProperties authProperties;

    public RemoteAuthCheck(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public AuthResponse checkPermission(AuthRequest authRequest) {
        String body = JSONObject.toJSONString(authRequest);
        String authResponseBody = null;
        try {
            authResponseBody = HttpUtil.post(authProperties.getServerUrl(), body, authProperties.getTimeout());
        } catch (Exception e) {
            log.error("统一认证服务请求失败", e);
            return AuthResponse.fail("Auth Server Error：" + e.getMessage());
        }
        if (StringUtils.isBlank(authResponseBody)) {
            return AuthResponse.fail("Auth Server Error：Unknown Error");
        }
        AuthResponse authResponse = JSONObject.parseObject(authResponseBody, AuthResponse.class);
        if (log.isDebugEnabled()) {
            log.debug("Receive Auth Result -----> {}", authResponse);
        }
        return authResponse;
    }

}
