
package com.kt.upms.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    /**
     * 注解@hasRole不match的时候会抛到这个异常
     * @param ex
     * @throws AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handle(AccessDeniedException ex) throws AccessDeniedException {
        log.error("权限不足", ex);
        throw ex;
    }

}