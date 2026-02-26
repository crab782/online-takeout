package com.test.takeout.controller;

import com.test.takeout.entity.StoreBalance;
import com.test.takeout.entity.StoreWithdrawalAccount;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.service.StoreWithdrawalAccountService;
import com.test.takeout.service.WithdrawalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/withdrawal")
@Slf4j
public class WithdrawalController {

    @Autowired
    private StoreBalanceService storeBalanceService;

    @Autowired
    private WithdrawalRecordService withdrawalRecordService;
    
    @Autowired
    private StoreWithdrawalAccountService storeWithdrawalAccountService;

    /**
     * 获取账号信息
     * @return 账号信息
     */
    @GetMapping("/account")
    public Map<String, Object> getAccountInfo() {
        try {
            // 从数据库获取账号信息
            // 这里假设通过店铺ID获取，实际应该从当前登录用户或会话中获取店铺ID
            Long storeId = 1L; // 临时固定，实际应该从登录信息中获取
            StoreWithdrawalAccount account = storeWithdrawalAccountService.getByStoreId(storeId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("msg", "success");
            result.put("data", account != null ? account : new HashMap<>());
            return result;
        } catch (Exception e) {
            log.error("获取账号信息失败：", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "获取账号信息失败");
            result.put("data", new HashMap<>());
            return result;
        }
    }

    /**
     * 保存账号信息
     * @param accountInfo 账号信息
     * @return 保存结果
     */
    @PostMapping("/account")
    public Map<String, Object> saveAccountInfo(@RequestBody Map<String, Object> accountInfo) {
        try {
            // 保存账号信息到数据库
            // 这里假设通过店铺ID保存，实际应该从当前登录用户或会话中获取店铺ID
            Long storeId = 1L; // 临时固定，实际应该从登录信息中获取
            
            StoreWithdrawalAccount account = storeWithdrawalAccountService.getByStoreId(storeId);
            if (account == null) {
                account = new StoreWithdrawalAccount();
                account.setStoreId(storeId);
            }
            
            // 设置账号信息
            account.setAccountType((Integer) accountInfo.get("accountType"));
            account.setAccountName((String) accountInfo.get("accountName"));
            account.setAccountNumber((String) accountInfo.get("accountNumber"));
            account.setBankName((String) accountInfo.get("bankName"));
            account.setBankBranch((String) accountInfo.get("bankBranch"));
            account.setRemark((String) accountInfo.get("remark"));
            
            // 保存到数据库
            boolean success = storeWithdrawalAccountService.saveOrUpdate(account);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 1);
                result.put("msg", "账号绑定成功");
            } else {
                result.put("code", 0);
                result.put("msg", "账号绑定失败");
            }
            return result;
        } catch (Exception e) {
            log.error("保存账号信息失败：", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "账号绑定失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 获取提现记录
     * @param page 当前页码
     * @param pageSize 每页大小
     * @return 提现记录列表
     */
    @GetMapping("/records")
    public Map<String, Object> getWithdrawalRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            // 从数据库获取提现记录
            // 这里假设通过店铺ID获取，实际应该从当前登录用户或会话中获取店铺ID
            Long storeId = 1L; // 临时固定，实际应该从登录信息中获取
            
            // 使用分页查询获取提现记录
            com.baomidou.mybatisplus.core.metadata.IPage<WithdrawalRecord> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WithdrawalRecord> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            queryWrapper.eq(WithdrawalRecord::getStoreId, storeId);
            queryWrapper.orderByDesc(WithdrawalRecord::getApplyTime);
            
            com.baomidou.mybatisplus.core.metadata.IPage<WithdrawalRecord> records = withdrawalRecordService.page(iPage, queryWrapper);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("msg", "success");
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", records.getRecords());
            data.put("total", records.getTotal());
            result.put("data", data);
            
            return result;
        } catch (Exception e) {
            log.error("获取提现记录失败：", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "获取提现记录失败");
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", List.of());
            data.put("total", 0);
            result.put("data", data);
            
            return result;
        }
    }

    /**
     * 申请提现
     * @param withdrawalInfo 提现信息
     * @return 提现申请结果
     */
    @PostMapping("/apply")
    public Map<String, Object> applyWithdrawal(@RequestBody Map<String, Object> withdrawalInfo) {
        try {
            // 获取提现金额和店铺ID
            Number amountNumber = (Number) withdrawalInfo.get("amount");
            if (amountNumber == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "提现金额不能为空");
                return result;
            }
            Long storeId = ((Number) withdrawalInfo.get("storeId")).longValue();
            
            // 将Number转换为BigDecimal
            java.math.BigDecimal amount = new java.math.BigDecimal(amountNumber.toString());

            log.info("申请提现：storeId={}, amount={}", storeId, amount);

            // 验证金额
            if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "提现金额必须大于0");
                return result;
            }

            // 获取店铺余额
            StoreBalance storeBalance = storeBalanceService.getByStoreId(storeId);
            if (storeBalance == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "店铺余额信息不存在");
                return result;
            }

            // 验证余额是否充足
            if (storeBalance.getBalance().compareTo(amount) < 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "余额不足");
                return result;
            }

            // 创建提现记录
            WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
            withdrawalRecord.setStoreId(storeId);
            withdrawalRecord.setAmount(amount);
            withdrawalRecord.setStatus(1); // 1-直接成功，不需要审核
            
            // 从数据库获取提现账户信息
            StoreWithdrawalAccount account = storeWithdrawalAccountService.getByStoreId(storeId);
            if (account != null) {
                withdrawalRecord.setBankName(account.getBankName());
                withdrawalRecord.setBankAccount(account.getAccountNumber());
                withdrawalRecord.setAccountName(account.getAccountName());
            } else {
                // 如果没有账户信息，使用默认值
                withdrawalRecord.setBankName("中国银行");
                withdrawalRecord.setBankAccount("6222021234567890123");
                withdrawalRecord.setAccountName("测试账户");
            }
            
            withdrawalRecord.setApplyTime(LocalDateTime.now());
            withdrawalRecord.setProcessTime(LocalDateTime.now()); // 直接处理完成

            // 保存提现记录
            boolean saveSuccess = withdrawalRecordService.save(withdrawalRecord);
            if (!saveSuccess) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "保存提现记录失败");
                return result;
            }

            // 减少店铺余额
            boolean reduceSuccess = storeBalanceService.reduceBalance(storeId, amount);
            if (!reduceSuccess) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "更新余额失败");
                return result;
            }

            log.info("提现申请成功：withdrawalId={}, storeId={}, amount={}", withdrawalRecord.getId(), storeId, amount);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("msg", "提现申请成功");
            return result;

        } catch (Exception e) {
            log.error("提现申请失败：", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "提现申请失败，请稍后重试: " + e.getMessage());
            return result;
        }
    }
}
