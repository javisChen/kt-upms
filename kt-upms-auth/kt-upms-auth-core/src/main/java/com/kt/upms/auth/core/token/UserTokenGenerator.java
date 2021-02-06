package com.kt.upms.auth.core.token;

import com.kt.upms.auth.core.model.LoginUserDetails;

public interface UserTokenGenerator {

    String generate(LoginUserDetails loginUserDetails);

    String generate();
}
