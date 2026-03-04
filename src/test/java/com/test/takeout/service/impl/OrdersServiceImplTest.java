package com.test.takeout.service.impl;

import com.test.takeout.entity.Orders;
import com.test.takeout.service.OrdersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrdersServiceImplTest {

    @Mock
    private OrdersService ordersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // 模拟数据
        Orders order = new Orders();
        order.setNumber("ORDER001");
        order.setUserId(1L);
        order.setStoreId(1L);
        order.setStatus(1);

        // 模拟service方法
        when(ordersService.save(order)).thenReturn(true);

        // 调用service方法
        boolean result = ordersService.save(order);

        // 验证结果
        assertTrue(result);
        verify(ordersService, times(1)).save(order);
    }

    @Test
    void testSave_Failure() {
        // 模拟数据
        Orders order = new Orders();
        order.setNumber("ORDER001");

        // 模拟service方法
        when(ordersService.save(order)).thenReturn(false);

        // 调用service方法
        boolean result = ordersService.save(order);

        // 验证结果
        assertFalse(result);
        verify(ordersService, times(1)).save(order);
    }

    @Test
    void testUpdateById() {
        // 模拟数据
        Orders order = new Orders();
        order.setId(1L);
        order.setNumber("ORDER001");
        order.setStatus(2);

        // 模拟service方法
        when(ordersService.updateById(order)).thenReturn(true);

        // 调用service方法
        boolean result = ordersService.updateById(order);

        // 验证结果
        assertTrue(result);
        verify(ordersService, times(1)).updateById(order);
    }

    @Test
    void testUpdateById_Failure() {
        // 模拟数据
        Orders order = new Orders();
        order.setId(1L);
        order.setStatus(2);

        // 模拟service方法
        when(ordersService.updateById(order)).thenReturn(false);

        // 调用service方法
        boolean result = ordersService.updateById(order);

        // 验证结果
        assertFalse(result);
        verify(ordersService, times(1)).updateById(order);
    }

    @Test
    void testRemoveById() {
        // 模拟数据
        Long id = 1L;

        // 模拟service方法
        when(ordersService.removeById(id)).thenReturn(true);

        // 调用service方法
        boolean result = ordersService.removeById(id);

        // 验证结果
        assertTrue(result);
        verify(ordersService, times(1)).removeById(id);
    }

    @Test
    void testRemoveById_Failure() {
        // 模拟数据
        Long id = 1L;

        // 模拟service方法
        when(ordersService.removeById(id)).thenReturn(false);

        // 调用service方法
        boolean result = ordersService.removeById(id);

        // 验证结果
        assertFalse(result);
        verify(ordersService, times(1)).removeById(id);
    }

    @Test
    void testGetById() {
        // 模拟数据
        Long id = 1L;
        Orders order = new Orders();
        order.setId(id);
        order.setNumber("ORDER001");
        order.setStatus(1);

        // 模拟service方法
        when(ordersService.getById(id)).thenReturn(order);

        // 调用service方法
        Orders result = ordersService.getById(id);

        // 验证结果
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("ORDER001", result.getNumber());
        verify(ordersService, times(1)).getById(id);
    }

    @Test
    void testGetById_NotFound() {
        // 模拟数据
        Long id = 999L;

        // 模拟service方法
        when(ordersService.getById(id)).thenReturn(null);

        // 调用service方法
        Orders result = ordersService.getById(id);

        // 验证结果
        assertNull(result);
        verify(ordersService, times(1)).getById(id);
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
        when(ordersService.list()).thenReturn(orders);

        // 调用service方法
        List<Orders> result = ordersService.list();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORDER001", result.get(0).getNumber());
        assertEquals("ORDER002", result.get(1).getNumber());
        verify(ordersService, times(1)).list();
    }

    @Test
    void testList_Empty() {
        // 模拟service方法
        when(ordersService.list()).thenReturn(new ArrayList<>());

        // 调用service方法
        List<Orders> result = ordersService.list();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ordersService, times(1)).list();
    }
}
