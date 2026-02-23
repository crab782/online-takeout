package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.dto.OrdersSubmitDTO;
import com.test.takeout.entity.AddressBook;
import com.test.takeout.entity.OrderDetail;
import com.test.takeout.entity.Orders;
import com.test.takeout.entity.ShoppingCart;
import com.test.takeout.service.AddressBookService;
import com.test.takeout.service.OrderDetailService;
import com.test.takeout.service.OrdersService;
import com.test.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpServletRequest;
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

    public OrderController(OrdersService ordersService,
                       AddressBookService addressBookService,
                       OrderDetailService orderDetailService,
                       ShoppingCartService shoppingCartService,
                       HttpServletRequest request) {
        this.ordersService = ordersService;
        this.addressBookService = addressBookService;
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
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

        Orders confirmOrder = new Orders();
        confirmOrder.setId(orders.getId());
        confirmOrder.setStatus(5);
        confirmOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(confirmOrder);
        if (success) {
            log.info("订单确认收货成功，订单ID：{}，状态：5（已完成）", orders.getId());
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
     * @param orders 订单信息
     * @return 操作结果
     */
    @PutMapping
    public R<String> update(@RequestBody Orders orders) {
        log.info("修改订单状态：orders={}", orders);

        Orders oldOrder = ordersService.getById(orders.getId());
        if (oldOrder == null) {
            log.error("订单不存在，订单ID：{}", orders.getId());
            return R.error("订单不存在");
        }

        // 处理状态转换（支持字符串状态和数字状态）
        Integer status = orders.getStatus();
        if (status == null) {
            // 尝试从请求体中获取字符串状态
            try {
                // 从请求体中获取原始数据
                String requestBody = request.getReader().lines().collect(java.util.stream.Collectors.joining(System.lineSeparator()));
                com.fasterxml.jackson.databind.JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(requestBody);
                if (jsonNode.has("status")) {
                    String statusStr = jsonNode.get("status").asText();
                    // 转换字符串状态为数字状态
                    status = convertStatusStringToInt(statusStr);
                    if (status == null) {
                        log.error("无效的订单状态：{}", statusStr);
                        return R.error("无效的订单状态");
                    }
                    orders.setStatus(status);
                }
            } catch (Exception e) {
                log.error("解析状态失败：{}", e.getMessage());
                return R.error("解析状态失败");
            }
        }

        String statusText = getStatusText(status);
        log.info("订单状态变更：订单ID={}，订单号={}，原状态={}，新状态={}（{}）", 
                orders.getId(), oldOrder.getNumber(), oldOrder.getStatus(), status, statusText);

        boolean success = ordersService.updateById(orders);
        if (success) {
            log.info("订单状态更新成功：订单ID={}，订单号={}，新状态={}（{}）", 
                    orders.getId(), oldOrder.getNumber(), status, statusText);
            return R.success("修改订单状态成功");
        } else {
            log.error("订单状态更新失败：订单ID={}，订单号={}，目标状态={}（{}）", 
                    orders.getId(), oldOrder.getNumber(), status, statusText);
            return R.error("修改订单状态失败");
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

        // 已取消订单数（状态为2）
        long cancelledOrders = todayOrders.stream().filter(order -> order.getStatus() == 2).count();
        stats.put("cancelledOrders", cancelledOrders);

        // 已支付订单数（支付状态为1）
        long paidOrders = todayOrders.stream().filter(order -> order.getPayStatus() == 1).count();
        stats.put("paidOrders", paidOrders);

        // 未支付订单数（支付状态为0）
        long unpaidOrders = todayOrders.stream().filter(order -> order.getPayStatus() == 0).count();
        stats.put("unpaidOrders", unpaidOrders);

        // 总金额（已支付的订单）
        BigDecimal totalAmount = todayOrders.stream()
                .filter(order -> order.getPayStatus() == 1)
                .map(Orders::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalAmount", totalAmount);

        return R.success(stats);
    }

    /**
     * 再来一单（基于历史订单重新下单）
     * @param orders 订单信息（必须包含订单ID）
     * @return 操作结果
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        log.info("再来一单：orders={}", orders);

        Long userId = (Long) request.getAttribute("userId");

        Orders originalOrder = ordersService.getById(orders.getId());
        if (originalOrder == null) {
            return R.error("原订单不存在");
        }

        LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(OrderDetail::getOrderId, orders.getId());
        List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);

        if (orderDetails == null || orderDetails.isEmpty()) {
            return R.error("订单明细不存在");
        }

        for (OrderDetail orderDetail : orderDetails) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setDishId(orderDetail.getDishId());
            shoppingCart.setSetmealId(orderDetail.getSetmealId());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setAmount(orderDetail.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());

            LambdaQueryWrapper<ShoppingCart> cartQueryWrapper = new LambdaQueryWrapper<>();
            cartQueryWrapper.eq(ShoppingCart::getUserId, userId);
            if (orderDetail.getDishId() != null) {
                cartQueryWrapper.eq(ShoppingCart::getDishId, orderDetail.getDishId());
            } else if (orderDetail.getSetmealId() != null) {
                cartQueryWrapper.eq(ShoppingCart::getSetmealId, orderDetail.getSetmealId());
            }

            ShoppingCart existingCart = shoppingCartService.getOne(cartQueryWrapper);
            if (existingCart != null) {
                existingCart.setNumber(existingCart.getNumber() + orderDetail.getNumber());
                shoppingCartService.updateById(existingCart);
            } else {
                shoppingCartService.save(shoppingCart);
            }
        }

        return R.success("已将订单商品添加到购物车");
    }

    /**
     * 查询订单详情
     * @param id 订单ID
     * @return 订单详情（包含订单明细）
     */
    @GetMapping("/detail")
    public R<Map<String, Object>> detail(@RequestParam("id") Long id) {
        log.info("查询订单详情：id={}", id);

        if (id == null) {
            return R.error("订单ID不能为空");
        }

        Orders order = ordersService.getById(id);
        if (order == null) {
            return R.error("订单不存在");
        }

        LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderDetails", orderDetails);

        return R.success(result);
    }

    /**
     * 支付订单
     * @param orders 订单信息（必须包含订单ID和支付方式）
     * @return 操作结果
     */
    @PostMapping("/pay")
    public R<String> pay(@RequestBody Orders orders) {
        log.info("支付订单：orders={}", orders);

        if (orders.getId() == null) {
            return R.error("订单ID不能为空");
        }
        if (orders.getPayMethod() == null) {
            return R.error("支付方式不能为空");
        }

        Orders payOrder = new Orders();
        payOrder.setId(orders.getId());
        payOrder.setPayMethod(orders.getPayMethod());
        payOrder.setPayStatus(1);
        payOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(payOrder);
        if (success) {
            return R.success("支付成功");
        } else {
            return R.error("支付失败");
        }
    }

    /**
     * 提交订单
     * @param ordersSubmitDTO 订单提交信息
     * @return 订单提交结果
     */
    @PostMapping("/submit")
    public R<Orders> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("提交订单：ordersSubmitDTO={}", ordersSubmitDTO);

        Long userId = (Long) request.getAttribute("userId");

        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            return R.error("收货地址不存在");
        }

        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setAmount(ordersSubmitDTO.getAmount());
        orders.setReceiver(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setStatus(1);
        orders.setPayStatus(0);
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());

        ordersService.save(orders);

        log.info("订单创建成功，订单ID：{}，订单号：{}，状态：1（商家已接单）", orders.getId(), orders.getNumber());

        List<OrdersSubmitDTO.OrderDetailDTO> orderDetails = ordersSubmitDTO.getOrderDetails();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (OrdersSubmitDTO.OrderDetailDTO detailDTO : orderDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orders.getId());
                orderDetail.setDishId(detailDTO.getDishId());
                orderDetail.setSetmealId(detailDTO.getSetmealId());
                orderDetail.setNumber(detailDTO.getNumber());
                orderDetail.setAmount(detailDTO.getAmount());
                orderDetailService.save(orderDetail);
            }
        }

        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));

        return R.success(orders);
    }

}