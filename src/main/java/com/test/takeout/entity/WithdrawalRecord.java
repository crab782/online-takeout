package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("withdrawal_record")
public class WithdrawalRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private BigDecimal amount;
    private Integer status;
    private String bankName;
    private String bankAccount;
    private String accountName;
    private LocalDateTime applyTime;
    private LocalDateTime processTime;
    private String remark;
}
