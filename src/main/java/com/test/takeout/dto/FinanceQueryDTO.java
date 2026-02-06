package com.test.takeout.dto;
import lombok.Data;

@Data
public class FinanceQueryDTO {
    private String month;
    private Integer page;
    private Integer pageSize;
}
