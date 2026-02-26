package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.test.takeout.common.R;
import com.test.takeout.entity.StoreBalance;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.service.WithdrawalRecordService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/backend/balance")
public class BalanceController {
    
    @Autowired
    private StoreBalanceService storeBalanceService;
    
    @Autowired
    private WithdrawalRecordService withdrawalRecordService;
    
    /**
     * 获取店铺余额信息
     */
    @GetMapping("/info")
    public R<StoreBalance> getBalanceInfo(@RequestParam("shopId") Long shopId) {
        StoreBalance balance = storeBalanceService.getByStoreId(shopId);
        return R.success(balance);
    }
    
    /**
     * 提交提现申请
     */
    @PostMapping("/withdrawal")
    public R<String> submitWithdrawal(@RequestBody WithdrawalRecord withdrawalRecord) {
        boolean success = withdrawalRecordService.submitWithdrawal(withdrawalRecord);
        if (success) {
            return R.success("提现申请提交成功");
        } else {
            return R.error("提现申请提交失败，余额不足");
        }
    }
    
    /**
     * 获取提现记录
     */
    @GetMapping("/withdrawal/records")
    public R<IPage<WithdrawalRecord>> getWithdrawalRecords(
            @RequestParam("shopId") Long shopId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<WithdrawalRecord> records = withdrawalRecordService.getWithdrawalRecordsByStoreId(shopId, page, pageSize);
        return R.success(records);
    }
    
    /**
     * 获取近30天可提现金额趋势
     */
    @GetMapping("/trend")
    public R<Map<String, Object>> getBalanceTrend(@RequestParam("shopId") Long shopId) {
        // 这里可以实现获取近30天余额趋势的逻辑
        // 暂时返回空数据，前端可以根据需要调整
        Map<String, Object> trendData = new HashMap<>();
        return R.success(trendData);
    }
}
