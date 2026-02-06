package com.test.takeout.vo;
import lombok.Data;

@Data
public class CartVO {
    private Long id;
    private Long dishId;
    private Long setmealId;
    private String name;
    private String description;
    private String image;
    private Double price;
    private Integer number;
}
