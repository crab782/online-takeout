package com.test.takeout.dto;

import lombok.Data;

@Data
public class OrderCancelDTO {
    private Long orderId;
    private String reason;
}
