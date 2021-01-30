package com.kt.upms.auth.core.extractor;


import com.kt.upms.config.AccessTokenProperties;

import javax.servlet.http.HttpServletRequest;

/**
 * token提取器，可自行实现提取方法
 *
 * @Author javis
 * @Date 2019-06-24 17:37
 **/
public interface TokenExtractor {

    /**
     * 提取token
     *
     * @param request
     * @param properties
     * @return token
     */
    String extract(HttpServletRequest request, AccessTokenProperties properties);
}
