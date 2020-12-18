package com.kt.upms.util;

import com.kt.component.exception.BizException;
import com.kt.model.enums.BizEnums;

public class Assert {

    public static void isTrue(boolean condition, BizEnums bizEnum) {
        if (condition) {
            throw new BizException(bizEnum.getCode(), bizEnum.getMsg());
        }
    }

}
