package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceDataVO {
    private List<FinanceItemVO> list;
    private Integer total;
    private BigDecimal totalRevenue;
    private BigDecimal totalRevenueChange;
    private Integer shopCount;
    private BigDecimal averageRevenue;
    private BigDecimal totalWithdrawal;
    private BigDecimal withdrawalChange;
    private Integer withdrawalCount;
    private BigDecimal platformCashFlow;
    private BigDecimal cashFlowChange;
}
