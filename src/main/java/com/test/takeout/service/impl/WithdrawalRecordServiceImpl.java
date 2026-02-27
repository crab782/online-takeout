package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.entity.StoreBalance;
import com.test.takeout.mapper.WithdrawalRecordMapper;
import com.test.takeout.service.WithdrawalRecordService;
import com.test.takeout.service.StoreBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class WithdrawalRecordServiceImpl extends ServiceImpl<WithdrawalRecordMapper, WithdrawalRecord> implements WithdrawalRecordService {
    
    @Autowired
    private StoreBalanceService storeBalanceService;
    
    /**
     * 提交提现申请
     * @param withdrawalRecord 提现记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean submitWithdrawal(WithdrawalRecord withdrawalRecord) {
        // 获取店铺余额
        StoreBalance storeBalance = storeBalanceService.getByStoreId(withdrawalRecord.getStoreId());
        if (storeBalance == null) {
            return false;
        }
        
        // 检查余额是否足够
        BigDecimal balance = storeBalance.getBalance();
        BigDecimal amount = withdrawalRecord.getAmount();
        if (balance.compareTo(amount) < 0) {
            return false;
        }
        
        // 创建提现记录
        withdrawalRecord.setStatus(0); // 0: 待处理
        withdrawalRecord.setApplyTime(java.time.LocalDateTime.now()); // 设置申请时间
        boolean saveSuccess = this.save(withdrawalRecord);
        if (!saveSuccess) {
            return false;
        }
        
        // 更新店铺余额
        BigDecimal newBalance = balance.subtract(amount);
        storeBalance.setBalance(newBalance);
        return storeBalanceService.updateById(storeBalance);
    }
    
    /**
     * 根据店铺ID获取提现记录
     * @param storeId 店铺ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 提现记录分页数据
     */
    @Override
    public IPage<WithdrawalRecord> getWithdrawalRecordsByStoreId(Long storeId, Integer page, Integer pageSize) {
        IPage<WithdrawalRecord> iPage = new Page<>(page, pageSize);
        
        LambdaQueryWrapper<WithdrawalRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WithdrawalRecord::getStoreId, storeId);
        queryWrapper.orderByDesc(WithdrawalRecord::getApplyTime);
        
        return this.page(iPage, queryWrapper);
    }
}
