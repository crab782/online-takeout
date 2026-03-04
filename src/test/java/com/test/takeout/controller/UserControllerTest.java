package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.User;
import com.test.takeout.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserInfo() {
        // 模拟数据
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setName("测试用户");

        // 模拟service方法
        when(userService.getById(1L)).thenReturn(user);

        // 调用controller方法
        R<User> response = userController.getUserInfo();

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(userService, times(1)).getById(1L);
    }

    @Test
    void testGetUserInfo_NotFound() {
        // 模拟service方法
        when(userService.getById(1L)).thenReturn(null);

        // 调用controller方法
        R<User> response = userController.getUserInfo();

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("用户信息不存在", response.getMsg());
        verify(userService, times(1)).getById(1L);
    }

    @Test
    void testGetUserInfo_Exception() {
        // 模拟service方法抛出异常
        when(userService.getById(1L)).thenThrow(new RuntimeException("测试异常"));

        // 调用controller方法
        R<User> response = userController.getUserInfo();

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("系统内部错误", response.getMsg());
        verify(userService, times(1)).getById(1L);
    }

    @Test
    void testLogout() {
        // 调用controller方法
        R<String> response = userController.logout();

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("退出成功", response.getMsg());
    }
}
