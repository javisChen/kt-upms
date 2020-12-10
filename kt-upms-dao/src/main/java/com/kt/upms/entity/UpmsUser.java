package com.kt.upms.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.StringJoiner;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpmsUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @TableField("name")
//    @JSONField(name = "NAME")
    private String name;

    /**
     * 手机号码
     */
//    @NotBlank(message = "phone 不能为空")
//    @Phone(message = "phone 不合法")
    @TableField("phone")
    private String phone;

    /**
     * 用户密码
     */
//    @NotBlank(message = "password 不能为空")
    @TableField("password")
    private String password;

    /**
     * 用户状态：1-正常；2-锁定；
     */
    @TableField("status")
//    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private Integer status;

    public enum StatusEnum {
        NORMAL(1, "正常"),
        LOCKED(2, "锁定");

        StatusEnum(int value, String desc) {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", UpmsUser.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("phone='" + phone + "'")
                .add("password='" + password + "'")
                .add("status=" + status)
                .toString();
    }
}
