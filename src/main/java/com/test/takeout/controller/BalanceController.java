package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.entity.StoreBalance;
import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.service.EmployeeService;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.service.WithdrawalRecordService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/backend/balance")
public class BalanceController {
    
    @Autowired
    private StoreBalanceService storeBalanceService;
    
    @Autowired
    private WithdrawalRecordService withdrawalRecordService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * 获取当前登录用户的店铺ID
     */
    private Long getCurrentStoreId() {
        // 首先尝试从请求属性中获取storeId
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId != null) {
            return storeId;
        }
        
        // 如果没有storeId，尝试从userId查询员工信息获取storeId
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            Employee employee = employeeService.getById(userId);
            if (employee != null) {
                storeId = employee.getStoreId();
                // 将storeId存入请求属性，方便后续使用
                request.setAttribute("storeId", storeId);
                return storeId;
            }
        }
        
        log.warn("无法获取当前登录用户的店铺ID");
        return null;
    }
    
    /**
     * 获取店铺余额信息
     */
    @GetMapping("/info")
    public R<StoreBalance> getBalanceInfo(@RequestParam(value = "shopId", required = false) Long shopId) {
        // 如果前端没有传递shopId，从当前登录用户的会话中获取
        if (shopId == null) {
            shopId = getCurrentStoreId();
        }
        
        if (shopId == null) {
            return R.error("请先登录");
        }
        
        StoreBalance balance = storeBalanceService.getByStoreId(shopId);
        return R.success(balance);
    }
    
    /**
     * 提交提现申请
     */
    @PostMapping("/withdrawal")
    public R<String> submitWithdrawal(@RequestBody WithdrawalRecord withdrawalRecord) {
        // 从当前登录用户的会话中获取店铺ID
        Long storeId = getCurrentStoreId();
        if (storeId == null) {
            return R.error("请先登录");
        }
        
        // 设置店铺ID
        withdrawalRecord.setStoreId(storeId);
        
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
            @RequestParam(value = "shopId", required = false) Long shopId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        // 如果前端没有传递shopId，从当前登录用户的会话中获取
        if (shopId == null) {
            shopId = getCurrentStoreId();
        }
        
        if (shopId == null) {
            return R.error("请先登录");
        }
        
        log.info("获取店铺提现记录：storeId={}", shopId);
        IPage<WithdrawalRecord> records = withdrawalRecordService.getWithdrawalRecordsByStoreId(shopId, page, pageSize);
        return R.success(records);
    }
    
    /**
     * 获取近30天可提现金额趋势
     */
    @GetMapping("/trend")
    public R<Map<String, Object>> getBalanceTrend(@RequestParam(value = "shopId", required = false) Long shopId) {
        // 如果前端没有传递shopId，从当前登录用户的会话中获取
        if (shopId == null) {
            shopId = getCurrentStoreId();
        }
        
        if (shopId == null) {
            return R.error("请先登录");
        }
        
        // 这里可以实现获取近30天余额趋势的逻辑
        // 暂时返回空数据，前端可以根据需要调整
        Map<String, Object> trendData = new HashMap<>();
        return R.success(trendData);
    }
}
