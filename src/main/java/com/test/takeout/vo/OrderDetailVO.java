package com.test.takeout.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDetailVO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private Integer number;
}
