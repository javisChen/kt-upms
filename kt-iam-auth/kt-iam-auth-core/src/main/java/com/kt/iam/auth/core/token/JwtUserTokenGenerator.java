package com.kt.iam.auth.core.token;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kt.iam.auth.core.model.LoginUserDetails;

import java.util.Date;
import java.util.Map;

public class JwtUserTokenGenerator implements UserTokenGenerator {

    private final String secret = "*$Y4F*$BdC4&tZJh";

    @Override
    public String generate(LoginUserDetails loginUserDetails) {
        return createJwt(loginUserDetails);
    }

    @Override
    public String generate() {
        return null;
    }

    private String createJwt(LoginUserDetails loginUserDetails) {
        Date now = new Date();
        return JWT.create()
//                .withClaim("userid", userDB.getId())//添加payload
//                .withClaim("username", userDB.getUsername())
//                .withClaim("email", userDB.getEmail())
//                .withExpiresAt()//设置过期时间
                .withAudience(loginUserDetails.getUserCode())
                .withNotBefore(now) // 生效时间
                .withIssuedAt(now) // 签发时间
                .withJWTId(StrUtil.uuid()) // 编号
                .withIssuer("IAM") // 签发人
                .sign(Algorithm.HMAC256(secret));//设置签名 密钥

    }

    private Map<String, Object> createHeader() {
        return null;
    }
}
