package com.kt.upms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatusEnum {
        NORMAL(1, "正常"),
        LOCKED(2, "锁定");

        UserStatusEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @EnumValue
        private int value;
        private String desc;

        public int getValue() {
            return value;
        }
    }