package com.test.takeout.enums;

public enum ShopStatusEnum {
    ACTIVE("active", "营业中"),
    INACTIVE("inactive", "未营业"),
    CLOSED("closed", "已关闭");

    private final String code;
    private final String description;

    ShopStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ShopStatusEnum getByCode(String code) {
        for (ShopStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
