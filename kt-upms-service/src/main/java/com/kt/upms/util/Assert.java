package com.kt.upms.util;

import com.kt.component.exception.BizException;
import com.kt.model.enums.BizEnums;
import org.omg.CORBA._IDLTypeStub;

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
