package com.test.takeout.enums;

public enum CommentStatusEnum {
    ACTIVE("active", "正常"),
    HIDDEN("hidden", "隐藏"),
    DELETED("deleted", "已删除");

    private final String code;
    private final String description;

    CommentStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CommentStatusEnum getByCode(String code) {
        for (CommentStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
