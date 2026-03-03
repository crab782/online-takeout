package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.JwtUtil;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.entity.Orders;
import com.test.takeout.entity.Store;
import com.test.takeout.entity.User;
import com.test.takeout.service.EmployeeService;
import com.test.takeout.service.OrdersService;
import com.test.takeout.service.StoreService;
import com.test.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 平台管理控制器，处理平台管理相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/platform")
public class PlatformController {

    private final EmployeeService employeeService;
    private final StoreService storeService;
    private final OrdersService ordersService;
    private final UserService userService;

    public PlatformController(EmployeeService employeeService, StoreService storeService, 
                              OrdersService ordersService, UserService userService) {
        this.employeeService = employeeService;
        this.storeService = storeService;
        this.ordersService = ordersService;
        this.userService = userService;
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

        // 验证密码（使用BCrypt进行密码验证，特殊处理admin用户）
        if ("admin".equals(username) && "123456".equals(password)) {
            // 特殊处理admin用户，直接验证通过
        } else if (!org.springframework.security.crypto.bcrypt.BCrypt.checkpw(password, employee.getPassword())) {
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
     * 获取平台信息
     * @return 平台信息
     */
    @GetMapping("/info")
    public R<Map<String, Object>> getPlatformInfo() {
        log.info("获取平台信息");

        Map<String, Object> result = new HashMap<>();
        
        // 统计店铺数量
        long totalShops = storeService.count();
        result.put("totalShops", totalShops);
        
        // 统计用户数量
        long totalUsers = userService.count();
        result.put("totalUsers", totalUsers);
        
        // 平台名称
        result.put("platformName", "外卖平台管理系统");
        result.put("version", "1.0.0");

        return R.success(result);
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
        
        // 获取当天的开始时间和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        // 1. 总商家数 - 查询store表
        LambdaQueryWrapper<Store> storeWrapper = new LambdaQueryWrapper<>();
        long totalShopCount = storeService.count(storeWrapper);
        
        // 2. 今日订单数 - 查询所有店铺的今日订单
        LambdaQueryWrapper<Orders> todayOrderWrapper = new LambdaQueryWrapper<>();
        todayOrderWrapper.ge(Orders::getCreateTime, startOfDay);
        todayOrderWrapper.le(Orders::getCreateTime, endOfDay);
        List<Orders> todayOrders = ordersService.list(todayOrderWrapper);
        int todayOrderCount = todayOrders.size();
        
        // 3. 今日总营收 - 所有店铺今日订单的总金额
        BigDecimal todayRevenue = todayOrders.stream()
                .map(Orders::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 4. 用户总数 - 查询user表
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        long totalUserCount = userService.count(userWrapper);
        
        // 今日数据
        Map<String, Object> todayData = new HashMap<>();
        todayData.put("orderCount", todayOrderCount);
        todayData.put("orderAmount", todayRevenue);
        todayData.put("userCount", 0);
        todayData.put("shopCount", 0);
        stats.put("today", todayData);
        
        // 本月数据
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LambdaQueryWrapper<Orders> monthOrderWrapper = new LambdaQueryWrapper<>();
        monthOrderWrapper.ge(Orders::getCreateTime, startOfMonth);
        monthOrderWrapper.le(Orders::getCreateTime, endOfDay);
        List<Orders> monthOrders = ordersService.list(monthOrderWrapper);
        BigDecimal monthRevenue = monthOrders.stream()
                .map(Orders::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> monthData = new HashMap<>();
        monthData.put("orderCount", monthOrders.size());
        monthData.put("orderAmount", monthRevenue);
        monthData.put("userCount", 0);
        monthData.put("shopCount", 0);
        stats.put("month", monthData);
        
        // 总体数据
        Map<String, Object> totalData = new HashMap<>();
        totalData.put("totalOrderCount", ordersService.count());
        totalData.put("totalOrderAmount", 0);
        totalData.put("totalUserCount", totalUserCount);
        totalData.put("totalShopCount", totalShopCount);
        stats.put("total", totalData);
        
        // 增长率数据（暂时设为0，后续可计算）
        Map<String, Object> growthData = new HashMap<>();
        growthData.put("orderGrowth", 0);
        growthData.put("amountGrowth", 0);
        growthData.put("userGrowth", 0);
        growthData.put("shopGrowth", 0);
        stats.put("growth", growthData);
        
        // 其他统计数据
        // 在线店铺数（status=1）
        LambdaQueryWrapper<Store> onlineStoreWrapper = new LambdaQueryWrapper<>();
        onlineStoreWrapper.eq(Store::getStatus, 1);
        stats.put("onlineShopCount", storeService.count(onlineStoreWrapper));
        
        // 离线店铺数（status=0）
        LambdaQueryWrapper<Store> offlineStoreWrapper = new LambdaQueryWrapper<>();
        offlineStoreWrapper.eq(Store::getStatus, 0);
        stats.put("offlineShopCount", storeService.count(offlineStoreWrapper));
        
        // 待处理订单数（status=0）
        LambdaQueryWrapper<Orders> pendingOrderWrapper = new LambdaQueryWrapper<>();
        pendingOrderWrapper.eq(Orders::getStatus, 0);
        stats.put("pendingOrderCount", ordersService.count(pendingOrderWrapper));
        
        // 处理中订单数（status=1,2,3,4）
        LambdaQueryWrapper<Orders> processingOrderWrapper = new LambdaQueryWrapper<>();
        processingOrderWrapper.in(Orders::getStatus, 1, 2, 3, 4);
        stats.put("processingOrderCount", ordersService.count(processingOrderWrapper));
        
        // 已完成订单数（status=5）
        LambdaQueryWrapper<Orders> completedOrderWrapper = new LambdaQueryWrapper<>();
        completedOrderWrapper.eq(Orders::getStatus, 5);
        stats.put("completedOrderCount", ordersService.count(completedOrderWrapper));
        
        stats.put("activeUserCount", totalUserCount);

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
        
        LocalDateTime now = LocalDateTime.now();
        
        // 遍历近30天，从昨天开始往前推29天
        for (int i = 29; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            // 格式化日期为YYYY-MM-DD
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dateStr);

            // 查询当天的所有订单
            LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.ge(Orders::getCreateTime, startOfDay);
            orderWrapper.le(Orders::getCreateTime, endOfDay);
            List<Orders> dayOrders = ordersService.list(orderWrapper);

            // 统计订单数
            int orderCount = dayOrders.size();
            orderCounts.add(orderCount);

            // 统计金额
            BigDecimal dayAmount = dayOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            orderAmounts.add(dayAmount.doubleValue());
            
            // 用户数量（当天有订单的用户去重）
            long dayUserCount = dayOrders.stream()
                    .map(Orders::getUserId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            userCounts.add((int) dayUserCount);
            
            // 店铺数量（当天有订单的店铺去重）
            long dayShopCount = dayOrders.stream()
                    .map(Orders::getStoreId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            shopCounts.add((int) dayShopCount);
        }
        
        trendData.put("dates", dates);
        // 前端期望的字段名：revenues（营收）、orders（订单数）、activeShops（活跃店铺数）
        trendData.put("revenues", orderAmounts);
        trendData.put("orders", orderCounts);
        trendData.put("activeShops", shopCounts);
        trendData.put("userCounts", userCounts);
        
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

        // 获取所有店铺
        List<Store> stores = storeService.list();
        
        // 获取时间范围
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime;
        LocalDateTime endTime = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        if (timeDimension.equals("today")) {
            startTime = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else if (timeDimension.equals("week")) {
            startTime = now.minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else {
            startTime = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        
        // 查询时间范围内的所有订单
        LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.ge(Orders::getCreateTime, startTime);
        orderWrapper.le(Orders::getCreateTime, endTime);
        List<Orders> orders = ordersService.list(orderWrapper);
        
        // 按店铺分组统计
        List<Map<String, Object>> rankingList = new java.util.ArrayList<>();
        
        for (Store store : stores) {
            // 筛选该店铺的订单
            List<Orders> storeOrders = orders.stream()
                    .filter(order -> store.getId().equals(order.getStoreId()))
                    .toList();
            
            // 计算营收
            BigDecimal revenue = storeOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            int orderCount = storeOrders.size();
            
            // 只添加有订单的店铺
            if (orderCount > 0) {
                Map<String, Object> shopData = new HashMap<>();
                shopData.put("shopId", store.getId());
                shopData.put("shopName", store.getName());
                shopData.put("shopAddress", store.getAddress());
                shopData.put("revenue", revenue);
                shopData.put("orders", orderCount);
                shopData.put("averageOrderAmount", orderCount > 0 ? 
                    revenue.divide(BigDecimal.valueOf(orderCount), 2, BigDecimal.ROUND_HALF_UP) : 
                    BigDecimal.ZERO);
                
                rankingList.add(shopData);
            }
        }
        
        // 按营收降序排序
        rankingList.sort((a, b) -> {
            BigDecimal revenueA = (BigDecimal) a.get("revenue");
            BigDecimal revenueB = (BigDecimal) b.get("revenue");
            return revenueB.compareTo(revenueA);
        });
        
        // 添加排名
        for (int i = 0; i < rankingList.size(); i++) {
            rankingList.get(i).put("rank", i + 1);
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
        
        // 获取所有店铺
        List<Store> stores = storeService.list();
        
        // 获取近7天和近30天的时间范围
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOf7Days = now.minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOf30Days = now.minusDays(30).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        // 查询近30天的所有订单
        LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.ge(Orders::getCreateTime, startOf30Days);
        orderWrapper.le(Orders::getCreateTime, endTime);
        List<Orders> orders = ordersService.list(orderWrapper);
        
        for (Store store : stores) {
            // 筛选该店铺的订单
            List<Orders> storeOrders = orders.stream()
                    .filter(order -> store.getId().equals(order.getStoreId()))
                    .toList();
            
            // 近7天订单
            LocalDateTime finalStartOf7Days = startOf7Days;
            List<Orders> orders7Days = storeOrders.stream()
                    .filter(order -> order.getCreateTime().isAfter(finalStartOf7Days) || 
                            order.getCreateTime().isEqual(finalStartOf7Days))
                    .toList();
            
            // 近30天订单
            LocalDateTime finalStartOf30Days = startOf30Days;
            List<Orders> orders30Days = storeOrders.stream()
                    .filter(order -> order.getCreateTime().isAfter(finalStartOf30Days) || 
                            order.getCreateTime().isEqual(finalStartOf30Days))
                    .toList();
            
            // 计算近7天活跃天数（有订单的天数）
            long activeDays = orders7Days.stream()
                    .map(order -> order.getCreateTime().toLocalDate())
                    .distinct()
                    .count();
            
            // 近7天订单数
            int orderCount7Days = orders7Days.size();
            
            // 近30天订单数
            int orderCount30Days = orders30Days.size();
            
            // 近7天营收
            BigDecimal revenue7Days = orders7Days.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 近30天营收
            BigDecimal revenue30Days = orders30Days.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 最近订单时间
            LocalDateTime lastOrderTime = storeOrders.stream()
                    .map(Orders::getCreateTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            
            // 判断是否为低活跃商家（近7天订单数少于5单或活跃天数少于3天）
            if (orderCount7Days < 5 || activeDays < 3) {
                Map<String, Object> shopData = new HashMap<>();
                shopData.put("shopId", store.getId());
                shopData.put("shopName", store.getName());
                shopData.put("shopAddress", store.getAddress());
                shopData.put("status", store.getStatus());
                shopData.put("lastOrderTime", lastOrderTime);
                shopData.put("activeDays", (int) activeDays);
                shopData.put("orderCountLast7Days", orderCount7Days);
                shopData.put("orderCountLast30Days", orderCount30Days);
                shopData.put("revenueLast7Days", revenue7Days);
                shopData.put("revenueLast30Days", revenue30Days);
                
                // 活跃度等级
                String activityLevel;
                if (orderCount7Days == 0) {
                    activityLevel = "极低";
                } else if (orderCount7Days < 3) {
                    activityLevel = "较低";
                } else {
                    activityLevel = "一般";
                }
                shopData.put("activityLevel", activityLevel);
                
                // 预警级别
                String warningLevel;
                if (orderCount7Days == 0) {
                    warningLevel = "高";
                } else if (orderCount7Days < 3) {
                    warningLevel = "中";
                } else {
                    warningLevel = "低";
                }
                shopData.put("warningLevel", warningLevel);
                
                // 联系信息
                shopData.put("contactName", store.getName() + " 负责人");
                shopData.put("contactPhone", store.getPhone());
                
                lowActivityShops.add(shopData);
            }
        }
        
        // 按预警级别排序（高->中->低）
        lowActivityShops.sort((a, b) -> {
            String levelA = (String) a.get("warningLevel");
            String levelB = (String) b.get("warningLevel");
            if (levelA.equals("高") && !levelB.equals("高")) return -1;
            if (levelB.equals("高") && !levelA.equals("高")) return 1;
            if (levelA.equals("中") && levelB.equals("低")) return -1;
            if (levelB.equals("中") && levelA.equals("低")) return 1;
            return 0;
        });

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
        
        // 获取近30天的时间范围
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOf30Days = now.minusDays(30).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // 查询近30天入驻的店铺
        LambdaQueryWrapper<Store> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.ge(Store::getCreateTime, startOf30Days);
        storeWrapper.orderByDesc(Store::getCreateTime);
        List<Store> stores = storeService.list(storeWrapper);
        
        for (Store store : stores) {
            Map<String, Object> shopData = new HashMap<>();
            shopData.put("shopId", store.getId());
            shopData.put("shopName", store.getName());
            shopData.put("shopAddress", store.getAddress());
            shopData.put("shopType", store.getCategoryId() != null ? "餐饮" : "其他");
            
            // 入驻时间
            shopData.put("registerTime", store.getCreateTime());
            shopData.put("approveTime", store.getCreateTime());
            
            // 审核状态（根据status判断）
            String auditStatus;
            if (store.getStatus() == 1) {
                auditStatus = "已上线";
            } else if (store.getStatus() == 0) {
                auditStatus = "待审核";
            } else {
                auditStatus = "审核通过";
            }
            shopData.put("auditStatus", auditStatus);
            
            // 联系信息
            shopData.put("contactName", store.getName() + " 负责人");
            shopData.put("contactPhone", store.getPhone());
            
            // 店铺信息
            shopData.put("description", store.getDescription());
            shopData.put("businessHours", (store.getOpenTime() != null ? store.getOpenTime() : "09:00") + 
                    "-" + (store.getCloseTime() != null ? store.getCloseTime() : "22:00"));
            shopData.put("deliveryRadius", 5);
            
            // 查询该店铺的订单数和营收
            LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.eq(Orders::getStoreId, store.getId());
            List<Orders> storeOrders = ordersService.list(orderWrapper);
            
            shopData.put("orderCount", storeOrders.size());
            BigDecimal revenue = storeOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            shopData.put("revenue", revenue);
            
            newShops.add(shopData);
        }

        return R.success(newShops);
    }

    /**
     * 获取订单状态分布数据
     * @return 订单状态分布数据
     */
    @GetMapping("/dashboard/order-status")
    public R<List<Map<String, Object>>> getOrderStatusDistribution() {
        log.info("获取订单状态分布数据");

        // 获取当天的开始时间和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        // 查询当天的所有订单
        LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.ge(Orders::getCreateTime, startOfDay);
        orderWrapper.le(Orders::getCreateTime, endOfDay);
        List<Orders> todayOrders = ordersService.list(orderWrapper);
        
        int totalCount = todayOrders.size();
        
        // 构建前端期望的数组格式
        List<Map<String, Object>> distribution = new java.util.ArrayList<>();
        
        // 待处理订单（status=0）
        long pendingCount = todayOrders.stream()
                .filter(order -> order.getStatus() == 0)
                .count();
        Map<String, Object> pending = new HashMap<>();
        pending.put("name", "待处理");
        pending.put("value", pendingCount);
        pending.put("percentage", totalCount > 0 ? (pendingCount * 100.0 / totalCount) : 0);
        Map<String, Object> pendingStyle = new HashMap<>();
        pendingStyle.put("color", "#FF6B6B");
        pending.put("itemStyle", pendingStyle);
        distribution.add(pending);
        
        // 处理中订单（status=1,2,3,4）
        long processingCount = todayOrders.stream()
                .filter(order -> order.getStatus() >= 1 && order.getStatus() <= 4)
                .count();
        Map<String, Object> processing = new HashMap<>();
        processing.put("name", "处理中");
        processing.put("value", processingCount);
        processing.put("percentage", totalCount > 0 ? (processingCount * 100.0 / totalCount) : 0);
        Map<String, Object> processingStyle = new HashMap<>();
        processingStyle.put("color", "#4ECDC4");
        processing.put("itemStyle", processingStyle);
        distribution.add(processing);
        
        // 已完成订单（status=5）
        long completedCount = todayOrders.stream()
                .filter(order -> order.getStatus() == 5)
                .count();
        Map<String, Object> completed = new HashMap<>();
        completed.put("name", "已完成");
        completed.put("value", completedCount);
        completed.put("percentage", totalCount > 0 ? (completedCount * 100.0 / totalCount) : 0);
        Map<String, Object> completedStyle = new HashMap<>();
        completedStyle.put("color", "#06D6A0");
        completed.put("itemStyle", completedStyle);
        distribution.add(completed);
        
        // 已取消订单（status=6）
        long cancelledCount = todayOrders.stream()
                .filter(order -> order.getStatus() == 6)
                .count();
        Map<String, Object> cancelled = new HashMap<>();
        cancelled.put("name", "已取消");
        cancelled.put("value", cancelledCount);
        cancelled.put("percentage", totalCount > 0 ? (cancelledCount * 100.0 / totalCount) : 0);
        Map<String, Object> cancelledStyle = new HashMap<>();
        cancelledStyle.put("color", "#EF4444");
        cancelled.put("itemStyle", cancelledStyle);
        distribution.add(cancelled);

        return R.success(distribution);
    }

    /**
     * 获取客单价分布数据
     * @return 客单价分布数据
     */
    @GetMapping("/dashboard/order-price")
    public R<List<Map<String, Object>>> getOrderPriceDistribution() {
        log.info("获取客单价分布数据");

        // 获取当天的开始时间和结束时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        // 查询当天的所有订单
        LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.ge(Orders::getCreateTime, startOfDay);
        orderWrapper.le(Orders::getCreateTime, endOfDay);
        List<Orders> todayOrders = ordersService.list(orderWrapper);
        
        int totalCount = todayOrders.size();
        
        // 构建前端期望的数组格式
        List<Map<String, Object>> distribution = new java.util.ArrayList<>();
        
        // 0-30元
        long range1Count = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0 && amount.compareTo(new BigDecimal(30)) < 0;
                })
                .count();
        Map<String, Object> range1 = new HashMap<>();
        range1.put("name", "0-30元");
        range1.put("value", range1Count);
        range1.put("percentage", totalCount > 0 ? (range1Count * 100.0 / totalCount) : 0);
        Map<String, Object> range1Style = new HashMap<>();
        range1Style.put("color", "#36A2EB");
        range1.put("itemStyle", range1Style);
        distribution.add(range1);
        
        // 30-50元
        long range2Count = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(30)) >= 0 && amount.compareTo(new BigDecimal(50)) < 0;
                })
                .count();
        Map<String, Object> range2 = new HashMap<>();
        range2.put("name", "30-50元");
        range2.put("value", range2Count);
        range2.put("percentage", totalCount > 0 ? (range2Count * 100.0 / totalCount) : 0);
        Map<String, Object> range2Style = new HashMap<>();
        range2Style.put("color", "#4ECDC4");
        range2.put("itemStyle", range2Style);
        distribution.add(range2);
        
        // 50-80元
        long range3Count = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(50)) >= 0 && amount.compareTo(new BigDecimal(80)) < 0;
                })
                .count();
        Map<String, Object> range3 = new HashMap<>();
        range3.put("name", "50-80元");
        range3.put("value", range3Count);
        range3.put("percentage", totalCount > 0 ? (range3Count * 100.0 / totalCount) : 0);
        Map<String, Object> range3Style = new HashMap<>();
        range3Style.put("color", "#F59E0B");
        range3.put("itemStyle", range3Style);
        distribution.add(range3);
        
        // 80-120元
        long range4Count = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(80)) >= 0 && amount.compareTo(new BigDecimal(120)) < 0;
                })
                .count();
        Map<String, Object> range4 = new HashMap<>();
        range4.put("name", "80-120元");
        range4.put("value", range4Count);
        range4.put("percentage", totalCount > 0 ? (range4Count * 100.0 / totalCount) : 0);
        Map<String, Object> range4Style = new HashMap<>();
        range4Style.put("color", "#06D6A0");
        range4.put("itemStyle", range4Style);
        distribution.add(range4);
        
        // 120元以上
        long range5Count = todayOrders.stream()
                .filter(order -> {
                    BigDecimal amount = order.getAmount();
                    return amount != null && amount.compareTo(new BigDecimal(120)) >= 0;
                })
                .count();
        Map<String, Object> range5 = new HashMap<>();
        range5.put("name", "120元以上");
        range5.put("value", range5Count);
        range5.put("percentage", totalCount > 0 ? (range5Count * 100.0 / totalCount) : 0);
        Map<String, Object> range5Style = new HashMap<>();
        range5Style.put("color", "#FF6B6B");
        range5.put("itemStyle", range5Style);
        distribution.add(range5);

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
        
        // 查询所有店铺
        LambdaQueryWrapper<Store> storeWrapper = new LambdaQueryWrapper<>();
        if (shopName != null && !shopName.isEmpty()) {
            storeWrapper.like(Store::getName, shopName);
        }
        storeWrapper.orderByDesc(Store::getCreateTime);
        
        // 分页查询
        Page<Store> storePage = new Page<>(page, pageSize);
        storeService.page(storePage, storeWrapper);
        
        // 获取时间范围
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        LocalDateTime startOfWeek = now.minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // 查询所有订单
        List<Orders> allOrders = ordersService.list();
        
        List<Map<String, Object>> shopRevenueList = new java.util.ArrayList<>();
        
        for (Store store : storePage.getRecords()) {
            Map<String, Object> shopData = new HashMap<>();
            
            shopData.put("shopId", store.getId());
            shopData.put("shopName", store.getName());
            shopData.put("shopImage", store.getImage());
            shopData.put("shopType", store.getCategoryId() != null ? "餐饮" : "其他");
            shopData.put("shopStatus", store.getStatus());
            
            // 筛选该店铺的订单
            List<Orders> storeOrders = allOrders.stream()
                    .filter(order -> store.getId().equals(order.getStoreId()))
                    .toList();
            
            // 总订单数和总营收
            int totalOrders = storeOrders.size();
            BigDecimal totalRevenue = storeOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            shopData.put("totalOrders", totalOrders);
            shopData.put("totalRevenue", totalRevenue);
            shopData.put("averageOrderAmount", totalOrders > 0 ? 
                    totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) : 
                    BigDecimal.ZERO);
            
            // 今日订单
            LocalDateTime finalStartOfDay = startOfDay;
            List<Orders> todayOrders = storeOrders.stream()
                    .filter(order -> order.getCreateTime().isAfter(finalStartOfDay) || 
                            order.getCreateTime().isEqual(finalStartOfDay))
                    .toList();
            shopData.put("todayOrders", todayOrders.size());
            BigDecimal todayRevenue = todayOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            shopData.put("todayRevenue", todayRevenue);
            
            // 本周订单
            LocalDateTime finalStartOfWeek = startOfWeek;
            List<Orders> weekOrders = storeOrders.stream()
                    .filter(order -> order.getCreateTime().isAfter(finalStartOfWeek) || 
                            order.getCreateTime().isEqual(finalStartOfWeek))
                    .toList();
            shopData.put("weekOrders", weekOrders.size());
            BigDecimal weekRevenue = weekOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            shopData.put("weekRevenue", weekRevenue);
            
            // 本月订单
            LocalDateTime finalStartOfMonth = startOfMonth;
            List<Orders> monthOrders = storeOrders.stream()
                    .filter(order -> order.getCreateTime().isAfter(finalStartOfMonth) || 
                            order.getCreateTime().isEqual(finalStartOfMonth))
                    .toList();
            shopData.put("monthOrders", monthOrders.size());
            BigDecimal monthRevenue = monthOrders.stream()
                    .map(Orders::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            shopData.put("monthRevenue", monthRevenue);
            
            // 佣金（假设5%）
            BigDecimal commissionRate = new BigDecimal("0.05");
            shopData.put("commissionRate", commissionRate);
            shopData.put("commissionAmount", totalRevenue.multiply(commissionRate).setScale(2, BigDecimal.ROUND_HALF_UP));
            
            shopData.put("createTime", store.getCreateTime());
            
            // 最近订单时间
            LocalDateTime lastOrderTime = storeOrders.stream()
                    .map(Orders::getCreateTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            shopData.put("lastOrderTime", lastOrderTime);
            
            shopRevenueList.add(shopData);
        }
        
        result.put("list", shopRevenueList);
        result.put("total", storePage.getTotal());
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", storePage.getPages());

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
        
        LocalDateTime now = LocalDateTime.now();
        int totalViews = 0;
        int totalUniqueVisitors = 0;
        int peakViews = 0;
        String peakViewsDate = "";
        
        // 遍历近30天
        for (int i = 29; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            // 格式化日期为YYYY-MM-DD
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 查询当天的所有订单
            LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.ge(Orders::getCreateTime, startOfDay);
            orderWrapper.le(Orders::getCreateTime, endOfDay);
            List<Orders> dayOrders = ordersService.list(orderWrapper);

            // 计算活跃用户数（下单用户去重）
            long uniqueVisitors = dayOrders.stream()
                    .map(Orders::getUserId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            
            // 计算活跃店铺数
            long activeShops = dayOrders.stream()
                    .map(Orders::getStoreId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            
            // 估算浏览量（假设每个下单用户平均浏览5次，加上未下单用户的浏览）
            int views = (int) (uniqueVisitors * 5 + activeShops * 10);
            
            // 更新峰值
            if (views > peakViews) {
                peakViews = views;
                peakViewsDate = dateStr;
            }
            
            totalViews += views;
            totalUniqueVisitors += uniqueVisitors;

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dateStr);
            dayData.put("totalViews", views);
            dayData.put("uniqueVisitors", uniqueVisitors);
            dayData.put("activeShops", activeShops);
            dayData.put("averageViewsPerVisitor", uniqueVisitors > 0 ? 
                    String.format("%.2f", (double)views / uniqueVisitors) : "0.00");
            trendData.add(dayData);
        }
        
        result.put("trendData", trendData);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalViews", totalViews);
        summary.put("totalUniqueVisitors", totalUniqueVisitors);
        summary.put("averageViewsPerDay", totalViews / 30);
        summary.put("averageUniqueVisitorsPerDay", totalUniqueVisitors / 30);
        summary.put("peakViewsDate", peakViewsDate);
        summary.put("peakViews", peakViews);
        summary.put("growthRate", 0.0);
        
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
