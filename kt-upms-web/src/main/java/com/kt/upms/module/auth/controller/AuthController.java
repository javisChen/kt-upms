package com.kt.upms.module.auth.controller;


import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.auth.core.cache.UserTokenCache;
import com.kt.upms.auth.core.extractor.TokenExtractor;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class AuthController extends BaseController {

    @Autowired
    private UserTokenCache userTokenCache;
    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    private SecurityCoreProperties coreProperties;

    @PostMapping("/auth/logout")
    public ServerResponse logout() {
        String accessToken = tokenExtractor.extract(getRequest(), coreProperties.getAuthentication());
        if (StringUtils.isNoneBlank(accessToken)) {
            userTokenCache.remove(accessToken);
        }
        return ServerResponse.ok();
    }
}

