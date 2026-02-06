package com.test.takeout.dto;
import lombok.Data;

@Data
public class WithdrawalProcessDTO {
    private Long id;
    private String status;
    private String remark;
}
