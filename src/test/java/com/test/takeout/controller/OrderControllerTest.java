package com.test.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.dto.OrdersSubmitDTO;
import com.test.takeout.entity.Orders;
import com.test.takeout.service.AddressBookService;
import com.test.takeout.service.OrderDetailService;
import com.test.takeout.service.OrdersService;
import com.test.takeout.service.ShoppingCartService;
import com.test.takeout.service.StoreBalanceService;
import com.test.takeout.service.DishService;
import com.test.takeout.service.SetmealService;
import com.test.takeout.service.SetmealDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrdersService ordersService;

    @Mock
    private AddressBookService addressBookService;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private StoreBalanceService storeBalanceService;

    @Mock
    private DishService dishService;

    @Mock
    private SetmealService setmealService;

    @Mock
    private SetmealDishService setmealDishService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 模拟 request.getAttribute("userId") 返回 1L
        when(request.getAttribute("userId")).thenReturn(1L);
        // 模拟 orderDetailService.list() 返回空列表
        when(orderDetailService.list(any())).thenReturn(new ArrayList<>());
    }

    @Test
    void testList() {
        // 模拟数据
        List<Orders> orders = new ArrayList<>();
        Orders order1 = new Orders();
        order1.setId(1L);
        order1.setNumber("ORDER001");
        order1.setStatus(1);
        orders.add(order1);

        Orders order2 = new Orders();
        order2.setId(2L);
        order2.setNumber("ORDER002");
        order2.setStatus(2);
        orders.add(order2);

        // 模拟service方法
        when(ordersService.list(any())).thenReturn(orders);

        // 调用controller方法
        R<List<Orders>> response = orderController.list();

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(ordersService, times(1)).list(any());
    }

    @Test
    void testPage() {
        // 模拟数据
        Page<Orders> pageInfo = new Page<>(1, 10);
        Orders order = new Orders();
        order.setId(1L);
        order.setNumber("ORDER001");
        order.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(order));

        // 模拟service方法
        when(ordersService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Orders>> response = orderController.page(1, 10, 1L, "ORDER001", "2024-01-01", "2024-01-31", "1");

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(ordersService, times(1)).page(any(Page.class), any());
    }

    @Test
    void testUserPage() {
        // 模拟数据
        Page<Orders> pageInfo = new Page<>(1, 10);
        Orders order = new Orders();
        order.setId(1L);
        order.setNumber("ORDER001");
        order.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(order));

        // 模拟service方法
        when(ordersService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Orders>> response = orderController.userPage(1, 10);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(ordersService, times(1)).page(any(Page.class), any());
    }

    @Test
    void testUpdate() {
        // 模拟数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1L);
        requestBody.put("status", 2);

        Orders order = new Orders();
        order.setId(1L);
        order.setNumber("ORDER001");
        order.setStatus(1);
        order.setStoreId(1L);
        order.setAmount(new BigDecimal(100));

        // 模拟service方法
        when(ordersService.getById(1L)).thenReturn(order);
        when(ordersService.updateById(any(Orders.class))).thenReturn(true);
        when(storeBalanceService.addBalance(anyLong(), any(BigDecimal.class))).thenReturn(true);

        // 调用controller方法
        R<String> response = orderController.update(requestBody);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("修改订单状态成功", response.getMsg());
        verify(ordersService, times(1)).getById(1L);
        verify(ordersService, times(1)).updateById(any(Orders.class));
    }

    @Test
    void testUpdate_Failure() {
        // 模拟数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1L);
        requestBody.put("status", 2);

        // 模拟service方法
        when(ordersService.getById(1L)).thenReturn(null);

        // 调用controller方法
        R<String> response = orderController.update(requestBody);

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("订单不存在", response.getMsg());
        verify(ordersService, times(1)).getById(1L);
    }

    @Test
    void testDetail() {
        // 模拟数据
        Long id = 1L;
        Orders order = new Orders();
        order.setId(id);
        order.setNumber("ORDER001");
        order.setStatus(1);

        // 模拟service方法
        when(ordersService.getById(id)).thenReturn(order);

        // 调用controller方法
        R<Orders> response = orderController.detail(id);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(ordersService, times(1)).getById(id);
    }

    @Test
    void testDetail_NotFound() {
        // 模拟数据
        Long id = 999L;

        // 模拟service方法
        when(ordersService.getById(id)).thenReturn(null);

        // 调用controller方法
        R<Orders> response = orderController.detail(id);

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("订单不存在", response.getMsg());
        verify(ordersService, times(1)).getById(id);
    }

    @Test
    void testCancel() {
        // 模拟数据
        Orders orders = new Orders();
        orders.setId(1L);

        // 模拟service方法
        when(ordersService.updateById(any(Orders.class))).thenReturn(true);

        // 调用controller方法
        R<String> response = orderController.cancel(orders);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("取消订单成功", response.getMsg());
        verify(ordersService, times(1)).updateById(any(Orders.class));
    }

    @Test
    void testConfirm() {
        // 模拟数据
        Orders orders = new Orders();
        orders.setId(1L);

        Orders oldOrder = new Orders();
        oldOrder.setId(1L);
        oldOrder.setStoreId(1L);
        oldOrder.setAmount(new BigDecimal(100));

        // 模拟service方法
        when(ordersService.getById(1L)).thenReturn(oldOrder);
        when(ordersService.updateById(any(Orders.class))).thenReturn(true);
        when(storeBalanceService.addBalance(anyLong(), any(BigDecimal.class))).thenReturn(true);

        // 调用controller方法
        R<String> response = orderController.confirm(orders);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("确认收货成功", response.getMsg());
        verify(ordersService, times(1)).getById(1L);
        verify(ordersService, times(1)).updateById(any(Orders.class));
    }

    @Test
    void testUpdateStatus() {
        // 模拟数据
        Orders orders = new Orders();
        orders.setId(1L);
        orders.setStatus(2);

        Orders oldOrder = new Orders();
        oldOrder.setId(1L);
        oldOrder.setStoreId(1L);
        oldOrder.setAmount(new BigDecimal(100));

        // 模拟service方法
        when(ordersService.getById(1L)).thenReturn(oldOrder);
        when(ordersService.updateById(any(Orders.class))).thenReturn(true);
        when(storeBalanceService.addBalance(anyLong(), any(BigDecimal.class))).thenReturn(true);

        // 调用controller方法
        R<String> response = orderController.updateStatus(orders);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("更新订单状态成功", response.getMsg());
        verify(ordersService, times(1)).getById(1L);
        verify(ordersService, times(1)).updateById(any(Orders.class));
    }

    @Test
    void testTodayStats() {
        // 模拟数据
        List<Orders> orders = new ArrayList<>();
        Orders order1 = new Orders();
        order1.setStatus(0);
        order1.setAmount(new BigDecimal(100));
        orders.add(order1);

        Orders order2 = new Orders();
        order2.setStatus(1);
        order2.setAmount(new BigDecimal(200));
        orders.add(order2);

        // 模拟service方法
        when(ordersService.list(any())).thenReturn(orders);

        // 调用controller方法
        R<Map<String, Object>> response = orderController.todayStats();

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(ordersService, times(1)).list(any());
    }

    @Test
    void testAgain() {
        // 模拟数据
        Orders orders = new Orders();
        orders.setId(1L);

        Orders oldOrder = new Orders();
        oldOrder.setId(1L);

        // 模拟service方法
        when(ordersService.getById(1L)).thenReturn(oldOrder);
        when(orderDetailService.list(any())).thenReturn(new ArrayList<>());
        when(shoppingCartService.save(any())).thenReturn(true);

        // 调用controller方法
        R<String> response = orderController.again(orders);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("再来一单成功", response.getMsg());
        verify(ordersService, times(1)).getById(1L);
    }

    @Test
    void testPay() {
        // 模拟数据
        Map<String, Object> paymentInfo = new HashMap<>();
        paymentInfo.put("id", 1L);
        paymentInfo.put("paymentMethod", "alipay");

        Orders order = new Orders();
        order.setId(1L);
        order.setNumber("ORDER001");

        // 模拟service方法
        when(ordersService.getById(1L)).thenReturn(order);
        when(ordersService.updateById(any(Orders.class))).thenReturn(true);

        // 调用controller方法
        R<String> response = orderController.pay(paymentInfo);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("支付成功", response.getMsg());
        verify(ordersService, times(1)).getById(1L);
        verify(ordersService, times(1)).updateById(any(Orders.class));
    }
}
