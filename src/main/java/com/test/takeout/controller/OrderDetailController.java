package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.Orders;
import com.test.takeout.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 订单详情控制器，处理订单详情相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    private final OrdersService ordersService;

    public OrderDetailController(OrdersService ordersService) {
        this.ordersService = ordersService;
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

        return R.success(orders);
    }

}