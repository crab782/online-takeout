package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/backend/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/pending-orders")
    public ResponseVO<List<OrderVO>> getPendingOrders(@RequestParam Long shopId) {
        return ResponseVO.success(dashboardService.getPendingOrders(shopId));
    }

    @GetMapping("/today-stats")
    public ResponseVO<Map<String, Object>> getTodayOrderStats(@RequestParam Long shopId) {
        return ResponseVO.success(dashboardService.getTodayOrderStats(shopId));
    }

    @PostMapping("/business-trend")
    public ResponseVO<List<BusinessTrendVO>> getBusinessTrend(@RequestBody BusinessTrendQueryDTO queryDTO) {
        return ResponseVO.success(dashboardService.getBusinessTrend(queryDTO));
    }

    @GetMapping("/user-expenses")
    public ResponseVO<List<Map<String, Object>>> getUserExpenses(@RequestParam Long shopId,
                                                                 @RequestParam String sortBy,
                                                                 @RequestParam String sortOrder) {
        return ResponseVO.success(dashboardService.getUserExpenses(shopId, sortBy, sortOrder));
    }

    @GetMapping("/today-order-status")
    public ResponseVO<List<OrderStatusDistributionVO>> getTodayOrderStatus(@RequestParam Long shopId) {
        return ResponseVO.success(dashboardService.getTodayOrderStatus(shopId));
    }

    @GetMapping("/today-order-price")
    public ResponseVO<List<OrderPriceDistributionVO>> getTodayOrderPrice(@RequestParam Long shopId) {
        return ResponseVO.success(dashboardService.getTodayOrderPrice(shopId));
    }

    @PostMapping("/shop-views")
    public ResponseVO<List<ShopViewVO>> getShopViews(@RequestBody ShopViewsQueryDTO queryDTO) {
        return ResponseVO.success(dashboardService.getShopViews(queryDTO));
    }

    @GetMapping("/available-withdrawal")
    public ResponseVO<BigDecimal> getAvailableWithdrawalAmount(@RequestParam Long shopId) {
        return ResponseVO.success(dashboardService.getAvailableWithdrawalAmount(shopId));
    }
}
