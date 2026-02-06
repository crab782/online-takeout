package com.test.takeout.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class WithdrawalApplyDTO {
    private Long shopId;
    private BigDecimal amount;
}
