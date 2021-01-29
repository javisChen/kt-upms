package com.kt.upms.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * 登录配置
 *
 * @author karl
 */
@Data
@Configuration
public class LoginProperties {

    /**
     * 失效时间（单位秒），默认七天
     */
    private Long expire = (long) (60 * 60 * 24 * 7);

    /**
     * 验证码令牌key
     */
    private String captchaTokenKey = "c_t";

    /**
     * 验证码key
     */
    private String captchaKey = "c";

    /**
     * 验证码失效时间（单位秒），默认90秒
     */
    private Long captchaKeyExpire = 90L;


}
