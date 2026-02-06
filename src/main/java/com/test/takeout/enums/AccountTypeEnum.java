package com.test.takeout.enums;

public enum AccountTypeEnum {
    ALIPAY("alipay", "支付宝"),
    WECHAT("wechat", "微信"),
    BANK("bank", "银行卡");

    private final String code;
    private final String description;

    AccountTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AccountTypeEnum getByCode(String code) {
        for (AccountTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
