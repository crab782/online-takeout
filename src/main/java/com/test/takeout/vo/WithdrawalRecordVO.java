package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawalRecordVO {
    private Long id;
    private Long shopId;
    private String shopName;
    private BigDecimal amount;
    private String status;
    private Date applyTime;
    private Date processTime;
    private String accountType;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private String remark;
}
