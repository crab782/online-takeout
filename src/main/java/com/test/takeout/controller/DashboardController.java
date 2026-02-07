package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Orders;
import com.test.takeout.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 仪表盘控制器
 */
@Slf4j
@RestController
@RequestMapping("/backend/dashboard")
public class DashboardController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 获取待处理订单
     * @return 待处理订单列表
     */
    @GetMapping("/pending-orders")
    public R<Map<String, Object>> getPendingOrders() {
        log.info("获取待处理订单");

        // 构造条件构造器，查询状态为待处理的订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 0); // 0表示待处理
        queryWrapper.orderByDesc(Orders::getCreateTime);

        // 执行查询
        List<Orders> pendingOrders = ordersService.list(queryWrapper);

        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", pendingOrders);
        data.put("total", pendingOrders.size());

        return R.success(data);
    }

    /**
     * 获取当天订单统计
     * @return 当天订单统计数据
     */
    @GetMapping("/today-stats")
    public R<Map<String, Object>> getTodayOrderStats() {
        log.info("获取当天订单统计");

        // 获取当天的开始时间和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 查询当天的所有订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Orders::getCreateTime, startOfDay);
        queryWrapper.le(Orders::getCreateTime, endOfDay);

        List<Orders> todayOrders = ordersService.list(queryWrapper);

        // 统计总订单数
        int totalOrders = todayOrders.size();

        // 统计总金额
        BigDecimal totalAmount = todayOrders.stream()
                .map(Orders::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计已完成的订单数
        int completedOrders = (int) todayOrders.stream()
                .filter(order -> order.getStatus() == 1) // 1表示已完成
                .count();

        // 构建响应数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("totalAmount", totalAmount);
        stats.put("completedOrders", completedOrders);

        return R.success(stats);
    }

    /**
     * 获取近30日业务趋势数据
     * @return 近30日业务趋势数据
     */
    @GetMapping("/business-trend")
    public R<Map<String, Object>> getBusinessTrend() {
        log.info("获取近30日业务趋势数据");

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 准备近30天的数据容器
        List<String> dates = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();

        // 遍历近30天，从昨天开始往前推29天
        for (int i = 29; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            // 格式化日期为YYYY-MM-DD
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dateStr);

            // 查询当天的订单
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ge(Orders::getCreateTime, startOfDay);
            queryWrapper.le(Orders::getCreateTime, endOfDay);

            List<Orders> dayOrders = ordersService.list(queryWrapper);

            // 统计订单数
            int orderCount = dayOrders.size();
            orderCounts.add(orderCount);

            // 统计金额
            BigDecimal dayAmount = dayOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            amounts.add(dayAmount.doubleValue());
        }

        // 构建响应数据
        Map<String, Object> trend = new HashMap<>();
        trend.put("dates", dates.toArray(new String[0]));
        trend.put("orderCounts", orderCounts.stream().mapToInt(Integer::intValue).toArray());
        trend.put("amounts", amounts.stream().mapToDouble(Double::doubleValue).toArray());

        return R.success(trend);
    }

    /**
     * 获取用户支出数据
     * @return 用户支出数据
     */
    @GetMapping("/user-expenses")
    public R<Map<String, Object>> getUserExpenses() {
        log.info("获取用户支出数据");

        // 查询所有订单
        List<Orders> allOrders = ordersService.list();

        // 统计用户数量（去重）
        long userCounts = allOrders.stream()
                .map(Orders::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        // 统计总支出金额
        BigDecimal totalExpenses = allOrders.stream()
                .map(Orders::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 计算平均支出
        BigDecimal averageExpense = userCounts > 0 ? 
                totalExpenses.divide(BigDecimal.valueOf(userCounts), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;

        // 构建响应数据
        Map<String, Object> expenses = new HashMap<>();
        expenses.put("userCounts", userCounts);
        expenses.put("totalExpenses", totalExpenses);
        expenses.put("averageExpense", averageExpense);

        return R.success(expenses);
    }

    /**
     * 获取今日订单状态分布
     * @return 今日订单状态分布
     */
    @GetMapping("/today-order-status")
    public R<Map<String, Object>> getTodayOrderStatus() {
        log.info("获取今日订单状态分布");

        // 获取当天的开始时间和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 查询当天的所有订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Orders::getCreateTime, startOfDay);
        queryWrapper.le(Orders::getCreateTime, endOfDay);

        List<Orders> todayOrders = ordersService.list(queryWrapper);

        // 统计各状态的订单数
        long pending = todayOrders.stream()
                .filter(order -> order.getStatus() == 0) // 0表示待处理
                .count();

        long completed = todayOrders.stream()
                .filter(order -> order.getStatus() == 1) // 1表示已完成
                .count();

        long cancelled = todayOrders.stream()
                .filter(order -> order.getStatus() == 2) // 2表示已取消
                .count();

        // 构建响应数据
        Map<String, Object> status = new HashMap<>();
        status.put("pending", pending);
        status.put("completed", completed);
        status.put("cancelled", cancelled);

        return R.success(status);
    }

    /**
     * 获取今日订单价格分布
     * @return 今日订单价格分布
     */
    @GetMapping("/today-order-price")
    public R<Map<String, Object>> getTodayOrderPrice() {
        log.info("获取今日订单价格分布");

        // 获取当天的开始时间和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 查询当天的所有订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Orders::getCreateTime, startOfDay);
        queryWrapper.le(Orders::getCreateTime, endOfDay);

        List<Orders> todayOrders = ordersService.list(queryWrapper);

        // 统计各价格区间的订单数
        long range1 = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0 && amount.compareTo(new BigDecimal(50)) < 0;
                })
                .count();

        long range2 = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(50)) >= 0 && amount.compareTo(new BigDecimal(100)) < 0;
                })
                .count();

        long range3 = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(100)) >= 0 && amount.compareTo(new BigDecimal(200)) < 0;
                })
                .count();

        long range4 = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(200)) >= 0;
                })
                .count();

        // 构建响应数据
        Map<String, Object> price = new HashMap<>();
        price.put("range1", range1); // 0-50
        price.put("range2", range2); // 50-100
        price.put("range3", range3); // 100-200
        price.put("range4", range4); // 200+

        return R.success(price);
    }

}
