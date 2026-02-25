package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.StoreBalance;
import com.test.takeout.mapper.StoreBalanceMapper;
import com.test.takeout.service.StoreBalanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class StoreBalanceServiceImpl extends ServiceImpl<StoreBalanceMapper, StoreBalance> implements StoreBalanceService {
    
    @Override
    public StoreBalance getByStoreId(Long storeId) {
        StoreBalance balance = this.baseMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<StoreBalance>()
                .eq(StoreBalance::getStoreId, storeId)
        );
        if (balance == null) {
            // 如果余额记录不存在，初始化一个
            initStoreBalance(storeId);
            balance = getByStoreId(storeId);
        }
        return balance;
    }
    
    @Override
    @Transactional
    public boolean addBalance(Long storeId, BigDecimal amount) {
        StoreBalance balance = getByStoreId(storeId);
        if (balance != null) {
            balance.setBalance(balance.getBalance().add(amount));
            balance.setTotalRevenue(balance.getTotalRevenue().add(amount));
            return this.updateById(balance);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean reduceBalance(Long storeId, BigDecimal amount) {
        StoreBalance balance = getByStoreId(storeId);
        if (balance != null && balance.getBalance().compareTo(amount) >= 0) {
            balance.setBalance(balance.getBalance().subtract(amount));
            balance.setWithdrawnAmount(balance.getWithdrawnAmount().add(amount));
            return this.updateById(balance);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean initStoreBalance(Long storeId) {
        StoreBalance balance = new StoreBalance();
        balance.setStoreId(storeId);
        balance.setBalance(BigDecimal.ZERO);
        balance.setTotalRevenue(BigDecimal.ZERO);
        balance.setWithdrawnAmount(BigDecimal.ZERO);
        return this.save(balance);
    }
}
