package com.test.takeout.service;

import com.test.takeout.vo.PageResponseVO;
import com.test.takeout.entity.Withdrawal;
import com.test.takeout.dto.WithdrawalApplyDTO;

public interface WithdrawalService {
    
    void applyWithdrawal(WithdrawalApplyDTO applyDTO);
    
    PageResponseVO<Withdrawal> getWithdrawalList(Long shopId, Integer page, Integer pageSize);

}
