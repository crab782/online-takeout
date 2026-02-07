package com.test.takeout.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdersSubmitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long addressBookId;

    private BigDecimal amount;

    private List<OrderDetailDTO> orderDetails;

    @Data
    public static class OrderDetailDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long dishId;

        private Long setmealId;

        private Integer number;

        private BigDecimal amount;
    }
}
