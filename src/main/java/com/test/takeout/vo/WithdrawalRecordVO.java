package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WithdrawalRecordVO {
    private Long id;
    private Long shopId;
    private String shopName;
    private BigDecimal amount;
    private String status;
    private LocalDateTime applyTime;
    private LocalDateTime processTime;
    private String accountType;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private String remark;
}
