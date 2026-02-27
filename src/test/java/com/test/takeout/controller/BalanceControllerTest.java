package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.StoreBalance;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.service.WithdrawalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceControllerTest {

    @Mock
    private StoreBalanceService storeBalanceService;

    @Mock
    private WithdrawalRecordService withdrawalRecordService;

    @InjectMocks
    private BalanceController balanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalanceInfo_Success() {
        // 准备测试数据
        Long shopId = 1L;
        StoreBalance storeBalance = new StoreBalance();
        storeBalance.setId(1L);
        storeBalance.setStoreId(shopId);
        storeBalance.setBalance(new BigDecimal(1000));
        storeBalance.setTotalRevenue(new BigDecimal(5000));
        storeBalance.setWithdrawnAmount(new BigDecimal(4000));
        
        // 模拟方法调用
        when(storeBalanceService.getByStoreId(shopId)).thenReturn(storeBalance);
        
        // 执行测试
        R<StoreBalance> result = balanceController.getBalanceInfo(shopId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("success", result.getMsg());
        assertEquals(storeBalance, result.getData());
        verify(storeBalanceService, times(1)).getByStoreId(shopId);
    }

    @Test
    void testSubmitWithdrawal_Success() {
        // 准备测试数据
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setStoreId(1L);
        withdrawalRecord.setAmount(new BigDecimal(500));
        
        // 模拟方法调用
        when(withdrawalRecordService.submitWithdrawal(withdrawalRecord)).thenReturn(true);
        
        // 执行测试
        R<String> result = balanceController.submitWithdrawal(withdrawalRecord);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("提现申请提交成功", result.getMsg());
        verify(withdrawalRecordService, times(1)).submitWithdrawal(withdrawalRecord);
    }

    @Test
    void testSubmitWithdrawal_InsufficientBalance() {
        // 准备测试数据
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setStoreId(1L);
        withdrawalRecord.setAmount(new BigDecimal(500));
        
        // 模拟方法调用
        when(withdrawalRecordService.submitWithdrawal(withdrawalRecord)).thenReturn(false);
        
        // 执行测试
        R<String> result = balanceController.submitWithdrawal(withdrawalRecord);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("提现申请提交失败，余额不足", result.getMsg());
        verify(withdrawalRecordService, times(1)).submitWithdrawal(withdrawalRecord);
    }
}
