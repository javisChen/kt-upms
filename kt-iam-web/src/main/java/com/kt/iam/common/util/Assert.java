package com.kt.iam.common.util;

import com.kt.component.exception.BizException;
import com.kt.iam.enums.BizEnums;

public class Assert {

    public static void isTrue(boolean condition, BizEnums bizEnum) {
        if (condition) {
            throw new BizException(bizEnum.getCode(), bizEnum.getMsg());
        }
    }

    public static void isFalse(boolean condition, BizEnums bizEnum) {
        isTrue(!condition, bizEnum);
    }

}
