package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinanceItemVO {
    private Long shopId;
    private String shopName;
    private BigDecimal revenue;
    private BigDecimal withdrawal;
    private Integer orderCount;
}
