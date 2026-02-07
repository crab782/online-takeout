package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.test.takeout.service.WithdrawalService;
import com.test.takeout.vo.PageResponseVO;
import com.test.takeout.entity.Withdrawal;
import com.test.takeout.dto.WithdrawalApplyDTO;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    
    @Override
    public void applyWithdrawal(WithdrawalApplyDTO applyDTO) {
        // TODO: 实现申请提现的逻辑
    }
    
    @Override
    public PageResponseVO<Withdrawal> getWithdrawalList(Long shopId, Integer page, Integer pageSize) {
        // TODO: 实现获取提现列表的逻辑
        return null;
    }

}
