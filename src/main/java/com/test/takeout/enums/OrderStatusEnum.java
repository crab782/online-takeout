package com.test.takeout.enums;

public enum OrderStatusEnum {
    PENDING("pending", "待付款"),
    PAY_SUCCESS("pay_success", "支付成功"),
    ACCEPTED("accepted", "商家接单"),
    PREPARING("preparing", "商家出餐"),
    RIDER_ACCEPTED("rider_accepted", "骑手接单"),
    DELIVERING("delivering", "配送中"),
    DELIVERED("delivered", "待收货"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String description;

    OrderStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatusEnum getByCode(String code) {
        for (OrderStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
