package com.test.takeout.enums;

public enum ProductStatusEnum {
    AVAILABLE("available", "可售"),
    UNAVAILABLE("unavailable", "不可售"),
    OUT_OF_STOCK("out_of_stock", "缺货");

    private final String code;
    private final String description;

    ProductStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductStatusEnum getByCode(String code) {
        for (ProductStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}