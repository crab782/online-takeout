package com.test.takeout.service;

import com.test.takeout.entity.StoreWithdrawalAccount;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StoreWithdrawalAccountService extends IService<StoreWithdrawalAccount> {
    /**
     * 根据店铺ID获取提现账户信息
     * @param storeId 店铺ID
     * @return 提现账户信息
     */
    StoreWithdrawalAccount getByStoreId(Long storeId);
}