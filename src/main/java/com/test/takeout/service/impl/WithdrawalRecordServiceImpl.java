package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.mapper.WithdrawalRecordMapper;
import com.test.takeout.service.WithdrawalRecordService;
import com.test.takeout.service.StoreBalanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Service
public class WithdrawalRecordServiceImpl extends ServiceImpl<WithdrawalRecordMapper, WithdrawalRecord> implements WithdrawalRecordService {
    
    @Autowired
    private StoreBalanceService storeBalanceService;
    
    @Override
    public IPage<WithdrawalRecord> getWithdrawalRecordsByStoreId(Long storeId, int page, int pageSize) {
        Page<WithdrawalRecord> withdrawalPage = new Page<>(page, pageSize);
        return this.baseMapper.selectPage(
            withdrawalPage,
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WithdrawalRecord>()
                .eq(WithdrawalRecord::getStoreId, storeId)
                .orderByDesc(WithdrawalRecord::getApplyTime)
        );
    }
    
    @Override
    @Transactional
    public boolean submitWithdrawal(WithdrawalRecord withdrawalRecord) {
        // 检查余额是否足够
        boolean success = storeBalanceService.reduceBalance(
            withdrawalRecord.getStoreId(),
            withdrawalRecord.getAmount()
        );
        if (success) {
            // 保存提现记录
            withdrawalRecord.setStatus(0); // 0-申请中
            withdrawalRecord.setApplyTime(LocalDateTime.now());
            return this.save(withdrawalRecord);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean processWithdrawal(Long id, int status, String remark) {
        WithdrawalRecord record = this.getById(id);
        if (record != null) {
            record.setStatus(status);
            record.setProcessTime(LocalDateTime.now());
            record.setRemark(remark);
            return this.updateById(record);
        }
        return false;
    }
}
