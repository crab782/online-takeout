package com.test.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.takeout.entity.WithdrawalRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface WithdrawalRecordService extends IService<WithdrawalRecord> {
    /**
     * 分页查询提现记录
     */
    IPage<WithdrawalRecord> getWithdrawalRecordsByStoreId(Long storeId, int page, int pageSize);
    
    /**
     * 提交提现申请
     */
    boolean submitWithdrawal(WithdrawalRecord withdrawalRecord);
    
    /**
     * 处理提现申请
     */
    boolean processWithdrawal(Long id, int status, String remark);
}
