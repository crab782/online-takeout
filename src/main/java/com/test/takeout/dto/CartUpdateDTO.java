package com.test.takeout.dto;
import lombok.Data;

@Data
public class CartUpdateDTO {
    private Long dishId;
    private Long setmealId;
    private Integer number;
}
