package com.kt.upms.security.token.extractor;

import com.kt.upms.security.configuration.AuthenticationProperties;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JarvisChen
 * @desc
 * @date 2019-10-18
 * @time 17:23
 */
public class DefaultTokenExtractor implements TokenExtractor {

    @Override
    public String extract(HttpServletRequest request, AuthenticationProperties jwtProperties) {

        String token = null;
        // parameter query token
        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            String[] tokenParamVal = request.getParameterMap().get(jwtProperties.getTokenQueryParam());
            if (tokenParamVal != null && tokenParamVal.length == 1) {
                token = tokenParamVal[0];
            }
        }
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // header token
        String headerPrefix = jwtProperties.getTokenHeaderPrefix();
        String header = request.getHeader(jwtProperties.getTokenHeader());
        if (StringUtils.isBlank(header)) {
            return null;
//            throw new AuthenticationServiceException("Authorization token cannot be blank!");
        }
        if (!header.startsWith(headerPrefix)) {
            return null;
//            throw new AuthenticationServiceException("Authorization token should start with '" + headerPrefix+ "'!");
        }
        return header.substring(headerPrefix.length());
    }
}
