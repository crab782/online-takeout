package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.dto.OrdersSubmitDTO;
import com.test.takeout.dto.OrderStatusUpdateDTO;
import java.util.Map;
import com.test.takeout.entity.AddressBook;
import com.test.takeout.entity.OrderDetail;
import com.test.takeout.entity.Orders;
import com.test.takeout.entity.ShoppingCart;
import com.test.takeout.service.AddressBookService;
import com.test.takeout.service.DishService;
import com.test.takeout.service.OrderDetailService;
import com.test.takeout.service.OrdersService;
import com.test.takeout.service.SetmealDishService;
import com.test.takeout.service.SetmealService;
import com.test.takeout.service.ShoppingCartService;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.entity.Dish;
import com.test.takeout.entity.Setmeal;
import com.test.takeout.entity.SetmealDish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器，处理订单相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrdersService ordersService;

    private final AddressBookService addressBookService;

    private final OrderDetailService orderDetailService;

    private final ShoppingCartService shoppingCartService;

    private final HttpServletRequest request;
    private final StoreBalanceService storeBalanceService;
    private final DishService dishService;
    private final SetmealService setmealService;
    private final SetmealDishService setmealDishService;

    public OrderController(OrdersService ordersService,
                       AddressBookService addressBookService,
                       OrderDetailService orderDetailService,
                       ShoppingCartService shoppingCartService,
                       StoreBalanceService storeBalanceService,
                       DishService dishService,
                       SetmealService setmealService,
                       SetmealDishService setmealDishService,
                       HttpServletRequest request) {
        this.ordersService = ordersService;
        this.addressBookService = addressBookService;
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
        this.storeBalanceService = storeBalanceService;
        this.dishService = dishService;
        this.setmealService = setmealService;
        this.setmealDishService = setmealDishService;
        this.request = request;
    }

    /**
     * 分页查询订单列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param shopId 店铺ID（可选）
     * @param orderNumber 订单号（可选）
     * @param beginTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "shopId", required = false) Long shopId,
                                @RequestParam(value = "orderNumber", required = false) String orderNumber,
                                @RequestParam(value = "beginTime", required = false) String beginTime,
                                @RequestParam(value = "endTime", required = false) String endTime,
                                @RequestParam(value = "status", required = false) String status) {
        log.info("分页查询订单列表：page={}, pageSize={}, shopId={}, orderNumber={}, beginTime={}, endTime={}, status={}", page, pageSize, shopId, orderNumber, beginTime, endTime, status);

        // 构建分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        // 构建条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据店铺ID过滤
        if (shopId != null) {
            queryWrapper.eq(Orders::getStoreId, shopId);
        }
        
        // 根据订单号过滤
        if (orderNumber != null && !orderNumber.isEmpty()) {
            queryWrapper.like(Orders::getNumber, orderNumber);
        }
        
        // 根据开始时间过滤
        if (beginTime != null && !beginTime.isEmpty()) {
            try {
                LocalDateTime startTime = java.time.LocalDateTime.parse(beginTime.replace(" ", "T"));
                queryWrapper.ge(Orders::getCreateTime, startTime);
            } catch (Exception e) {
                log.error("开始时间格式错误：{}", beginTime);
            }
        }
        
        // 根据结束时间过滤
        if (endTime != null && !endTime.isEmpty()) {
            try {
                LocalDateTime endTimeObj = java.time.LocalDateTime.parse(endTime.replace(" ", "T"));
                queryWrapper.le(Orders::getCreateTime, endTimeObj);
            } catch (Exception e) {
                log.error("结束时间格式错误：{}", endTime);
            }
        }
        
        // 根据订单状态过滤
        if (status != null && !status.isEmpty()) {
            // 处理状态字符串，支持多个状态（逗号分隔）
            String[] statusArray = status.split(",");
            if (statusArray.length > 0) {
                queryWrapper.in(Orders::getStatus, java.util.Arrays.stream(statusArray)
                    .map(this::getStatusFromString)
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toList()));
            }
        }
        
        // 添加排序条件，按照订单创建时间降序排列
        queryWrapper.orderByDesc(Orders::getCreateTime);

        // 执行查询
        ordersService.page(pageInfo, queryWrapper);

        // 为每个订单查询对应的订单详情（菜品信息）
        List<Orders> ordersList = pageInfo.getRecords();
        for (Orders order : ordersList) {
            // 查询该订单的所有订单详情
            LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);
            
            // 为套餐类型的订单详情添加菜品描述
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getSetmealId() != null) {
                    // 查询套餐包含的菜品
                    LambdaQueryWrapper<SetmealDish> setmealDishQuery = new LambdaQueryWrapper<>();
                    setmealDishQuery.eq(SetmealDish::getSetmealId, orderDetail.getSetmealId());
                    List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishQuery);
                    
                    // 构建菜品描述字符串
                    if (setmealDishes != null && !setmealDishes.isEmpty()) {
                        StringBuilder dishNames = new StringBuilder();
                        for (int i = 0; i < setmealDishes.size(); i++) {
                            SetmealDish dish = setmealDishes.get(i);
                            dishNames.append(dish.getName()).append("×").append(dish.getCopies());
                            if (i < setmealDishes.size() - 1) {
                                dishNames.append("、");
                            }
                        }
                        orderDetail.setDishDesc(dishNames.toString());
                    }
                }
            }
            
            // 将订单详情设置到订单对象中
            order.setOrderDetails(orderDetails);
        }

        return R.success(pageInfo);
    }

    /**
     * 查询当前用户的所有订单
     * @return 订单列表
     */
    @GetMapping("/list")
    public R<List<Orders>> list() {
        log.info("查询当前用户的所有订单");

        Long userId = (Long) request.getAttribute("userId");

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        List<Orders> list = ordersService.list(queryWrapper);

        // 为每个订单查询对应的订单详情（菜品信息）
        for (Orders order : list) {
            // 查询该订单的所有订单详情
            LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);
            
            // 将订单详情设置到订单对象中
            order.setOrderDetails(orderDetails);
        }

        return R.success(list);
    }

    /**
     * 分页查询当前用户的订单
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页订单数据
     */
    @GetMapping("/userPage")
    public R<Page<Orders>> userPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("分页查询当前用户的订单：page={}, pageSize={}", page, pageSize);

        Long userId = (Long) request.getAttribute("userId");

        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        ordersService.page(pageInfo, queryWrapper);

        // 为每个订单查询对应的订单详情（菜品信息）
        List<Orders> ordersList = pageInfo.getRecords();
        for (Orders order : ordersList) {
            // 查询该订单的所有订单详情
            LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);
            
            // 将订单详情设置到订单对象中
            order.setOrderDetails(orderDetails);
        }

        return R.success(pageInfo);
    }

    /**
     * 取消订单
     * @param orders 订单信息（必须包含订单ID）
     * @return 操作结果
     */
    @PutMapping("/cancel")
    public R<String> cancel(@RequestBody Orders orders) {
        log.info("取消订单：orders={}", orders);

        if (orders.getId() == null) {
            return R.error("订单ID不能为空");
        }

        Orders cancelOrder = new Orders();
        cancelOrder.setId(orders.getId());
        cancelOrder.setStatus(6);
        cancelOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(cancelOrder);
        if (success) {
            log.info("订单取消成功，订单ID：{}，状态：6（已取消）", orders.getId());
            return R.success("取消订单成功");
        } else {
            return R.error("取消订单失败");
        }
    }

    /**
     * 确认收货
     * @param orders 订单信息（必须包含订单ID）
     * @return 操作结果
     */
    @PutMapping("/confirm")
    public R<String> confirm(@RequestBody Orders orders) {
        log.info("确认收货：orders={}", orders);

        if (orders.getId() == null) {
            return R.error("订单ID不能为空");
        }

        // 查询订单信息，获取店铺ID和金额
        Orders oldOrder = ordersService.getById(orders.getId());
        if (oldOrder == null) {
            return R.error("订单不存在");
        }

        Orders confirmOrder = new Orders();
        confirmOrder.setId(orders.getId());
        confirmOrder.setStatus(5);
        confirmOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(confirmOrder);
        if (success) {
            // 订单完成，增加店铺余额
            storeBalanceService.addBalance(oldOrder.getStoreId(), oldOrder.getAmount());
            log.info("订单确认收货成功，订单ID：{}，状态：5（已完成），店铺ID：{}，增加余额：{}", 
                    orders.getId(), oldOrder.getStoreId(), oldOrder.getAmount());
            return R.success("确认收货成功");
        } else {
            return R.error("确认收货失败");
        }
    }

    /**
     * 更新订单状态
     * @param orders 订单信息（必须包含订单ID和新状态）
     * @return 操作结果
     */
    @PutMapping("/status")
    public R<String> updateStatus(@RequestBody Orders orders) {
        log.info("更新订单状态：orders={}", orders);

        if (orders.getId() == null) {
            return R.error("订单ID不能为空");
        }
        if (orders.getStatus() == null) {
            return R.error("订单状态不能为空");
        }

        Orders oldOrder = ordersService.getById(orders.getId());
        if (oldOrder == null) {
            log.error("订单不存在，订单ID：{}", orders.getId());
            return R.error("订单不存在");
        }

        String statusText = getStatusText(orders.getStatus());
        log.info("订单状态变更：订单ID={}，订单号={}，原状态={}，新状态={}（{}）", 
                orders.getId(), oldOrder.getNumber(), oldOrder.getStatus(), orders.getStatus(), statusText);

        Orders updateOrder = new Orders();
        updateOrder.setId(orders.getId());
        updateOrder.setStatus(orders.getStatus());
        updateOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(updateOrder);
        if (success) {
            // 当订单状态更新为已完成时，增加店铺余额
            if (orders.getStatus() == 5) {
                storeBalanceService.addBalance(oldOrder.getStoreId(), oldOrder.getAmount());
                log.info("订单完成，增加店铺余额：店铺ID={}，金额={}", oldOrder.getStoreId(), oldOrder.getAmount());
            }
            log.info("订单状态更新成功：订单ID={}，订单号={}，新状态={}（{}）", 
                    orders.getId(), oldOrder.getNumber(), orders.getStatus(), statusText);
            return R.success("更新订单状态成功");
        } else {
            log.error("订单状态更新失败：订单ID={}，订单号={}，目标状态={}（{}）", 
                    orders.getId(), oldOrder.getNumber(), orders.getStatus(), statusText);
            return R.error("更新订单状态失败");
        }
    }

    /**
     * 修改订单状态（取消/派送/完成）
     * @param requestBody 请求体
     * @return 操作结果
     */
    @PutMapping
    public R<String> update(@RequestBody Map<String, Object> requestBody) {
        log.info("修改订单状态：requestBody={}", requestBody);

        try {
            // 获取订单ID
            Long orderId = ((Number) requestBody.get("id")).longValue();
            if (orderId == null) {
                log.error("订单ID不能为空");
                return R.error("订单ID不能为空");
            }

            // 查询订单
            Orders oldOrder = ordersService.getById(orderId);
            if (oldOrder == null) {
                log.error("订单不存在，订单ID：{}", orderId);
                return R.error("订单不存在");
            }

            // 获取状态
            Object statusObj = requestBody.get("status");
            Integer status = null;

            if (statusObj != null) {
                if (statusObj instanceof Integer) {
                    // 数字状态
                    status = (Integer) statusObj;
                } else if (statusObj instanceof String) {
                    // 字符串状态
                    String statusStr = (String) statusObj;
                    status = convertStatusStringToInt(statusStr);
                    if (status == null) {
                        log.error("无效的订单状态：{}", statusStr);
                        return R.error("无效的订单状态");
                    }
                }
            }

            if (status == null) {
                log.error("订单状态不能为空");
                return R.error("订单状态不能为空");
            }

            String statusText = getStatusText(status);
            log.info("订单状态变更：订单ID={}，订单号={}，原状态={}，新状态={}（{}）", 
                    orderId, oldOrder.getNumber(), oldOrder.getStatus(), status, statusText);

            // 创建更新对象
            Orders updateOrder = new Orders();
            updateOrder.setId(orderId);
            updateOrder.setStatus(status);
            updateOrder.setUpdateTime(LocalDateTime.now());

            boolean success = ordersService.updateById(updateOrder);
            if (success) {
                // 当订单状态更新为已完成时，增加店铺余额
                if (status == 5) {
                    storeBalanceService.addBalance(oldOrder.getStoreId(), oldOrder.getAmount());
                    log.info("订单完成，增加店铺余额：店铺ID={}，金额={}", oldOrder.getStoreId(), oldOrder.getAmount());
                }
                log.info("订单状态更新成功：订单ID={}，订单号={}，新状态={}（{}）", 
                        orderId, oldOrder.getNumber(), status, statusText);
                return R.success("修改订单状态成功");
            } else {
                log.error("订单状态更新失败：订单ID={}，订单号={}，目标状态={}（{}）", 
                        orderId, oldOrder.getNumber(), status, statusText);
                return R.error("修改订单状态失败");
            }
        } catch (Exception e) {
            log.error("修改订单状态失败：{}", e.getMessage(), e);
            return R.error("系统内部错误");
        }
    }

    /**
     * 将字符串状态转换为数字状态码
     * @param statusStr 字符串状态
     * @return 数字状态码
     */
    private Integer convertStatusStringToInt(String statusStr) {
        if (statusStr == null) {
            return null;
        }
        switch (statusStr) {
            case "pending":
                return 0;
            case "pay_success":
                return 1;
            case "accepted":
                return 2;
            case "preparing":
                return 3;
            case "rider_accepted":
                return 4;
            case "delivering":
                return 5;
            case "completed":
                return 5;
            case "cancelled":
                return 6;
            default:
                // 尝试直接转换为数字
                try {
                    return Integer.parseInt(statusStr);
                } catch (NumberFormatException e) {
                    return null;
                }
        }
    }

    /**
     * 将字符串状态转换为数字状态码（用于状态过滤）
     * @param statusStr 字符串状态
     * @return 数字状态码
     */
    private Integer getStatusFromString(String statusStr) {
        if (statusStr == null) {
            return null;
        }
        switch (statusStr) {
            case "pending":
                return 0;
            case "pay_success":
                return 1;
            case "accepted":
                return 2;
            case "preparing":
                return 3;
            case "rider_accepted":
                return 4;
            case "delivering":
                return 4;
            case "delivered":
                return 4;
            case "completed":
                return 5;
            case "cancelled":
                return 6;
            default:
                // 尝试直接转换为数字
                try {
                    return Integer.parseInt(statusStr);
                } catch (NumberFormatException e) {
                    return null;
                }
        }
    }

    /**
     * 获取订单状态文本
     * @param status 订单状态
     * @return 状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知状态";
        }
        switch (status) {
            case 0:
                return "待处理";
            case 1:
                return "商家已接单";
            case 2:
                return "准备中";
            case 3:
                return "骑手已接单";
            case 4:
                return "配送中";
            case 5:
                return "已完成";
            case 6:
                return "已取消";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取当天订单统计
     * @return 当天订单统计数据
     */
    @GetMapping("/today-stats")
    public R<Map<String, Object>> todayStats() {
        log.info("获取当天订单统计");

        // 获取当天开始时间（00:00:00）
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        // 获取当天结束时间（23:59:59）
        LocalDateTime todayEnd = LocalDateTime.now().with(LocalTime.MAX);

        // 查询当天所有订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Orders::getCreateTime, todayStart);
        queryWrapper.le(Orders::getCreateTime, todayEnd);
        List<Orders> todayOrders = ordersService.list(queryWrapper);

        // 统计数据
        Map<String, Object> stats = new HashMap<>();

        // 总订单数
        int totalOrders = todayOrders.size();
        stats.put("totalOrders", totalOrders);

        // 待处理订单数（状态为0）
        long pendingOrders = todayOrders.stream().filter(order -> order.getStatus() == 0).count();
        stats.put("pendingOrders", pendingOrders);

        // 已完成订单数（状态为1）
        long completedOrders = todayOrders.stream().filter(order -> order.getStatus() == 1).count();
        stats.put("completedOrders", completedOrders);

        // 计算总销售额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Orders order : todayOrders) {
            if (order.getAmount() != null) {
                totalAmount = totalAmount.add(order.getAmount());
            }
        }
        stats.put("totalAmount", totalAmount);

        return R.success(stats);
    }

    /**
     * 再来一单
     * @param orders 订单信息（必须包含订单ID）
     * @return 操作结果
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        log.info("再来一单：orders={}", orders);

        if (orders.getId() == null) {
            return R.error("订单ID不能为空");
        }

        // 查询原订单
        Orders oldOrder = ordersService.getById(orders.getId());
        if (oldOrder == null) {
            return R.error("订单不存在");
        }

        // 查询原订单的订单详情
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orders.getId());
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);

        // 将原订单的菜品添加到购物车
        Long userId = (Long) request.getAttribute("userId");
        for (OrderDetail detail : orderDetails) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setDishId(detail.getDishId());
            shoppingCart.setSetmealId(detail.getSetmealId());
            shoppingCart.setNumber(detail.getNumber());
            shoppingCart.setAmount(detail.getAmount());
            shoppingCartService.save(shoppingCart);
        }

        return R.success("再来一单成功");
    }

    /**
     * 提交订单
     * @param ordersSubmitDTO 订单提交信息
     * @return 订单提交结果
     */
    @PostMapping("/submit")
    @Transactional
    public R<Orders> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("提交订单：ordersSubmitDTO={}", ordersSubmitDTO);

        try {
            Long userId = (Long) request.getAttribute("userId");

            // 使用默认地址
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBook::getUserId, userId);
            queryWrapper.eq(AddressBook::getIsDefault, 1);
            AddressBook addressBook = addressBookService.getOne(queryWrapper);
            
            if (addressBook == null) {
                return R.error("请先设置默认收货地址");
            }

            // 检查库存
            List<OrdersSubmitDTO.OrderDetailDTO> orderDetails = ordersSubmitDTO.getOrderDetails();
            if (orderDetails != null && !orderDetails.isEmpty()) {
                for (OrdersSubmitDTO.OrderDetailDTO detailDTO : orderDetails) {
                    // 检查菜品库存
                    if (detailDTO.getDishId() != null) {
                        Dish dish = dishService.getById(detailDTO.getDishId());
                        if (dish == null) {
                            return R.error("商品【" + detailDTO.getName() + "】不存在");
                        }
                        if (dish.getStock() == null || dish.getStock() < detailDTO.getNumber()) {
                            return R.error("商品【" + dish.getName() + "】库存不足，当前库存：" + 
                                    (dish.getStock() != null ? dish.getStock() : 0) + "，需要：" + detailDTO.getNumber());
                        }
                    }
                    // 检查套餐库存（套餐库存取包含菜品的最小库存）
                    if (detailDTO.getSetmealId() != null) {
                        Setmeal setmeal = setmealService.getById(detailDTO.getSetmealId());
                        if (setmeal == null) {
                            return R.error("套餐【" + detailDTO.getName() + "】不存在");
                        }
                        // 获取套餐包含的菜品
                        LambdaQueryWrapper<SetmealDish> setmealDishQuery = new LambdaQueryWrapper<>();
                        setmealDishQuery.eq(SetmealDish::getSetmealId, detailDTO.getSetmealId());
                        List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishQuery);
                        
                        for (SetmealDish setmealDish : setmealDishes) {
                            Dish dish = dishService.getById(setmealDish.getDishId());
                            if (dish == null) {
                                return R.error("套餐【" + setmeal.getName() + "】中的商品不存在");
                            }
                            // 计算套餐所需菜品数量 = 套餐份数 * 每份套餐中该菜品的数量
                            int requiredDishCount = detailDTO.getNumber() * setmealDish.getCopies();
                            if (dish.getStock() == null || dish.getStock() < requiredDishCount) {
                                return R.error("套餐【" + setmeal.getName() + "】库存不足，商品【" + 
                                        dish.getName() + "】当前库存：" + (dish.getStock() != null ? dish.getStock() : 0) + 
                                        "，需要：" + requiredDishCount);
                            }
                        }
                    }
                }
            }

            Orders orders = new Orders();
            orders.setUserId(userId);
            orders.setStoreId(ordersSubmitDTO.getStoreId());
            orders.setStoreName(ordersSubmitDTO.getStoreName());
            orders.setAmount(ordersSubmitDTO.getAmount());
            orders.setReceiver(addressBook.getConsignee());
            orders.setAddress(addressBook.getDetail());
            orders.setPhone(addressBook.getPhone());
            orders.setStatus(0);
            orders.setPayStatus(0);
            orders.setCreateTime(LocalDateTime.now());
            orders.setUpdateTime(LocalDateTime.now());
            
            // 生成订单号
            String orderNumber = String.valueOf(System.currentTimeMillis());
            orders.setNumber(orderNumber);

            ordersService.save(orders);

            log.info("订单创建成功，订单ID：{}，订单号：{}，店铺ID：{}，店铺名称：{}，状态：0（待付款）", 
                    orders.getId(), orders.getNumber(), ordersSubmitDTO.getStoreId(), ordersSubmitDTO.getStoreName());

            if (orderDetails != null && !orderDetails.isEmpty()) {
                for (OrdersSubmitDTO.OrderDetailDTO detailDTO : orderDetails) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orders.getId());
                    orderDetail.setDishId(detailDTO.getDishId());
                    orderDetail.setSetmealId(detailDTO.getSetmealId());
                    orderDetail.setName(detailDTO.getName());
                    orderDetail.setNumber(detailDTO.getNumber());
                    orderDetail.setAmount(detailDTO.getAmount());
                    orderDetail.setImage(detailDTO.getImage());
                    orderDetailService.save(orderDetail);

                    // 更新库存
                    if (detailDTO.getDishId() != null) {
                        // 更新菜品库存
                        Dish dish = dishService.getById(detailDTO.getDishId());
                        if (dish != null) {
                            dish.setStock(dish.getStock() - detailDTO.getNumber());
                            // 更新库存状态
                            if (dish.getStock() <= 0) {
                                dish.setStockStatus(0); // 售罄
                            } else if (dish.getStock() < 10) {
                                dish.setStockStatus(2); // 紧张
                            } else {
                                dish.setStockStatus(1); // 充足
                            }
                            dishService.updateById(dish);
                            log.info("更新菜品库存：菜品ID={}，名称={}，原库存={}，减少数量={}，新库存={}",
                                    dish.getId(), dish.getName(), dish.getStock() + detailDTO.getNumber(), detailDTO.getNumber(), dish.getStock());
                        }
                    } else if (detailDTO.getSetmealId() != null) {
                        // 更新套餐包含的菜品库存
                        LambdaQueryWrapper<SetmealDish> setmealDishQuery = new LambdaQueryWrapper<>();
                        setmealDishQuery.eq(SetmealDish::getSetmealId, detailDTO.getSetmealId());
                        List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishQuery);
                        
                        for (SetmealDish setmealDish : setmealDishes) {
                            Dish dish = dishService.getById(setmealDish.getDishId());
                            if (dish != null) {
                                int requiredDishCount = detailDTO.getNumber() * setmealDish.getCopies();
                                dish.setStock(dish.getStock() - requiredDishCount);
                                // 更新库存状态
                                if (dish.getStock() <= 0) {
                                    dish.setStockStatus(0); // 售罄
                                } else if (dish.getStock() < 10) {
                                    dish.setStockStatus(2); // 紧张
                                } else {
                                    dish.setStockStatus(1); // 充足
                                }
                                dishService.updateById(dish);
                                log.info("更新套餐菜品库存：菜品ID={}，名称={}，原库存={}，减少数量={}，新库存={}",
                                        dish.getId(), dish.getName(), dish.getStock() + requiredDishCount, requiredDishCount, dish.getStock());
                            }
                        }
                    }
                }
            }

            // 只清空该店铺的购物车商品
            LambdaQueryWrapper<ShoppingCart> cartQueryWrapper = new LambdaQueryWrapper<>();
            cartQueryWrapper.eq(ShoppingCart::getUserId, userId);
            cartQueryWrapper.eq(ShoppingCart::getStoreId, ordersSubmitDTO.getStoreId());
            shoppingCartService.remove(cartQueryWrapper);

            return R.success(orders);
        } catch (Exception e) {
            log.error("创建订单失败：", e);
            return R.error("系统内部错误：" + e.getMessage());
        }
    }

    /**
     * 支付订单
     * @param paymentInfo 支付信息，包含订单ID和支付方式
     * @return 支付结果
     */
    @PostMapping("/pay")
    public R<String> pay(@RequestBody Map<String, Object> paymentInfo) {
        log.info("支付订单：paymentInfo={}", paymentInfo);

        try {
            if (paymentInfo == null) {
                return R.error("支付信息不能为空");
            }

            Object idObj = paymentInfo.get("id");
            if (idObj == null) {
                return R.error("订单ID不能为空");
            }

            Long orderId = null;
            if (idObj instanceof Number) {
                orderId = ((Number) idObj).longValue();
            } else if (idObj instanceof String) {
                try {
                    orderId = Long.parseLong((String) idObj);
                } catch (NumberFormatException e) {
                    return R.error("订单ID格式错误");
                }
            }

            if (orderId == null) {
                return R.error("订单ID不能为空");
            }

            String paymentMethod = (String) paymentInfo.get("paymentMethod");
            if (paymentMethod == null) {
                paymentMethod = "alipay"; // 默认支付宝
            }

            // 查询订单
            Orders order = ordersService.getById(orderId);
            if (order == null) {
                return R.error("订单不存在");
            }

            // 更新订单支付状态和订单状态
            Orders updateOrder = new Orders();
            updateOrder.setId(orderId);
            updateOrder.setPayStatus(1); // 支付成功
            updateOrder.setPayMethod(paymentMethod.equals("alipay") ? 1 : 2); // 1:支付宝，2:微信
            updateOrder.setStatus(2); // 自动接单，状态变为商家接单
            updateOrder.setUpdateTime(LocalDateTime.now());

            boolean success = ordersService.updateById(updateOrder);
            if (success) {
                log.info("订单支付成功，订单ID：{}，订单号：{}，支付方式：{}", 
                        orderId, order.getNumber(), paymentMethod);
                return R.success("支付成功");
            } else {
                log.error("订单支付状态更新失败，订单ID：{}", orderId);
                return R.error("支付失败");
            }
        } catch (Exception e) {
            log.error("支付订单失败：", e);
            return R.error("系统内部错误：" + e.getMessage());
        }
    }

    /**
     * 查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/detail")
    public R<Orders> detail(@RequestParam Long id) {
        log.info("查询订单详情：id={}", id);

        Orders orders = ordersService.getById(id);
        if (orders == null) {
            return R.error("订单不存在");
        }

        // 查询该订单的所有订单详情（商品信息）
        LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);

        // 将订单详情设置到订单对象中
        orders.setOrderDetails(orderDetails);

        return R.success(orders);
    }
}
