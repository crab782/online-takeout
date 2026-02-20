package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.User;
import com.test.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器，处理用户相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/backend/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取后端用户信息
     * @return 用户信息
     */
    @GetMapping("/info")
    public R<User> getUserInfo() {
        log.info("开始获取后端用户信息");
        try {
            // 查询用户信息，这里假设只有一个用户，查询ID为1的用户
            User user = userService.getById(1L);
            if (user == null) {
                log.warn("用户信息不存在");
                return R.error("用户信息不存在");
            }
            log.info("返回用户信息成功");
            return R.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return R.error("系统内部错误");
        }
    }

    /**
     * 后端用户退出
     * @return 退出结果
     */
    @PostMapping("/logout")
    public R<String> logout() {
        log.info("后端用户退出");

        return R.success("退出成功");
    }

}