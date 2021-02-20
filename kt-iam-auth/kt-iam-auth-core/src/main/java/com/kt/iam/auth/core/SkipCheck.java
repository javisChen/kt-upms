package com.kt.iam.auth.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跳过权限校验
 * 使用在包含@RestController的类时，该Controller下所有接口将不进行权限校验
 * 使用在接口方法上时，该方法的接口将不进行权限校验
 * 该注解适合使用在开发阶段使用，测试和生产阶段建议把所有接口都交由权限中心进行校验
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipCheck {
}
