package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.Orders;
import com.test.takeout.mapper.OrdersMapper;
import com.test.takeout.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * 订单服务实现类
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
