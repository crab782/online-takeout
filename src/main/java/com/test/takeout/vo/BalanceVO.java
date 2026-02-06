package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceVO {
    private BigDecimal balance;
    private BigDecimal totalRecharge;
    private BigDecimal totalConsume;
}
