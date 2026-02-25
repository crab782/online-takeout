package com.test.takeout.controller;

import com.test.takeout.entity.StoreBalance;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.service.StoreBalanceService;
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

    /**
     * 获取账号信息
     * @return 账号信息
     */
    @GetMapping("/account")
    public Map<String, Object> getAccountInfo() {
        // 这里可以从数据库获取账号信息，暂时返回空数据
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("msg", "success");
        result.put("data", new HashMap<>());
        return result;
    }

    /**
     * 保存账号信息
     * @param accountInfo 账号信息
     * @return 保存结果
     */
    @PostMapping("/account")
    public Map<String, Object> saveAccountInfo(@RequestBody Map<String, Object> accountInfo) {
        // 这里可以保存账号信息到数据库
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("msg", "账号绑定成功");
        return result;
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
        // 这里可以从数据库获取提现记录，暂时返回空数据
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("msg", "success");
        
        Map<String, Object> data = new HashMap<>();
        data.put("list", List.of());
        data.put("total", 0);
        result.put("data", data);
        
        return result;
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
            Double amountDouble = (Double) withdrawalInfo.get("amount");
            Long storeId = ((Number) withdrawalInfo.get("storeId")).longValue();
            
            // 将Double转换为BigDecimal
            java.math.BigDecimal amount = java.math.BigDecimal.valueOf(amountDouble);

            log.info("申请提现：storeId={}, amount={}", storeId, amount);

            // 验证金额
            if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
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
            withdrawalRecord.setStatus(0); // 0-申请中
            withdrawalRecord.setBankName("中国银行"); // 暂时固定，实际应该从账号信息中获取
            withdrawalRecord.setBankAccount("6222021234567890123"); // 暂时固定
            withdrawalRecord.setAccountName("测试账户"); // 暂时固定
            withdrawalRecord.setApplyTime(LocalDateTime.now());

            // 保存提现记录
            withdrawalRecordService.save(withdrawalRecord);

            // 减少店铺余额
            storeBalanceService.reduceBalance(storeId, amount);

            log.info("提现申请成功：withdrawalId={}, storeId={}, amount={}", withdrawalRecord.getId(), storeId, amount);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("msg", "提现申请成功");
            return result;

        } catch (Exception e) {
            log.error("提现申请失败：", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "提现申请失败，请稍后重试");
            return result;
        }
    }
}
