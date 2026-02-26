package com.test.takeout.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.test.takeout.entity.WithdrawalRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface WithdrawalRecordService extends IService<WithdrawalRecord> {
    /**
     * 提交提现申请
     * @param withdrawalRecord 提现记录
     * @return 是否成功
     */
    boolean submitWithdrawal(WithdrawalRecord withdrawalRecord);
    
    /**
     * 根据店铺ID获取提现记录
     * @param storeId 店铺ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 提现记录分页数据
     */
    IPage<WithdrawalRecord> getWithdrawalRecordsByStoreId(Long storeId, Integer page, Integer pageSize);
}
