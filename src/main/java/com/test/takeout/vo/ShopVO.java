package com.test.takeout.vo;

import lombok.Data;

@Data
public class ShopVO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private Double rating;
    private Integer sales;
    private String logo;
    private Boolean isOpen;
}
