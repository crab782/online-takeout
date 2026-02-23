package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.OrderDetail;
import com.test.takeout.entity.Orders;
import com.test.takeout.service.OrderDetailService;
import com.test.takeout.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单详情控制器，处理订单详情相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    private final OrdersService ordersService;
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrdersService ordersService, OrderDetailService orderDetailService) {
        this.ordersService = ordersService;
        this.orderDetailService = orderDetailService;
    }

    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public R<Orders> getOrderDetailById(@PathVariable Long id) {
        log.info("根据ID查询订单详情：id={}", id);

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