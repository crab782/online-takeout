package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.User;
import com.test.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端用户控制器，处理前端用户相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class FrontUserController {

    private final UserService userService;

    public FrontUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * @param registerData 注册数据
     * @return 注册结果
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody Map<String, String> registerData) {
        log.info("用户注册：registerData={}", registerData);

        String phone = registerData.get("phone");
        String password = registerData.get("password");

        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }

        if (password == null || password.isEmpty()) {
            return R.error("密码不能为空");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User existingUser = userService.getOne(queryWrapper);

        if (existingUser != null) {
            return R.error("该手机号已注册");
        }

        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        boolean success = userService.save(user);
        if (success) {
            return R.success("注册成功");
        } else {
            return R.error("注册失败");
        }
    }

    /**
     * 手机号密码登录
     * @param loginData 登录数据
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        log.info("手机号密码登录：loginData={}", loginData);

        String phone = loginData.get("phone");
        String password = loginData.get("password");

        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }

        if (password == null || password.isEmpty()) {
            return R.error("密码不能为空");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        queryWrapper.eq(User::getPassword, password);
        User user = userService.getOne(queryWrapper);

        if (user == null) {
            return R.error("手机号或密码错误");
        }

        if (user.getStatus() == 0) {
            return R.error("账号已被禁用");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", "mock_token_" + System.currentTimeMillis());
        result.put("userId", user.getId());
        result.put("phone", phone);
        result.put("username", user.getUsername());
        result.put("avatar", user.getAvatar());
        
        return R.success(result);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @GetMapping("/info")
    public R<User> getUserInfo() {
        log.info("获取当前登录用户信息");

        Long userId = 1L;
        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        user.setPassword(null);
        
        return R.success(user);
    }

    /**
     * 修改用户密码
     * @param passwordData 密码数据
     * @return 修改结果
     */
    @PostMapping("/password")
    public R<String> changePassword(@RequestBody Map<String, String> passwordData) {
        log.info("修改用户密码：passwordData={}", passwordData);

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        String confirmPassword = passwordData.get("confirmPassword");

        if (oldPassword == null || oldPassword.isEmpty()) {
            return R.error("原密码不能为空");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            return R.error("新密码不能为空");
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return R.error("确认密码不能为空");
        }

        if (!newPassword.equals(confirmPassword)) {
            return R.error("两次输入的密码不一致");
        }

        Long userId = 1L;
        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return R.error("原密码错误");
        }

        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());

        boolean success = userService.updateById(user);
        if (success) {
            return R.success("密码修改成功");
        } else {
            return R.error("密码修改失败");
        }
    }

    /**
     * 更新用户信息
     * @param userData 用户数据
     * @return 更新结果
     */
    @PostMapping("/update")
    public R<String> updateUserInfo(@RequestBody Map<String, String> userData) {
        log.info("更新用户信息：userData={}", userData);

        Long userId = 1L;
        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        String username = userData.get("username");
        String email = userData.get("email");
        String avatar = userData.get("avatar");

        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }

        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        if (avatar != null && !avatar.isEmpty()) {
            user.setAvatar(avatar);
        }

        user.setUpdateTime(LocalDateTime.now());

        boolean success = userService.updateById(user);
        if (success) {
            return R.success("更新成功");
        } else {
            return R.error("更新失败");
        }
    }

    /**
     * 获取手机验证码
     * @param params 验证码参数
     * @return 验证码发送结果
     */
    @GetMapping("/code")
    public R<String> getPhoneCode(@RequestParam Map<String, String> params) {
        log.info("获取手机验证码：params={}", params);

        String phone = params.get("phone");

        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }

        return R.success("验证码发送成功");
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/loginout")
    public R<String> loginout() {
        log.info("退出登录");

        return R.success("退出成功");
    }

}
