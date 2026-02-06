package com.test.takeout.enums;

public enum GenderEnum {
    MALE("male", "男"),
    FEMALE("female", "女"),
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    GenderEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static GenderEnum getByCode(String code) {
        for (GenderEnum gender : values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        return null;
    }
}
