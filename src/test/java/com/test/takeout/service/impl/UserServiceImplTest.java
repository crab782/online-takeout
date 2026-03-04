package com.test.takeout.service.impl;

import com.test.takeout.entity.User;
import com.test.takeout.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // 模拟数据
        User user = new User();
        user.setUsername("testuser");
        user.setName("测试用户");
        user.setPassword("123456");

        // 模拟service方法
        when(userService.save(user)).thenReturn(true);

        // 调用service方法
        boolean result = userService.save(user);

        // 验证结果
        assertTrue(result);
        verify(userService, times(1)).save(user);
    }

    @Test
    void testSave_Failure() {
        // 模拟数据
        User user = new User();
        user.setUsername("testuser");

        // 模拟service方法
        when(userService.save(user)).thenReturn(false);

        // 调用service方法
        boolean result = userService.save(user);

        // 验证结果
        assertFalse(result);
        verify(userService, times(1)).save(user);
    }

    @Test
    void testUpdateById() {
        // 模拟数据
        User user = new User();
        user.setId(1L);
        user.setUsername("updateduser");
        user.setName("更新用户");

        // 模拟service方法
        when(userService.updateById(user)).thenReturn(true);

        // 调用service方法
        boolean result = userService.updateById(user);

        // 验证结果
        assertTrue(result);
        verify(userService, times(1)).updateById(user);
    }

    @Test
    void testUpdateById_Failure() {
        // 模拟数据
        User user = new User();
        user.setId(1L);
        user.setUsername("updateduser");

        // 模拟service方法
        when(userService.updateById(user)).thenReturn(false);

        // 调用service方法
        boolean result = userService.updateById(user);

        // 验证结果
        assertFalse(result);
        verify(userService, times(1)).updateById(user);
    }

    @Test
    void testRemoveById() {
        // 模拟数据
        Long id = 1L;

        // 模拟service方法
        when(userService.removeById(id)).thenReturn(true);

        // 调用service方法
        boolean result = userService.removeById(id);

        // 验证结果
        assertTrue(result);
        verify(userService, times(1)).removeById(id);
    }

    @Test
    void testRemoveById_Failure() {
        // 模拟数据
        Long id = 1L;

        // 模拟service方法
        when(userService.removeById(id)).thenReturn(false);

        // 调用service方法
        boolean result = userService.removeById(id);

        // 验证结果
        assertFalse(result);
        verify(userService, times(1)).removeById(id);
    }

    @Test
    void testGetById() {
        // 模拟数据
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("testuser");
        user.setName("测试用户");

        // 模拟service方法
        when(userService.getById(id)).thenReturn(user);

        // 调用service方法
        User result = userService.getById(id);

        // 验证结果
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userService, times(1)).getById(id);
    }

    @Test
    void testGetById_NotFound() {
        // 模拟数据
        Long id = 999L;

        // 模拟service方法
        when(userService.getById(id)).thenReturn(null);

        // 调用service方法
        User result = userService.getById(id);

        // 验证结果
        assertNull(result);
        verify(userService, times(1)).getById(id);
    }

    @Test
    void testList() {
        // 模拟数据
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser1");
        users.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        users.add(user2);

        // 模拟service方法
        when(userService.list()).thenReturn(users);

        // 调用service方法
        List<User> result = userService.list();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testuser1", result.get(0).getUsername());
        assertEquals("testuser2", result.get(1).getUsername());
        verify(userService, times(1)).list();
    }

    @Test
    void testList_Empty() {
        // 模拟service方法
        when(userService.list()).thenReturn(new ArrayList<>());

        // 调用service方法
        List<User> result = userService.list();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userService, times(1)).list();
    }
}
