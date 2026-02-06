package com.test.takeout.dto;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Double price;
    private Integer number;
}
