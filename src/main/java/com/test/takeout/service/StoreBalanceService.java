package com.test.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.takeout.entity.StoreBalance;

public interface StoreBalanceService extends IService<StoreBalance> {
    /**
     * 根据店铺ID获取余额信息
     */
    StoreBalance getByStoreId(Long storeId);
    
    /**
     * 增加店铺余额
     */
    boolean addBalance(Long storeId, java.math.BigDecimal amount);
    
    /**
     * 减少店铺余额
     */
    boolean reduceBalance(Long storeId, java.math.BigDecimal amount);
    
    /**
     * 初始化店铺余额
     */
    boolean initStoreBalance(Long storeId);
}
