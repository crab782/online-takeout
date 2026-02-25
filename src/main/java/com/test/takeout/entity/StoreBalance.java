package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("store_balance")
public class StoreBalance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private BigDecimal balance;
    private BigDecimal totalRevenue;
    private BigDecimal withdrawnAmount;
    private LocalDateTime lastUpdateTime;
    private LocalDateTime createTime;
}
