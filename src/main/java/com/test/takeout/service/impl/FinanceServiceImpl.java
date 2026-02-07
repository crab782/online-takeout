package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.service.FinanceService;
import com.test.takeout.vo.FinanceDataVO;
import com.test.takeout.vo.PageResult;
import com.test.takeout.vo.WithdrawalRecordVO;
import com.test.takeout.vo.WithdrawalTrendVO;
import com.test.takeout.vo.CashFlowTrendVO;
import com.test.takeout.dto.WithdrawalProcessDTO;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.mapper.WithdrawalRecordMapper;
import org.springframework.beans.BeanUtils;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceServiceImpl implements FinanceService {
    
    @Resource
    private WithdrawalRecordMapper withdrawalRecordMapper;
    
    @Override
    public FinanceDataVO getFinanceData(String month, Integer page, Integer pageSize) {
        // TODO: 实现获取财务数据的逻辑
        FinanceDataVO financeData = new FinanceDataVO();
        financeData.setTotalRevenue(BigDecimal.ZERO);
        financeData.setTotalWithdrawal(BigDecimal.ZERO);
        financeData.setShopCount(0);
        return financeData;
    }
    
    @Override
    public PageResult<WithdrawalRecordVO> getWithdrawalRecords(Integer page, Integer pageSize, String search, String status) {
        Page<WithdrawalRecord> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<WithdrawalRecord> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加搜索条件
        if (search != null && !search.isEmpty()) {
            queryWrapper.like(WithdrawalRecord::getShopName, search)
                       .or()
                       .like(WithdrawalRecord::getAccountName, search);
        }
        
        // 添加状态筛选
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq(WithdrawalRecord::getStatus, status);
        }
        
        queryWrapper.orderByDesc(WithdrawalRecord::getApplyTime);
        
        Page<WithdrawalRecord> withdrawalPage = withdrawalRecordMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为VO
        List<WithdrawalRecordVO> records = withdrawalPage.getRecords().stream()
                .map(record -> {
                    WithdrawalRecordVO vo = new WithdrawalRecordVO();
                    BeanUtils.copyProperties(record, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        
        PageResult<WithdrawalRecordVO> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(withdrawalPage.getTotal());
        result.setCurrent(withdrawalPage.getCurrent());
        result.setSize(withdrawalPage.getSize());
        
        return result;
    }
    
    @Override
    public List<WithdrawalTrendVO> getWithdrawalTrend() {
        // TODO: 实现获取提现趋势的逻辑
        return List.of();
    }
    
    @Override
    public List<CashFlowTrendVO> getCashFlowTrend() {
        // TODO: 实现获取现金流趋势的逻辑
        return List.of();
    }
    
    @Override
    public void processWithdrawal(WithdrawalProcessDTO processDTO) {
        WithdrawalRecord record = withdrawalRecordMapper.selectById(processDTO.getRecordId());
        if (record != null) {
            record.setStatus(processDTO.getStatus());
            record.setProcessTime(LocalDateTime.now());
            record.setRemark(processDTO.getRemark());
            withdrawalRecordMapper.updateById(record);
        }
    }
}
