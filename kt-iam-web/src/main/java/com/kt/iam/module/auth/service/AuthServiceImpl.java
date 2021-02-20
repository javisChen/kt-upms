package com.kt.iam.module.auth.service;

import com.kt.iam.auth.core.cache.UserTokenCache;
import com.kt.iam.auth.core.extractor.TokenExtractor;
import com.kt.iam.module.auth.dto.AuthKickDTO;
import com.kt.iam.security.configuration.SecurityCoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserTokenCache userTokenCache;
    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    private SecurityCoreProperties coreProperties;

    @Override
    public void kick(AuthKickDTO dto) {
        Long userId = dto.getUserId();
        userTokenCache.remove(userId);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = tokenExtractor.extract(request, coreProperties.getAuthentication());
        if (StringUtils.isNotBlank(accessToken)) {
            userTokenCache.remove(accessToken);
        }
    }
}
