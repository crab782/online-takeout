package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.Setmeal;
import com.test.takeout.entity.SetmealDish;
import com.test.takeout.service.SetmealDishService;
import com.test.takeout.service.SetmealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SetmealControllerTest {

    @Mock
    private SetmealService setmealService;

    @Mock
    private SetmealDishService setmealDishService;

    @InjectMocks
    private SetmealController setmealController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_Success() {
        // 准备测试数据
        Map<String, Object> setmealData = new HashMap<>();
        setmealData.put("name", "测试套餐");
        setmealData.put("categoryId", 1); // 数字类型
        setmealData.put("price", 99.99); // 数字类型
        setmealData.put("description", "测试套餐描述");
        setmealData.put("image", "test.jpg");
        setmealData.put("status", 1); // 数字类型
        setmealData.put("storeId", 1); // 数字类型

        // 准备套餐菜品数据
        List<Map<String, Object>> setmealDishes = new ArrayList<>();
        Map<String, Object> dish1 = new HashMap<>();
        dish1.put("dishId", 1);
        dish1.put("name", "测试菜品1");
        dish1.put("price", 19.99);
        dish1.put("copies", 1);
        setmealDishes.add(dish1);
        setmealData.put("setmealDishes", setmealDishes);

        // 模拟方法调用
        when(setmealService.save(any(Setmeal.class))).thenReturn(true);
        when(setmealDishService.save(any(SetmealDish.class))).thenReturn(true);

        // 执行测试
        R<String> result = setmealController.save(setmealData);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("新增成功", result.getMsg());
        verify(setmealService, times(1)).save(any(Setmeal.class));
        verify(setmealDishService, times(1)).save(any(SetmealDish.class));
    }

    @Test
    void testSave_StringParameters_Success() {
        // 准备测试数据，使用字符串类型的参数
        Map<String, Object> setmealData = new HashMap<>();
        setmealData.put("name", "测试套餐");
        setmealData.put("categoryId", "1"); // 字符串类型
        setmealData.put("price", "99.99"); // 字符串类型
        setmealData.put("description", "测试套餐描述");
        setmealData.put("image", "test.jpg");
        setmealData.put("status", "1"); // 字符串类型
        setmealData.put("storeId", "1"); // 字符串类型

        // 准备套餐菜品数据
        List<Map<String, Object>> setmealDishes = new ArrayList<>();
        Map<String, Object> dish1 = new HashMap<>();
        dish1.put("dishId", "1"); // 字符串类型
        dish1.put("name", "测试菜品1");
        dish1.put("price", "19.99"); // 字符串类型
        dish1.put("copies", "1"); // 字符串类型
        setmealDishes.add(dish1);
        setmealData.put("setmealDishes", setmealDishes);

        // 模拟方法调用
        when(setmealService.save(any(Setmeal.class))).thenReturn(true);
        when(setmealDishService.save(any(SetmealDish.class))).thenReturn(true);

        // 执行测试
        R<String> result = setmealController.save(setmealData);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("新增成功", result.getMsg());
        verify(setmealService, times(1)).save(any(Setmeal.class));
        verify(setmealDishService, times(1)).save(any(SetmealDish.class));
    }

    @Test
    void testSave_NoDishes_Success() {
        // 准备测试数据，不包含菜品
        Map<String, Object> setmealData = new HashMap<>();
        setmealData.put("name", "测试套餐");
        setmealData.put("categoryId", 1);
        setmealData.put("price", 99.99);
        setmealData.put("description", "测试套餐描述");
        setmealData.put("image", "test.jpg");
        setmealData.put("status", 1);
        setmealData.put("storeId", 1);
        // 不设置setmealDishes

        // 模拟方法调用
        when(setmealService.save(any(Setmeal.class))).thenReturn(true);

        // 执行测试
        R<String> result = setmealController.save(setmealData);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("新增成功", result.getMsg());
        verify(setmealService, times(1)).save(any(Setmeal.class));
        verify(setmealDishService, never()).save(any(SetmealDish.class));
    }

    @Test
    void testSave_Failure() {
        // 准备测试数据
        Map<String, Object> setmealData = new HashMap<>();
        setmealData.put("name", "测试套餐");
        setmealData.put("categoryId", 1);
        setmealData.put("price", 99.99);
        setmealData.put("description", "测试套餐描述");
        setmealData.put("image", "test.jpg");
        setmealData.put("status", 1);
        setmealData.put("storeId", 1);

        // 模拟方法调用，返回失败
        when(setmealService.save(any(Setmeal.class))).thenReturn(false);

        // 执行测试
        R<String> result = setmealController.save(setmealData);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("新增失败", result.getMsg());
        verify(setmealService, times(1)).save(any(Setmeal.class));
        verify(setmealDishService, never()).save(any(SetmealDish.class));
    }

    @Test
    void testSave_Exception() {
        // 准备测试数据
        Map<String, Object> setmealData = new HashMap<>();
        setmealData.put("name", "测试套餐");
        setmealData.put("categoryId", 1);
        setmealData.put("price", 99.99);
        setmealData.put("description", "测试套餐描述");
        setmealData.put("image", "test.jpg");
        setmealData.put("status", 1);
        setmealData.put("storeId", 1);

        // 模拟方法调用，抛出异常
        when(setmealService.save(any(Setmeal.class))).thenThrow(new RuntimeException("保存失败"));

        // 执行测试
        R<String> result = setmealController.save(setmealData);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertTrue(result.getMsg().contains("新增失败"));
        verify(setmealService, times(1)).save(any(Setmeal.class));
        verify(setmealDishService, never()).save(any(SetmealDish.class));
    }
}
