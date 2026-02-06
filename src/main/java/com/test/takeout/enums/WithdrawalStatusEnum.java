package com.test.takeout.enums;

public enum WithdrawalStatusEnum {
    PENDING("pending", "待处理"),
    APPROVED("approved", "已批准"),
    REJECTED("rejected", "已拒绝"),
    COMPLETED("completed", "已完成");

    private final String code;
    private final String description;

    WithdrawalStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static WithdrawalStatusEnum getByCode(String code) {
        for (WithdrawalStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }














}