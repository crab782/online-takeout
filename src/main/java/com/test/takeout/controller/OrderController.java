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

    public OrderController(OrdersService ordersService,
                       AddressBookService addressBookService,
                       OrderDetailService orderDetailService,
                       ShoppingCartService shoppingCartService) {
        this.ordersService = ordersService;
        this.addressBookService = addressBookService;
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * 分页查询订单列表
     * @param page 页码
     * @param pageSize 每页数量
     * @return 订单列表
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("分页查询订单列表：page={}, pageSize={}", page, pageSize);

        // 构建分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        // 构建条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，按照订单创建时间降序排列
        queryWrapper.orderByDesc(Orders::getCreateTime);

        // 执行查询
        ordersService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 查询当前用户的所有订单
     * @return 订单列表
     */
    @GetMapping("/list")
    public R<List<Orders>> list() {
        log.info("查询当前用户的所有订单");

        Long userId = 1L;

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        List<Orders> list = ordersService.list(queryWrapper);

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

        Long userId = 1L;

        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        ordersService.page(pageInfo, queryWrapper);

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
        cancelOrder.setStatus(2);
        cancelOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(cancelOrder);
        if (success) {
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
        confirmOrder.setStatus(1);
        confirmOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(confirmOrder);
        if (success) {
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

        Orders updateOrder = new Orders();
        updateOrder.setId(orders.getId());
        updateOrder.setStatus(orders.getStatus());
        updateOrder.setUpdateTime(LocalDateTime.now());

        boolean success = ordersService.updateById(updateOrder);
        if (success) {
            return R.success("更新订单状态成功");
        } else {
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

        boolean success = ordersService.updateById(orders);
        if (success) {
            return R.success("修改订单状态成功");
        } else {
            return R.error("修改订单状态失败");
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

        Long userId = 1L;

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

        Long userId = 1L;

        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            return R.error("收货地址不存在");
        }

        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setAmount(ordersSubmitDTO.getAmount());
        orders.setReceiver(addressBook.getName());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setStatus(0);
        orders.setPayStatus(0);
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());

        ordersService.save(orders);

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