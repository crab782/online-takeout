package com.test.takeout.dto;
import lombok.Data;

@Data
public class WithdrawalQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String search;
    private String status;
}
