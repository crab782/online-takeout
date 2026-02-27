package com.test.takeout.service.impl;

import com.test.takeout.entity.StoreBalance;
import com.test.takeout.entity.StoreWithdrawalAccount;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.mapper.StoreBalanceMapper;
import com.test.takeout.mapper.StoreWithdrawalAccountMapper;
import com.test.takeout.mapper.WithdrawalRecordMapper;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.service.StoreWithdrawalAccountService;
import com.test.takeout.service.WithdrawalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WithdrawalRecordServiceImplTest {

    @Mock
    private WithdrawalRecordMapper withdrawalRecordMapper;

    @Mock
    private StoreBalanceService storeBalanceService;

    @Mock
    private StoreWithdrawalAccountService storeWithdrawalAccountService;

    @InjectMocks
    private WithdrawalRecordServiceImpl withdrawalRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 使用反射设置baseMapper，解决NullPointerException问题
        try {
            java.lang.reflect.Field field = com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.class.getDeclaredField("baseMapper");
            field.setAccessible(true);
            field.set(withdrawalRecordService, withdrawalRecordMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSubmitWithdrawal_Success() {
        // 准备测试数据
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setStoreId(1L);
        withdrawalRecord.setAmount(new BigDecimal(100));
        
        StoreBalance storeBalance = new StoreBalance();
        storeBalance.setStoreId(1L);
        storeBalance.setBalance(new BigDecimal(200));
        storeBalance.setWithdrawnAmount(new BigDecimal(0));
        
        // 模拟方法调用
        when(storeBalanceService.getByStoreId(1L)).thenReturn(storeBalance);
        when(withdrawalRecordMapper.insert(withdrawalRecord)).thenReturn(1);
        when(storeBalanceService.updateById(storeBalance)).thenReturn(true);
        
        // 执行测试
        boolean result = withdrawalRecordService.submitWithdrawal(withdrawalRecord);
        
        // 验证结果
        assertTrue(result);
        assertEquals(0, withdrawalRecord.getStatus());
        assertNotNull(withdrawalRecord.getApplyTime());
        verify(storeBalanceService, times(1)).getByStoreId(1L);
        verify(withdrawalRecordMapper, times(1)).insert(withdrawalRecord);
        verify(storeBalanceService, times(1)).updateById(storeBalance);
    }

    @Test
    void testSubmitWithdrawal_InsufficientBalance() {
        // 准备测试数据
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setStoreId(1L);
        withdrawalRecord.setAmount(new BigDecimal(300));
        
        StoreBalance storeBalance = new StoreBalance();
        storeBalance.setStoreId(1L);
        storeBalance.setBalance(new BigDecimal(200));
        
        // 模拟方法调用
        when(storeBalanceService.getByStoreId(1L)).thenReturn(storeBalance);
        
        // 执行测试
        boolean result = withdrawalRecordService.submitWithdrawal(withdrawalRecord);
        
        // 验证结果
        assertFalse(result);
        verify(storeBalanceService, times(1)).getByStoreId(1L);
        verify(withdrawalRecordMapper, never()).insert(any());
    }

    @Test
    void testSubmitWithdrawal_StoreBalanceNotFound() {
        // 准备测试数据
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setStoreId(1L);
        withdrawalRecord.setAmount(new BigDecimal(100));
        
        // 模拟方法调用
        when(storeBalanceService.getByStoreId(1L)).thenReturn(null);
        
        // 执行测试
        boolean result = withdrawalRecordService.submitWithdrawal(withdrawalRecord);
        
        // 验证结果
        assertFalse(result);
        verify(storeBalanceService, times(1)).getByStoreId(1L);
        verify(withdrawalRecordMapper, never()).insert(any());
    }

    @Test
    void testGetWithdrawalRecordsByStoreId() {
        // 准备测试数据
        Long storeId = 1L;
        Integer page = 1;
        Integer pageSize = 10;
        
        // 执行测试
        withdrawalRecordService.getWithdrawalRecordsByStoreId(storeId, page, pageSize);
        
        // 验证结果
        verify(withdrawalRecordMapper, times(1)).selectPage(any(), any());
    }
}
