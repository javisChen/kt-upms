package com.kt.iam.auth.core.token;

import com.kt.iam.auth.core.model.LoginUserDetails;

public interface UserTokenGenerator {

    String generate(LoginUserDetails loginUserDetails);

    String generate();
}
