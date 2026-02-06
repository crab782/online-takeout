package com.test.takeout.enums;

public enum UserStatusEnum {
    ACTIVE("active", "正常"),
    INACTIVE("inactive", "禁用"),
    LOCKED("locked", "锁定");

    private final String code;
    private final String description;

    UserStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserStatusEnum getByCode(String code) {
        for (UserStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
