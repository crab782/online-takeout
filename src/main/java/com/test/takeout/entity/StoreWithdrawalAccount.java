package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("store_withdrawal_account")
public class StoreWithdrawalAccount {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private Integer accountType; // 1-银行卡，2-支付宝，3-微信
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankBranch;
    private String remark;
}