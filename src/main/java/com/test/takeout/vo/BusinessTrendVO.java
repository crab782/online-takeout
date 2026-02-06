package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BusinessTrendVO {
    private Date date;
    private Integer orderCount;
    private BigDecimal turnover;
    private BigDecimal avgOrderAmount;
}
