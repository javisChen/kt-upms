package com.kt.iam.auth.core.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCacheInfo {

    private String accessToken;
    private Integer expires;
}
