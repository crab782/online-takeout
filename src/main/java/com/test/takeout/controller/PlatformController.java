package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.JwtUtil;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.entity.Store;
import com.test.takeout.service.EmployeeService;
import com.test.takeout.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台管理控制器，处理平台管理相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/platform")
public class PlatformController {

    private final EmployeeService employeeService;
    private final StoreService storeService;

    public PlatformController(EmployeeService employeeService, StoreService storeService) {
        this.employeeService = employeeService;
        this.storeService = storeService;
    }

    /**
     * 平台登录
     * @param loginData 登录数据
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        log.info("平台登录：loginData={}", loginData);

        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || username.isEmpty()) {
            return R.error("用户名不能为空");
        }

        if (password == null || password.isEmpty()) {
            return R.error("密码不能为空");
        }

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee employee = employeeService.getOne(queryWrapper);

        if (employee == null) {
            return R.error("用户名不存在");
        }

        // 验证密码（使用BCrypt进行密码验证）
        if (!org.springframework.security.crypto.bcrypt.BCrypt.checkpw(password, employee.getPassword())) {
            return R.error("密码错误");
        }

        if (employee.getStatus() == 0) {
            return R.error("账号已被禁用");
        }

        String token = JwtUtil.generateToken(employee.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", employee.getId());
        result.put("username", employee.getUsername());
        result.put("name", employee.getName());

        return R.success(result);
    }

    /**
     * 平台登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    public R<String> logout() {
        log.info("平台登出");

        return R.success("登出成功");
    }

    /**
     * 获取店铺列表（包含评论统计数据）
     * @param page 当前页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键词（可选）
     * @return 店铺列表数据
     */
    @GetMapping("/comment/shop-list")
    public R<Page<Map<String, Object>>> getShopListWithCommentStats(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("获取店铺列表（包含评论统计数据）：page={}, pageSize={}, keyword={}", page, pageSize, keyword);

        Page<Store> storePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(Store::getName, keyword).or()
                    .like(Store::getDescription, keyword).or()
                    .like(Store::getAddress, keyword);
        }

        queryWrapper.orderByDesc(Store::getCreateTime);
        storeService.page(storePage, queryWrapper);

        Page<Map<String, Object>> resultPage = new Page<>();
        resultPage.setTotal(storePage.getTotal());
        resultPage.setCurrent(storePage.getCurrent());
        resultPage.setSize(storePage.getSize());
        resultPage.setPages(storePage.getPages());

        List<Map<String, Object>> resultList = storePage.getRecords().stream().map(store -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", store.getId());
            map.put("name", store.getName());
            map.put("address", store.getAddress());
            map.put("status", store.getStatus());
            map.put("createTime", store.getCreateTime());
            
            // 模拟评论统计数据
            map.put("totalComments", (int) (Math.random() * 100));
            map.put("averageRating", String.format("%.1f", 3.0 + Math.random() * 2.0));
            map.put("positiveRate", String.format("%.1f%%", 70.0 + Math.random() * 30.0));
            
            return map;
        }).toList();

        resultPage.setRecords(resultList);

        return R.success(resultPage);
    }

    /**
     * 获取店铺评论列表
     * @param shopId 店铺ID
     * @param page 当前页码
     * @param pageSize 每页数量
     * @return 评论列表数据
     */
    @GetMapping("/comment/list")
    public R<Page<Map<String, Object>>> getShopComments(
            @RequestParam("shopId") String shopId,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize) {
        log.info("获取店铺评论列表：shopId={}, page={}, pageSize={}", shopId, page, pageSize);

        Page<Map<String, Object>> commentPage = new Page<>(page, pageSize);
        commentPage.setTotal(50);
        commentPage.setCurrent(page);
        commentPage.setSize(pageSize);
        commentPage.setPages((int) Math.ceil(50.0 / pageSize));

        List<Map<String, Object>> commentList = new java.util.ArrayList<>();
        for (int i = 0; i < pageSize && i < 50; i++) {
            int index = (page - 1) * pageSize + i;
            if (index >= 50) break;
            
            Map<String, Object> comment = new HashMap<>();
            comment.put("id", "comment_" + (index + 1));
            comment.put("shopId", shopId);
            comment.put("userId", "user_" + ((index % 10) + 1));
            comment.put("userName", "用户" + ((index % 10) + 1));
            comment.put("rating", 3 + (index % 3));
            comment.put("content", "这是一条评论内容" + (index + 1));
            comment.put("createTime", java.time.LocalDateTime.now().minusDays(index));
            comment.put("reply", index % 3 == 0 ? "这是商家回复" : null);
            comment.put("replyTime", index % 3 == 0 ? java.time.LocalDateTime.now().minusDays(index).plusHours(1) : null);
            
            commentList.add(comment);
        }

        commentPage.setRecords(commentList);

        return R.success(commentPage);
    }

    /**
     * 获取评论详情
     * @param id 评论ID
     * @return 评论详情数据
     */
    @GetMapping("/comment/detail/{id}")
    public R<Map<String, Object>> getCommentDetail(@PathVariable("id") String id) {
        log.info("获取评论详情：id={}", id);

        Map<String, Object> comment = new HashMap<>();
        comment.put("id", id);
        comment.put("shopId", "shop_1");
        comment.put("shopName", "测试店铺");
        comment.put("userId", "user_1");
        comment.put("userName", "测试用户");
        comment.put("rating", 5);
        comment.put("content", "这是一条详细的评论内容，描述了用户的用餐体验和对店铺的评价。");
        comment.put("createTime", java.time.LocalDateTime.now().minusDays(2));
        comment.put("reply", "感谢您的评价，我们会继续努力提供更好的服务！");
        comment.put("replyTime", java.time.LocalDateTime.now().minusDays(1));
        comment.put("orderId", "order_123");
        comment.put("orderTime", java.time.LocalDateTime.now().minusDays(3));

        return R.success(comment);
    }

    /**
     * 删除评论
     * @param id 评论ID
     * @return 删除结果
     */
    @DeleteMapping("/comment/delete/{id}")
    public R<String> deleteComment(@PathVariable("id") String id) {
        log.info("删除评论：id={}", id);

        return R.success("删除评论成功");
    }

    /**
     * 回复评论
     * @param params 请求参数
     * @return 回复结果
     */
    @PostMapping("/comment/reply")
    public R<String> replyComment(@RequestBody Map<String, String> params) {
        String commentId = params.get("commentId");
        String content = params.get("content");

        log.info("回复评论：commentId={}, content={}", commentId, content);

        if (commentId == null || commentId.isEmpty()) {
            return R.error("评论ID不能为空");
        }

        if (content == null || content.isEmpty()) {
            return R.error("回复内容不能为空");
        }

        return R.success("回复评论成功");
    }

    /**
     * 获取平台数据看板统计信息
     * @return 统计数据
     */
    @GetMapping("/dashboard/stats")
    public R<Map<String, Object>> getPlatformStats() {
        log.info("获取平台数据看板统计信息");

        Map<String, Object> stats = new HashMap<>();
        
        // 今日数据
        Map<String, Object> todayData = new HashMap<>();
        todayData.put("orderCount", 1250);
        todayData.put("orderAmount", 45680.50);
        todayData.put("userCount", 89);
        todayData.put("shopCount", 3);
        stats.put("today", todayData);
        
        // 本月数据
        Map<String, Object> monthData = new HashMap<>();
        monthData.put("orderCount", 35680);
        monthData.put("orderAmount", 1256780.00);
        monthData.put("userCount", 2456);
        monthData.put("shopCount", 45);
        stats.put("month", monthData);
        
        // 总体数据
        Map<String, Object> totalData = new HashMap<>();
        totalData.put("totalOrderCount", 1256800);
        totalData.put("totalOrderAmount", 45678900.00);
        totalData.put("totalUserCount", 45678);
        totalData.put("totalShopCount", 1256);
        stats.put("total", totalData);
        
        // 增长率数据
        Map<String, Object> growthData = new HashMap<>();
        growthData.put("orderGrowth", 12.5);
        growthData.put("amountGrowth", 15.8);
        growthData.put("userGrowth", 8.3);
        growthData.put("shopGrowth", 5.2);
        stats.put("growth", growthData);
        
        // 其他统计数据
        stats.put("onlineShopCount", 1180);
        stats.put("offlineShopCount", 76);
        stats.put("activeUserCount", 12345);
        stats.put("pendingOrderCount", 156);
        stats.put("processingOrderCount", 89);
        stats.put("completedOrderCount", 1005);

        return R.success(stats);
    }

    /**
     * 获取平台近30天趋势数据
     * @return 趋势数据
     */
    @GetMapping("/dashboard/trend")
    public R<Map<String, Object>> getPlatformTrendData() {
        log.info("获取平台近30天趋势数据");

        Map<String, Object> trendData = new HashMap<>();
        
        // 日期列表
        List<String> dates = new java.util.ArrayList<>();
        // 订单数量列表
        List<Integer> orderCounts = new java.util.ArrayList<>();
        // 订单金额列表
        List<Double> orderAmounts = new java.util.ArrayList<>();
        // 用户数量列表
        List<Integer> userCounts = new java.util.ArrayList<>();
        // 店铺数量列表
        List<Integer> shopCounts = new java.util.ArrayList<>();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            java.time.LocalDate date = today.minusDays(i);
            dates.add(date.toString());
            
            // 模拟数据
            orderCounts.add(1000 + (int) (Math.random() * 500));
            orderAmounts.add(35000.0 + Math.random() * 15000.0);
            userCounts.add(50 + (int) (Math.random() * 50));
            shopCounts.add(1 + (int) (Math.random() * 3));
        }
        
        trendData.put("dates", dates);
        trendData.put("orderCounts", orderCounts);
        trendData.put("orderAmounts", orderAmounts);
        trendData.put("userCounts", userCounts);
        trendData.put("shopCounts", shopCounts);
        
        // 汇总数据
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", orderCounts.stream().mapToInt(Integer::intValue).sum());
        summary.put("totalAmount", orderAmounts.stream().mapToDouble(Double::doubleValue).sum());
        summary.put("totalUsers", userCounts.stream().mapToInt(Integer::intValue).sum());
        summary.put("totalShops", shopCounts.stream().mapToInt(Integer::intValue).sum());
        trendData.put("summary", summary);

        return R.success(trendData);
    }

    /**
     * 获取商家营收排序数据
     * @param timeDimension 时间维度（today/week/month）
     * @return 营收排序数据
     */
    @GetMapping("/dashboard/revenue-ranking")
    public R<List<Map<String, Object>>> getShopRevenueRanking(
            @RequestParam("timeDimension") String timeDimension) {
        log.info("获取商家营收排序数据：timeDimension={}", timeDimension);

        if (timeDimension == null || timeDimension.isEmpty()) {
            return R.error("时间维度不能为空");
        }

        if (!timeDimension.equals("today") && !timeDimension.equals("week") && !timeDimension.equals("month")) {
            return R.error("时间维度参数错误，必须是 today/week/month 之一");
        }

        List<Map<String, Object>> rankingList = new java.util.ArrayList<>();
        
        int shopCount = 10;
        for (int i = 0; i < shopCount; i++) {
            Map<String, Object> shopData = new HashMap<>();
            shopData.put("rank", i + 1);
            shopData.put("shopId", "shop_" + (i + 1));
            shopData.put("shopName", "店铺" + (i + 1));
            shopData.put("shopAddress", "测试地址" + (i + 1));
            
            // 根据时间维度生成不同的营收数据
            double baseRevenue = 50000.0 - (i * 3000.0);
            double orderCount = 500.0 - (i * 30.0);
            
            if (timeDimension.equals("today")) {
                shopData.put("revenue", baseRevenue * 0.1);
                shopData.put("orderCount", (int) (orderCount * 0.1));
            } else if (timeDimension.equals("week")) {
                shopData.put("revenue", baseRevenue * 0.5);
                shopData.put("orderCount", (int) (orderCount * 0.5));
            } else {
                shopData.put("revenue", baseRevenue);
                shopData.put("orderCount", (int) orderCount);
            }
            
            shopData.put("averageOrderAmount", shopData.get("revenue") instanceof Double ? 
                (Double) shopData.get("revenue") / (Integer) shopData.get("orderCount") : 0);
            
            rankingList.add(shopData);
        }

        return R.success(rankingList);
    }

    /**
     * 获取低活跃商家预警数据
     * @return 低活跃商家数据
     */
    @GetMapping("/dashboard/low-activity")
    public R<List<Map<String, Object>>> getLowActivityShops() {
        log.info("获取低活跃商家预警数据");

        List<Map<String, Object>> lowActivityShops = new java.util.ArrayList<>();
        
        int shopCount = 8;
        for (int i = 0; i < shopCount; i++) {
            Map<String, Object> shopData = new HashMap<>();
            shopData.put("shopId", "shop_" + (i + 1));
            shopData.put("shopName", "店铺" + (i + 1));
            shopData.put("shopAddress", "测试地址" + (i + 1));
            shopData.put("status", 1);
            
            // 模拟低活跃数据
            shopData.put("lastOrderTime", java.time.LocalDateTime.now().minusDays(7 + i * 2));
            shopData.put("orderCountLast7Days", i * 2);
            shopData.put("orderCountLast30Days", 5 + i * 3);
            shopData.put("revenueLast7Days", 500.0 * (i + 1));
            shopData.put("revenueLast30Days", 2000.0 * (i + 1));
            
            // 活跃度等级
            String activityLevel;
            if (i < 3) {
                activityLevel = "极低";
            } else if (i < 6) {
                activityLevel = "较低";
            } else {
                activityLevel = "一般";
            }
            shopData.put("activityLevel", activityLevel);
            
            // 预警级别
            String warningLevel;
            if (i < 3) {
                warningLevel = "高";
            } else if (i < 6) {
                warningLevel = "中";
            } else {
                warningLevel = "低";
            }
            shopData.put("warningLevel", warningLevel);
            
            // 联系信息
            shopData.put("contactName", "联系人" + (i + 1));
            shopData.put("contactPhone", "1380013800" + i);
            
            lowActivityShops.add(shopData);
        }

        return R.success(lowActivityShops);
    }

    /**
     * 获取新入驻商家数据
     * @return 新入驻商家数据
     */
    @GetMapping("/dashboard/new-shops")
    public R<List<Map<String, Object>>> getNewShopsData() {
        log.info("获取新入驻商家数据");

        List<Map<String, Object>> newShops = new java.util.ArrayList<>();
        
        int shopCount = 10;
        for (int i = 0; i < shopCount; i++) {
            Map<String, Object> shopData = new HashMap<>();
            shopData.put("shopId", "shop_" + (i + 1));
            shopData.put("shopName", "新店铺" + (i + 1));
            shopData.put("shopAddress", "新地址" + (i + 1));
            shopData.put("shopType", i % 2 == 0 ? "快餐" : "火锅");
            
            // 入驻时间
            shopData.put("registerTime", java.time.LocalDateTime.now().minusDays(i));
            shopData.put("auditTime", java.time.LocalDateTime.now().minusDays(i).plusHours(2));
            
            // 审核状态
            String auditStatus;
            if (i < 3) {
                auditStatus = "待审核";
            } else if (i < 7) {
                auditStatus = "审核通过";
            } else {
                auditStatus = "已上线";
            }
            shopData.put("auditStatus", auditStatus);
            
            // 联系信息
            shopData.put("contactName", "联系人" + (i + 1));
            shopData.put("contactPhone", "1390013900" + i);
            
            // 店铺信息
            shopData.put("description", "这是新入驻店铺" + (i + 1) + "的描述信息");
            shopData.put("businessHours", "09:00-22:00");
            shopData.put("deliveryRadius", 5 + i);
            
            // 营业数据
            shopData.put("orderCount", 10 + i * 5);
            shopData.put("revenue", 500.0 + i * 100.0);
            
            newShops.add(shopData);
        }

        return R.success(newShops);
    }

    /**
     * 获取订单状态分布数据
     * @return 订单状态分布数据
     */
    @GetMapping("/dashboard/order-status")
    public R<Map<String, Object>> getOrderStatusDistribution() {
        log.info("获取订单状态分布数据");

        Map<String, Object> distribution = new HashMap<>();
        
        // 待处理订单
        Map<String, Object> pending = new HashMap<>();
        pending.put("count", 156);
        pending.put("percentage", 12.5);
        pending.put("color", "#FF6B6B");
        distribution.put("pending", pending);
        
        // 处理中订单
        Map<String, Object> processing = new HashMap<>();
        processing.put("count", 89);
        processing.put("percentage", 7.1);
        processing.put("color", "#4ECDC4");
        distribution.put("processing", processing);
        
        // 已完成订单
        Map<String, Object> completed = new HashMap<>();
        completed.put("count", 1005);
        completed.put("percentage", 80.4);
        completed.put("color", "#06D6A0");
        distribution.put("completed", completed);
        
        // 已取消订单
        Map<String, Object> cancelled = new HashMap<>();
        cancelled.put("count", 5);
        cancelled.put("percentage", 0.4);
        cancelled.put("color", "#EF4444");
        distribution.put("cancelled", cancelled);
        
        // 总计
        Map<String, Object> total = new HashMap<>();
        total.put("totalCount", 1255);
        total.put("totalAmount", 45680.50);
        distribution.put("total", total);

        return R.success(distribution);
    }

    /**
     * 获取客单价分布数据
     * @return 客单价分布数据
     */
    @GetMapping("/dashboard/order-price")
    public R<Map<String, Object>> getOrderPriceDistribution() {
        log.info("获取客单价分布数据");

        Map<String, Object> distribution = new HashMap<>();
        
        // 0-30元
        Map<String, Object> range1 = new HashMap<>();
        range1.put("count", 325);
        range1.put("percentage", 25.9);
        range1.put("color", "#36A2EB");
        distribution.put("range1", range1);
        
        // 30-50元
        Map<String, Object> range2 = new HashMap<>();
        range2.put("count", 438);
        range2.put("percentage", 34.9);
        range2.put("color", "#4ECDC4");
        distribution.put("range2", range2);
        
        // 50-80元
        Map<String, Object> range3 = new HashMap<>();
        range3.put("count", 326);
        range3.put("percentage", 26.0);
        range3.put("color", "#F59E0B");
        distribution.put("range3", range3);
        
        // 80-120元
        Map<String, Object> range4 = new HashMap<>();
        range4.put("count", 125);
        range4.put("percentage", 10.0);
        range4.put("color", "#06D6A0");
        distribution.put("range4", range4);
        
        // 120元以上
        Map<String, Object> range5 = new HashMap<>();
        range5.put("count", 41);
        range5.put("percentage", 3.2);
        range5.put("color", "#FF6B6B");
        distribution.put("range5", range5);
        
        // 总计
        Map<String, Object> total = new HashMap<>();
        total.put("totalCount", 1255);
        total.put("averagePrice", 48.5);
        total.put("medianPrice", 42.0);
        distribution.put("total", total);

        return R.success(distribution);
    }

    /**
     * 获取店铺营收详情数据（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param shopName 店铺名称（可选）
     * @return 店铺营收数据
     */
    @GetMapping("/dashboard/revenue-by-shop")
    public R<Map<String, Object>> getRevenueByShop(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String shopName) {
        log.info("获取店铺营收详情数据，页码: {}, 每页数量: {}, 店铺名称: {}", page, pageSize, shopName);

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> shopRevenueList = new java.util.ArrayList<>();
        
        for (int i = 1; i <= pageSize; i++) {
            Map<String, Object> shopData = new HashMap<>();
            
            shopData.put("shopId", 1000L + i);
            shopData.put("shopName", "美味餐厅" + i);
            shopData.put("shopImage", "https://example.com/shop" + i + ".jpg");
            shopData.put("shopType", "中餐");
            shopData.put("shopStatus", 1);
            
            shopData.put("totalOrders", 150 + i * 10);
            shopData.put("totalRevenue", 4500.0 + i * 200.0);
            shopData.put("averageOrderAmount", 30.0 + i * 0.5);
            shopData.put("todayOrders", 5 + i);
            shopData.put("todayRevenue", 150.0 + i * 10.0);
            
            shopData.put("weekOrders", 35 + i * 8);
            shopData.put("weekRevenue", 1050.0 + i * 80.0);
            
            shopData.put("monthOrders", 120 + i * 15);
            shopData.put("monthRevenue", 3600.0 + i * 150.0);
            
            shopData.put("commissionRate", 0.05);
            shopData.put("commissionAmount", (4500.0 + i * 200.0) * 0.05);
            
            shopData.put("createTime", "2024-01-" + String.format("%02d", i));
            shopData.put("lastOrderTime", "2024-02-08 14:30:00");
            
            shopRevenueList.add(shopData);
        }
        
        result.put("list", shopRevenueList);
        result.put("total", 100);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", 10);

        return R.success(result);
    }

    /**
     * 获取平台近30日所有店铺浏览量趋势数据
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @return 店铺浏览量趋势数据
     */
    @GetMapping("/dashboard/shop-views-trend")
    public R<Map<String, Object>> getShopViewsTrend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("获取平台近30日所有店铺浏览量趋势数据，开始日期: {}, 结束日期: {}", startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        
        String[] dates = {
            "2024-01-10", "2024-01-11", "2024-01-12", "2024-01-13", "2024-01-14",
            "2024-01-15", "2024-01-16", "2024-01-17", "2024-01-18", "2024-01-19",
            "2024-01-20", "2024-01-21", "2024-01-22", "2024-01-23", "2024-01-24",
            "2024-01-25", "2024-01-26", "2024-01-27", "2024-01-28", "2024-01-29",
            "2024-01-30", "2024-01-31", "2024-02-01", "2024-02-02", "2024-02-03",
            "2024-02-04", "2024-02-05", "2024-02-06", "2024-02-07", "2024-02-08"
        };
        
        int[] views = {
            1250, 1320, 1180, 1450, 1380,
            1520, 1680, 1750, 1620, 1890,
            1950, 2100, 2250, 2180, 2320,
            2450, 2580, 2650, 2720, 2850,
            2920, 3050, 3180, 3250, 3320,
            3450, 3580, 3650, 3720, 3850
        };
        
        int[] uniqueVisitors = {
            850, 920, 780, 950, 880,
            1020, 1180, 1250, 1120, 1390,
            1450, 1600, 1750, 1680, 1820,
            1950, 2080, 2150, 2220, 2350,
            2420, 2550, 2680, 2750, 2820,
            2950, 3080, 3150, 3220, 3350
        };
        
        for (int i = 0; i < dates.length; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dates[i]);
            dayData.put("totalViews", views[i]);
            dayData.put("uniqueVisitors", uniqueVisitors[i]);
            dayData.put("averageViewsPerVisitor", String.format("%.2f", (double)views[i] / uniqueVisitors[i]));
            trendData.add(dayData);
        }
        
        result.put("trendData", trendData);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalViews", 85200);
        summary.put("totalUniqueVisitors", 64200);
        summary.put("averageViewsPerDay", 2840);
        summary.put("averageUniqueVisitorsPerDay", 2140);
        summary.put("peakViewsDate", "2024-02-08");
        summary.put("peakViews", 3850);
        summary.put("growthRate", 208.0);
        
        result.put("summary", summary);

        return R.success(result);
    }

    /**
     * 获取财务管理数据
     * @param month 月份（yyyy-MM格式）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 财务数据
     */
    @GetMapping("/finance/data")
    public R<Map<String, Object>> getFinanceData(
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("获取财务管理数据，月份: {}, 页码: {}, 每页数量: {}", month, page, pageSize);

        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRevenue", 285600.0);
        summary.put("totalCommission", 14280.0);
        summary.put("totalWithdrawal", 12500.0);
        summary.put("pendingWithdrawal", 1780.0);
        summary.put("totalShops", 156);
        summary.put("activeShops", 142);
        summary.put("totalOrders", 12560);
        summary.put("averageOrderAmount", 22.74);
        summary.put("month", month != null ? month : "2024-02");
        
        result.put("summary", summary);
        
        List<Map<String, Object>> financeList = new java.util.ArrayList<>();
        
        for (int i = 1; i <= pageSize; i++) {
            Map<String, Object> financeData = new HashMap<>();
            
            financeData.put("shopId", 1000L + i);
            financeData.put("shopName", "美味餐厅" + i);
            financeData.put("shopType", "中餐");
            
            financeData.put("revenue", 1800.0 + i * 50.0);
            financeData.put("commission", 90.0 + i * 2.5);
            financeData.put("withdrawal", 80.0 + i * 2.0);
            financeData.put("pendingWithdrawal", 10.0 + i * 0.5);
            
            financeData.put("orderCount", 80 + i * 2);
            financeData.put("averageOrderAmount", 22.5 + i * 0.1);
            
            financeData.put("commissionRate", 0.05);
            financeData.put("withdrawalRate", String.format("%.2f", (80.0 + i * 2.0) / (1800.0 + i * 50.0) * 100));
            
            financeData.put("lastWithdrawalTime", "2024-02-08 10:30:00");
            financeData.put("withdrawalStatus", "已提现");
            
            financeList.add(financeData);
        }
        
        result.put("list", financeList);
        result.put("total", 156);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", 16);

        return R.success(result);
    }

    /**
     * 获取提现记录
     * @param page 页码
     * @param pageSize 每页数量
     * @param search 搜索关键词（可选）
     * @param status 状态筛选（可选）
     * @return 提现记录
     */
    @GetMapping("/finance/withdrawal/records")
    public R<Map<String, Object>> getWithdrawalRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        log.info("获取提现记录，页码: {}, 每页数量: {}, 搜索关键词: {}, 状态: {}", page, pageSize, search, status);

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> withdrawalList = new java.util.ArrayList<>();
        
        String[] statuses = {"已通过", "处理中", "已拒绝"};
        String[] statusesEn = {"approved", "processing", "rejected"};
        
        for (int i = 1; i <= pageSize; i++) {
            Map<String, Object> withdrawalData = new HashMap<>();
            
            withdrawalData.put("id", 10000L + i);
            withdrawalData.put("shopId", 1000L + i);
            withdrawalData.put("shopName", "美味餐厅" + i);
            withdrawalData.put("shopType", "中餐");
            
            withdrawalData.put("amount", 500.0 + i * 10.0);
            withdrawalData.put("fee", 2.5 + i * 0.05);
            withdrawalData.put("actualAmount", 497.5 + i * 9.95);
            
            int statusIndex = (i - 1) % 3;
            withdrawalData.put("status", statuses[statusIndex]);
            withdrawalData.put("statusEn", statusesEn[statusIndex]);
            
            withdrawalData.put("applyTime", "2024-02-08 " + String.format("%02d", 8 + i) + ":30:00");
            withdrawalData.put("processTime", statusIndex == 0 ? "2024-02-08 " + String.format("%02d", 9 + i) + ":30:00" : null);
            withdrawalData.put("remark", statusIndex == 2 ? "账户信息有误" : "");
            
            withdrawalData.put("bankName", "中国工商银行");
            withdrawalData.put("bankAccount", "6222****" + String.format("%04d", 1000 + i));
            withdrawalData.put("accountName", "张三" + i);
            
            withdrawalList.add(withdrawalData);
        }
        
        result.put("list", withdrawalList);
        result.put("total", 86);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", 9);

        return R.success(result);
    }

    /**
     * 获取提现趋势数据
     * @return 提现趋势数据
     */
    @GetMapping("/finance/withdrawal/trend")
    public R<Map<String, Object>> getWithdrawalTrend() {
        log.info("获取提现趋势数据");

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        
        String[] dates = {
            "2024-01-10", "2024-01-11", "2024-01-12", "2024-01-13", "2024-01-14",
            "2024-01-15", "2024-01-16", "2024-01-17", "2024-01-18", "2024-01-19",
            "2024-01-20", "2024-01-21", "2024-01-22", "2024-01-23", "2024-01-24",
            "2024-01-25", "2024-01-26", "2024-01-27", "2024-01-28", "2024-01-29",
            "2024-01-30", "2024-01-31", "2024-02-01", "2024-02-02", "2024-02-03",
            "2024-02-04", "2024-02-05", "2024-02-06", "2024-02-07", "2024-02-08"
        };
        
        int[] withdrawalCount = {
            12, 15, 10, 18, 14,
            20, 22, 25, 19, 28,
            30, 32, 35, 33, 38,
            40, 42, 45, 43, 48,
            50, 52, 55, 53, 58,
            60, 62, 65, 63, 68
        };
        
        double[] withdrawalAmount = {
            6000.0, 7500.0, 5000.0, 9000.0, 7000.0,
            10000.0, 11000.0, 12500.0, 9500.0, 14000.0,
            15000.0, 16000.0, 17500.0, 16500.0, 19000.0,
            20000.0, 21000.0, 22500.0, 21500.0, 24000.0,
            25000.0, 26000.0, 27500.0, 26500.0, 29000.0,
            30000.0, 31000.0, 32500.0, 31500.0, 34000.0
        };
        
        double[] feeAmount = {
            30.0, 37.5, 25.0, 45.0, 35.0,
            50.0, 55.0, 62.5, 47.5, 70.0,
            75.0, 80.0, 87.5, 82.5, 95.0,
            100.0, 105.0, 112.5, 107.5, 120.0,
            125.0, 130.0, 137.5, 132.5, 145.0,
            150.0, 155.0, 162.5, 157.5, 170.0
        };
        
        for (int i = 0; i < dates.length; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dates[i]);
            dayData.put("withdrawalCount", withdrawalCount[i]);
            dayData.put("withdrawalAmount", withdrawalAmount[i]);
            dayData.put("feeAmount", feeAmount[i]);
            dayData.put("averageAmount", String.format("%.2f", withdrawalAmount[i] / withdrawalCount[i]));
            trendData.add(dayData);
        }
        
        result.put("trendData", trendData);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalWithdrawalCount", 1155);
        summary.put("totalWithdrawalAmount", 575000.0);
        summary.put("totalFeeAmount", 2875.0);
        summary.put("averageWithdrawalAmount", 497.84);
        summary.put("averageDailyWithdrawalCount", 38);
        summary.put("averageDailyWithdrawalAmount", 19166.67);
        summary.put("peakWithdrawalDate", "2024-02-08");
        summary.put("peakWithdrawalAmount", 34000.0);
        summary.put("growthRate", 466.67);
        
        result.put("summary", summary);

        return R.success(result);
    }

    /**
     * 获取现金流趋势数据
     * @return 现金流趋势数据
     */
    @GetMapping("/finance/cash-flow/trend")
    public R<Map<String, Object>> getCashFlowTrend() {
        log.info("获取现金流趋势数据");

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        
        String[] dates = {
            "2024-01-10", "2024-01-11", "2024-01-12", "2024-01-13", "2024-01-14",
            "2024-01-15", "2024-01-16", "2024-01-17", "2024-01-18", "2024-01-19",
            "2024-01-20", "2024-01-21", "2024-01-22", "2024-01-23", "2024-01-24",
            "2024-01-25", "2024-01-26", "2024-01-27", "2024-01-28", "2024-01-29",
            "2024-01-30", "2024-01-31", "2024-02-01", "2024-02-02", "2024-02-03",
            "2024-02-04", "2024-02-05", "2024-02-06", "2024-02-07", "2024-02-08"
        };
        
        double[] income = {
            9500.0, 10200.0, 8800.0, 11500.0, 10800.0,
            12500.0, 13200.0, 14800.0, 13900.0, 15600.0,
            16800.0, 17500.0, 18900.0, 18200.0, 19500.0,
            20800.0, 21500.0, 22800.0, 22100.0, 23500.0,
            24800.0, 25500.0, 26800.0, 26100.0, 27500.0,
            28800.0, 29500.0, 30800.0, 30100.0, 31500.0
        };
        
        double[] expense = {
            6000.0, 7500.0, 5000.0, 9000.0, 7000.0,
            10000.0, 11000.0, 12500.0, 9500.0, 14000.0,
            15000.0, 16000.0, 17500.0, 16500.0, 19000.0,
            20000.0, 21000.0, 22500.0, 21500.0, 24000.0,
            25000.0, 26000.0, 27500.0, 26500.0, 29000.0,
            30000.0, 31000.0, 32500.0, 31500.0, 34000.0
        };
        
        double[] netCashFlow = {
            3500.0, 2700.0, 3800.0, 2500.0, 3800.0,
            2500.0, 2200.0, 2300.0, 4400.0, 1600.0,
            1800.0, 1500.0, 1400.0, 1700.0, 500.0,
            800.0, 500.0, 300.0, 600.0, -500.0,
            -200.0, -500.0, -700.0, -400.0, -1500.0,
            -1200.0, -1500.0, -1700.0, -1400.0, -2500.0
        };
        
        for (int i = 0; i < dates.length; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dates[i]);
            dayData.put("income", income[i]);
            dayData.put("expense", expense[i]);
            dayData.put("netCashFlow", netCashFlow[i]);
            dayData.put("cashFlowRate", String.format("%.2f", (netCashFlow[i] / income[i]) * 100));
            trendData.add(dayData);
        }
        
        result.put("trendData", trendData);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", 575000.0);
        summary.put("totalExpense", 575000.0);
        summary.put("totalNetCashFlow", 0.0);
        summary.put("averageIncome", 19166.67);
        summary.put("averageExpense", 19166.67);
        summary.put("averageNetCashFlow", 0.0);
        summary.put("peakIncomeDate", "2024-02-08");
        summary.put("peakIncome", 31500.0);
        summary.put("peakExpenseDate", "2024-02-08");
        summary.put("peakExpense", 34000.0);
        summary.put("cashFlowGrowthRate", -71.43);
        
        result.put("summary", summary);

        return R.success(result);
    }

    /**
     * 获取今日订单列表（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param orderNo 订单号（可选）
     * @return 订单列表数据
     */
    @GetMapping("/orders/today")
    public R<Map<String, Object>> getTodayOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo) {
        log.info("获取今日订单列表，页码: {}, 每页数量: {}, 订单号: {}", page, pageSize, orderNo);

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> orderList = new java.util.ArrayList<>();
        
        String[] statuses = {"待支付", "已支付", "处理中", "配送中", "已完成", "已取消"};
        String[] statusesEn = {"pending", "paid", "processing", "delivering", "completed", "cancelled"};
        
        for (int i = 1; i <= pageSize; i++) {
            Map<String, Object> orderData = new HashMap<>();
            
            orderData.put("orderId", 10000L + i);
            orderData.put("orderNo", "ORD" + String.format("%08d", 10000 + i));
            orderData.put("shopId", 1000L + i);
            orderData.put("shopName", "美味餐厅" + i);
            orderData.put("shopType", "中餐");
            
            orderData.put("userId", 20000L + i);
            orderData.put("userName", "用户" + i);
            orderData.put("userPhone", "138****" + String.format("%04d", 1000 + i));
            
            int statusIndex = (i - 1) % 6;
            orderData.put("status", statuses[statusIndex]);
            orderData.put("statusEn", statusesEn[statusIndex]);
            
            orderData.put("totalAmount", 50.0 + i * 5.0);
            orderData.put("discountAmount", 5.0 + i * 0.5);
            orderData.put("deliveryFee", 3.0);
            orderData.put("actualAmount", 48.0 + i * 4.5);
            
            orderData.put("orderTime", "2024-02-08 " + String.format("%02d", 9 + i) + ":30:00");
            orderData.put("deliveryTime", statusIndex >= 3 ? "2024-02-08 " + String.format("%02d", 10 + i) + ":30:00" : null);
            orderData.put("completeTime", statusIndex == 4 ? "2024-02-08 " + String.format("%02d", 11 + i) + ":30:00" : null);
            
            orderData.put("address", "北京市朝阳区xxx街道xxx号");
            orderData.put("remark", i % 3 == 0 ? "少放辣" : "");
            
            orderList.add(orderData);
        }
        
        result.put("list", orderList);
        result.put("total", 156);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", 16);

        return R.success(result);
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情数据
     */
    @GetMapping("/orders/{orderId}")
    public R<Map<String, Object>> getOrderDetail(@PathVariable Long orderId) {
        log.info("获取订单详情，订单ID: {}", orderId);

        Map<String, Object> result = new HashMap<>();
        
        result.put("orderId", orderId);
        result.put("orderNo", "ORD" + String.format("%08d", 10000 + (orderId % 100)));
        result.put("shopId", 1000L + (orderId % 100));
        result.put("shopName", "美味餐厅" + (orderId % 100));
        result.put("shopType", "中餐");
        result.put("shopImage", "https://example.com/shop" + (orderId % 100) + ".jpg");
        
        result.put("userId", 20000L + (orderId % 100));
        result.put("userName", "用户" + (orderId % 100));
        result.put("userPhone", "138****" + String.format("%04d", 1000 + (orderId % 100)));
        result.put("userAvatar", "https://example.com/user" + (orderId % 100) + ".jpg");
        
        String[] statuses = {"待支付", "已支付", "处理中", "配送中", "已完成", "已取消"};
        String[] statusesEn = {"pending", "paid", "processing", "delivering", "completed", "cancelled"};
        int statusIndex = (int)(orderId % 6);
        result.put("status", statuses[statusIndex]);
        result.put("statusEn", statusesEn[statusIndex]);
        
        result.put("totalAmount", 50.0 + (orderId % 100) * 0.5);
        result.put("discountAmount", 5.0 + (orderId % 100) * 0.05);
        result.put("deliveryFee", 3.0);
        result.put("actualAmount", 48.0 + (orderId % 100) * 0.45);
        
        result.put("orderTime", "2024-02-08 09:30:00");
        result.put("payTime", statusIndex >= 1 ? "2024-02-08 09:35:00" : null);
        result.put("acceptTime", statusIndex >= 2 ? "2024-02-08 09:40:00" : null);
        result.put("deliveryTime", statusIndex >= 3 ? "2024-02-08 10:30:00" : null);
        result.put("completeTime", statusIndex == 4 ? "2024-02-08 11:30:00" : null);
        
        result.put("address", "北京市朝阳区xxx街道xxx号");
        result.put("contactName", "张三");
        result.put("contactPhone", "138****1234");
        
        result.put("remark", orderId % 3 == 0 ? "少放辣" : "");
        
        List<Map<String, Object>> orderItems = new java.util.ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("itemId", 1000L + i);
            item.put("itemName", "菜品" + i);
            item.put("itemImage", "https://example.com/food" + i + ".jpg");
            item.put("price", 15.0 + i * 2.0);
            item.put("quantity", 2);
            item.put("subtotal", (15.0 + i * 2.0) * 2);
            orderItems.add(item);
        }
        result.put("orderItems", orderItems);
        
        Map<String, Object> deliveryInfo = new HashMap<>();
        deliveryInfo.put("deliveryManId", 30000L + (orderId % 100));
        deliveryInfo.put("deliveryManName", "配送员" + (orderId % 100));
        deliveryInfo.put("deliveryManPhone", "139****5678");
        deliveryInfo.put("deliveryManAvatar", "https://example.com/delivery" + (orderId % 100) + ".jpg");
        deliveryInfo.put("deliveryStatus", statusIndex >= 3 ? "配送中" : "待接单");
        result.put("deliveryInfo", deliveryInfo);

        return R.success(result);
    }

    /**
     * 获取所有订单列表（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param orderNo 订单号（可选）
     * @param shopName 店铺名称（可选）
     * @param status 订单状态（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 订单列表数据
     */
    @GetMapping("/orders")
    public R<Map<String, Object>> getAllOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String shopName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        log.info("获取所有订单列表，页码: {}, 每页数量: {}, 订单号: {}, 店铺名称: {}, 状态: {}, 开始时间: {}, 结束时间: {}", 
                 page, pageSize, orderNo, shopName, status, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> orderList = new java.util.ArrayList<>();
        
        String[] statuses = {"待支付", "已支付", "处理中", "配送中", "已完成", "已取消"};
        String[] statusesEn = {"pending", "paid", "processing", "delivering", "completed", "cancelled"};
        
        for (int i = 1; i <= pageSize; i++) {
            Map<String, Object> orderData = new HashMap<>();
            
            orderData.put("orderId", 10000L + i);
            orderData.put("orderNo", "ORD" + String.format("%08d", 10000 + i));
            orderData.put("shopId", 1000L + i);
            orderData.put("shopName", "美味餐厅" + i);
            orderData.put("shopType", "中餐");
            
            orderData.put("userId", 20000L + i);
            orderData.put("userName", "用户" + i);
            orderData.put("userPhone", "138****" + String.format("%04d", 1000 + i));
            
            int statusIndex = (i - 1) % 6;
            orderData.put("status", statuses[statusIndex]);
            orderData.put("statusEn", statusesEn[statusIndex]);
            
            orderData.put("totalAmount", 50.0 + i * 5.0);
            orderData.put("discountAmount", 5.0 + i * 0.5);
            orderData.put("deliveryFee", 3.0);
            orderData.put("actualAmount", 48.0 + i * 4.5);
            
            orderData.put("orderTime", "2024-02-08 " + String.format("%02d", 9 + i) + ":30:00");
            orderData.put("deliveryTime", statusIndex >= 3 ? "2024-02-08 " + String.format("%02d", 10 + i) + ":30:00" : null);
            orderData.put("completeTime", statusIndex == 4 ? "2024-02-08 " + String.format("%02d", 11 + i) + ":30:00" : null);
            
            orderData.put("address", "北京市朝阳区xxx街道xxx号");
            orderData.put("remark", i % 3 == 0 ? "少放辣" : "");
            
            orderList.add(orderData);
        }
        
        result.put("list", orderList);
        result.put("total", 1256);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", 126);

        return R.success(result);
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 订单状态
     * @return 更新结果
     */
    @PutMapping("/orders/{orderId}/status")
    public R<Map<String, Object>> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, Integer> params) {
        log.info("更新订单状态，订单ID: {}, 状态: {}", orderId, params.get("status"));
        
        Integer status = params.get("status");
        if (status == null) {
            return R.error("订单状态不能为空");
        }
        
        if (status < 0 || status > 5) {
            return R.error("订单状态不合法");
        }
        
        String[] statusNames = {"待支付", "已支付", "处理中", "配送中", "已完成", "已取消"};
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("oldStatus", "待支付");
        result.put("newStatus", statusNames[status]);
        result.put("status", status);
        result.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        result.put("message", "订单状态更新成功");
        
        return R.success(result);
    }

    @GetMapping("/user/stats")
    public R<Map<String, Object>> getUserStats() {
        log.info("获取用户统计数据");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 12580);
        stats.put("activeUsers", 8560);
        stats.put("newUsersToday", 156);
        stats.put("newUsersThisMonth", 3420);
        stats.put("onlineUsers", 2340);

        Map<String, Object> userGrowth = new HashMap<>();
        userGrowth.put("today", 12.5);
        userGrowth.put("week", 8.3);
        userGrowth.put("month", 15.7);
        stats.put("userGrowth", userGrowth);

        Map<String, Object> userActivity = new HashMap<>();
        userActivity.put("dailyActive", 4560);
        userActivity.put("weeklyActive", 7890);
        userActivity.put("monthlyActive", 11200);
        stats.put("userActivity", userActivity);

        return R.success(stats);
    }

    @GetMapping("/user/gender-distribution")
    public R<Map<String, Object>> getUserGenderDistribution() {
        log.info("获取用户性别分布数据");

        Map<String, Object> distribution = new HashMap<>();
        
        List<Map<String, Object>> genderData = new java.util.ArrayList<>();
        
        Map<String, Object> male = new HashMap<>();
        male.put("gender", "男");
        male.put("genderEn", "male");
        male.put("count", 6280);
        male.put("percentage", 49.9);
        genderData.add(male);
        
        Map<String, Object> female = new HashMap<>();
        female.put("gender", "女");
        female.put("genderEn", "female");
        female.put("count", 6300);
        female.put("percentage", 50.1);
        genderData.add(female);
        
        distribution.put("genderData", genderData);
        distribution.put("total", 12580);

        return R.success(distribution);
    }

    @GetMapping("/user/age-distribution")
    public R<Map<String, Object>> getUserAgeDistribution() {
        log.info("获取用户年龄分布数据");

        Map<String, Object> distribution = new HashMap<>();
        
        List<Map<String, Object>> ageData = new java.util.ArrayList<>();
        
        Map<String, Object> age1 = new HashMap<>();
        age1.put("ageRange", "18岁以下");
        age1.put("ageRangeEn", "under 18");
        age1.put("count", 890);
        age1.put("percentage", 7.1);
        ageData.add(age1);
        
        Map<String, Object> age2 = new HashMap<>();
        age2.put("ageRange", "18-25岁");
        age2.put("ageRangeEn", "18-25");
        age2.put("count", 3150);
        age2.put("percentage", 25.0);
        ageData.add(age2);
        
        Map<String, Object> age3 = new HashMap<>();
        age3.put("ageRange", "26-35岁");
        age3.put("ageRangeEn", "26-35");
        age3.put("count", 4280);
        age3.put("percentage", 34.0);
        ageData.add(age3);
        
        Map<String, Object> age4 = new HashMap<>();
        age4.put("ageRange", "36-45岁");
        age4.put("ageRangeEn", "36-45");
        age4.put("count", 2680);
        age4.put("percentage", 21.3);
        ageData.add(age4);
        
        Map<String, Object> age5 = new HashMap<>();
        age5.put("ageRange", "46-55岁");
        age5.put("ageRangeEn", "46-55");
        age5.put("count", 1180);
        age5.put("percentage", 9.4);
        ageData.add(age5);
        
        Map<String, Object> age6 = new HashMap<>();
        age6.put("ageRange", "55岁以上");
        age6.put("ageRangeEn", "over 55");
        age6.put("count", 400);
        age6.put("percentage", 3.2);
        ageData.add(age6);
        
        distribution.put("ageData", ageData);
        distribution.put("total", 12580);

        return R.success(distribution);
    }

    @GetMapping("/user/consumption")
    public R<Map<String, Object>> getUserConsumption() {
        log.info("获取用户消费数据");

        Map<String, Object> consumption = new HashMap<>();
        
        consumption.put("totalConsumption", 1258000.0);
        consumption.put("averageConsumption", 100.0);
        consumption.put("maxConsumption", 5680.0);
        consumption.put("minConsumption", 15.0);
        
        Map<String, Object> consumptionDistribution = new HashMap<>();
        consumptionDistribution.put("under50", 3780);
        consumptionDistribution.put("under50Percentage", 30.1);
        consumptionDistribution.put("50to100", 4520);
        consumptionDistribution.put("50to100Percentage", 35.9);
        consumptionDistribution.put("100to200", 2890);
        consumptionDistribution.put("100to200Percentage", 23.0);
        consumptionDistribution.put("200to500", 1100);
        consumptionDistribution.put("200to500Percentage", 8.7);
        consumptionDistribution.put("over500", 290);
        consumptionDistribution.put("over500Percentage", 2.3);
        consumption.put("consumptionDistribution", consumptionDistribution);
        
        List<Map<String, Object>> monthlyConsumption = new java.util.ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", i);
            monthData.put("consumption", 95000.0 + i * 2500.0);
            monthData.put("userCount", 10500 + i * 180);
            monthlyConsumption.add(monthData);
        }
        consumption.put("monthlyConsumption", monthlyConsumption);
        
        Map<String, Object> topConsumers = new HashMap<>();
        topConsumers.put("top10", 156.0);
        topConsumers.put("top20", 234.0);
        topConsumers.put("top50", 312.0);
        topConsumers.put("average", 100.0);
        consumption.put("topConsumers", topConsumers);

        return R.success(consumption);
    }

    @GetMapping("/user/list")
    public R<Map<String, Object>> getUserList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "keyword", required = false) String keyword,
                                            @RequestParam(value = "status", required = false) Integer status) {
        log.info("获取用户列表：page={}, pageSize={}, keyword={}, status={}", page, pageSize, keyword, status);

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> userList = new java.util.ArrayList<>();
        
        for (int i = 1; i <= pageSize; i++) {
            int userId = (page - 1) * pageSize + i;
            Map<String, Object> user = new HashMap<>();
            user.put("id", userId);
            user.put("username", "user" + userId);
            user.put("nickname", "用户" + userId);
            user.put("phone", "138****" + String.format("%04d", userId % 10000));
            user.put("avatar", "https://example.com/avatar/" + userId + ".jpg");
            user.put("gender", i % 2 == 0 ? "男" : "女");
            user.put("age", 18 + (userId % 40));
            user.put("status", status != null ? status : (i % 5 == 0 ? 0 : 1));
            user.put("registerTime", "2024-01-" + String.format("%02d", (userId % 28 + 1)) + " 10:30:00");
            user.put("lastLoginTime", "2024-02-08 " + String.format("%02d", (userId % 24)) + ":30:00");
            user.put("totalOrders", 10 + (userId % 50));
            user.put("totalConsumption", 100.0 + (userId % 500));
            userList.add(user);
        }
        
        result.put("list", userList);
        result.put("total", 12580);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", 1258);

        return R.success(result);
    }

    @GetMapping("/user/trend")
    public R<Map<String, Object>> getUserTrend(@RequestParam(value = "days", defaultValue = "30") Integer days) {
        log.info("获取用户趋势数据：days={}", days);

        Map<String, Object> trend = new HashMap<>();
        
        List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        
        for (int i = 1; i <= days; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", "2024-01-" + String.format("%02d", (i % 28 + 1)));
            dayData.put("newUsers", 100 + (i % 50));
            dayData.put("activeUsers", 800 + (i % 200));
            dayData.put("onlineUsers", 200 + (i % 50));
            trendData.add(dayData);
        }
        
        trend.put("trendData", trendData);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalNewUsers", 3420);
        summary.put("avgActiveUsers", 8560);
        summary.put("avgOnlineUsers", 2340);
        summary.put("growthRate", 15.7);
        trend.put("summary", summary);

        return R.success(trend);
    }
}
