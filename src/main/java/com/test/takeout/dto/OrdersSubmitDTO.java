package com.test.takeout.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdersSubmitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long addressBookId;

    private Long storeId;

    private String storeName;

    private BigDecimal amount;

    private Integer totalQuantity;

    private List<OrderDetailDTO> orderDetails;

    @Data
    public static class OrderDetailDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;

        private String name;

        private String description;

        private BigDecimal price;

        private Long dishId;

        private Long setmealId;

        private Integer number;

        private BigDecimal amount;

        private String image;
    }
}
