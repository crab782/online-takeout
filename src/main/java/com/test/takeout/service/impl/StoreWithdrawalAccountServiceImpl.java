package com.test.takeout.service.impl;

import com.test.takeout.entity.StoreWithdrawalAccount;
import com.test.takeout.mapper.StoreWithdrawalAccountMapper;
import com.test.takeout.service.StoreWithdrawalAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

@Service
public class StoreWithdrawalAccountServiceImpl extends ServiceImpl<StoreWithdrawalAccountMapper, StoreWithdrawalAccount> implements StoreWithdrawalAccountService {
    
    /**
     * 根据店铺ID获取提现账户信息
     * @param storeId 店铺ID
     * @return 提现账户信息
     */
    @Override
    public StoreWithdrawalAccount getByStoreId(Long storeId) {
        return this.baseMapper.selectOne(
            new LambdaQueryWrapper<StoreWithdrawalAccount>()
                .eq(StoreWithdrawalAccount::getStoreId, storeId)
        );
    }
}