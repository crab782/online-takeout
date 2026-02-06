package com.test.takeout.enums;

public enum CategoryStatusEnum {
    ACTIVE("active", "启用"),
    INACTIVE("inactive", "禁用");

    private final String code;
    private final String description;

    CategoryStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CategoryStatusEnum getByCode(String code) {
        for (CategoryStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}