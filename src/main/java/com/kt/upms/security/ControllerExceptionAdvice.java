
package com.kt.upms.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * 捕获到该异常后直接往上抛，这样才能到达SpringSecurity的AccessDeniedHandler
     * @param ex
     * @throws AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handle(AccessDeniedException ex) throws AccessDeniedException {
        throw ex;
    }
}