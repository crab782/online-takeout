package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.FinanceDataVO;
import com.test.takeout.vo.PageResult;
import com.test.takeout.vo.WithdrawalRecordVO;
import com.test.takeout.vo.WithdrawalTrendVO;
import com.test.takeout.vo.CashFlowTrendVO;
import com.test.takeout.dto.WithdrawalProcessDTO;

public interface FinanceService {
    
    FinanceDataVO getFinanceData(String month, Integer page, Integer pageSize);
    
    PageResult<WithdrawalRecordVO> getWithdrawalRecords(Integer page, Integer pageSize, String search, String status);
    
    List<WithdrawalTrendVO> getWithdrawalTrend();
    
    List<CashFlowTrendVO> getCashFlowTrend();
    
    void processWithdrawal(WithdrawalProcessDTO processDTO);

}
