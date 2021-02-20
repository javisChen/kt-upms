package com.kt.iam.auth.core.extractor;

import com.kt.iam.config.AccessTokenProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Token提取器默认实现
 */
@Component
public class DefaultTokenExtractor implements TokenExtractor {

    @Override
    public String extract(HttpServletRequest request, AccessTokenProperties properties) {

        String token = null;
        // parameter query token
        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            String[] tokenParamVal = request.getParameterMap().get(properties.getTokenQueryParam());
            if (tokenParamVal != null && tokenParamVal.length == 1) {
                token = tokenParamVal[0];
            }
        }
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // header token
        String headerPrefix = properties.getTokenHeaderPrefix();
        String header = request.getHeader(properties.getTokenHeader());
        if (StringUtils.isBlank(header)) {
            return null;
        }
        if (!header.startsWith(headerPrefix)) {
            return null;
        }
        return header.substring(headerPrefix.length());
    }
}
