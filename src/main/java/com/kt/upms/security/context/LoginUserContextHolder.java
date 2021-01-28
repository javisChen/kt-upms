
package com.kt.upms.security.context;

import com.kt.upms.security.model.LoginUserContext;

public class LoginUserContextHolder {

    private static final ThreadLocal<LoginUserContext> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static LoginUserContext getContext() {
        LoginUserContext ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setContext(LoginUserContext context) {
        contextHolder.set(context);
    }

    public static LoginUserContext createEmptyContext() {
        return new LoginUserContext();
    }
}
