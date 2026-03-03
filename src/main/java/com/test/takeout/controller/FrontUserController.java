package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.User;
import com.test.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
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

        log.info("手机号：{}", phone);
        log.info("密码：{}", password);

        if (phone == null || phone.isEmpty()) {
            log.info("手机号为空");
            return R.error("手机号不能为空");
        }

        if (password == null || password.isEmpty()) {
            log.info("密码为空");
            return R.error("密码不能为空");
        }

        // 先根据手机号查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);

        log.info("查询到的用户：{}", user);

        if (user == null) {
            log.info("用户不存在");
            return R.error("手机号或密码错误");
        }

        if (user.getStatus() == 0) {
            log.info("账号已被禁用");
            return R.error("账号已被禁用");
        }

        log.info("数据库中的密码：{}", user.getPassword());
        log.info("输入的密码：{}", password);

        // 验证密码
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        
        // 测试：生成新的加密密码并验证
        String testEncodedPassword = passwordEncoder.encode(password);
        log.info("新生成的加密密码：{}", testEncodedPassword);
        boolean testMatch = passwordEncoder.matches(password, testEncodedPassword);
        log.info("新密码匹配结果：{}", testMatch);
        
        // 验证原始密码
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        log.info("原始密码匹配结果：{}", passwordMatch);

        if (!passwordMatch) {
            log.info("密码不匹配");
            return R.error("手机号或密码错误");
        }

        Map<String, Object> result = new HashMap<>();
        String token = com.test.takeout.common.JwtUtil.generateToken(user.getId());
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("phone", phone);
        result.put("username", user.getUsername());
        result.put("avatar", user.getAvatar());
        
        log.info("登录成功，返回结果：{}", result);
        return R.success(result);
    }

    /**
     * 获取当前登录用户信息
     * @param request HttpServletRequest
     * @return 用户信息
     */
    @GetMapping("/info")
    public R<User> getUserInfo(HttpServletRequest request) {
        log.info("获取当前登录用户信息");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }
        
        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        user.setPassword(null);
        
        return R.success(user);
    }

    /**
     * 修改用户密码
     * @param request HttpServletRequest
     * @param passwordData 密码数据
     * @return 修改结果
     */
    @PostMapping("/password")
    public R<String> changePassword(HttpServletRequest request, @RequestBody Map<String, String> passwordData) {
        log.info("修改用户密码");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        String confirmPassword = passwordData.get("confirmPassword");

        if (oldPassword == null || oldPassword.isEmpty()) {
            return R.error("原密码不能为空");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            return R.error("新密码不能为空");
        }

        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return R.error("新密码长度需为6-20个字符");
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return R.error("确认密码不能为空");
        }

        if (!newPassword.equals(confirmPassword)) {
            return R.error("两次输入的密码不一致");
        }

        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = 
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return R.error("原密码错误");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return R.error("新密码不能与原密码相同");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
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
     * @param request HttpServletRequest
     * @param userData 用户数据
     * @return 更新结果
     */
    @PostMapping("/update")
    public R<User> updateUserInfo(HttpServletRequest request, @RequestBody Map<String, Object> userData) {
        log.info("更新用户信息：userData={}", userData);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        String username = (String) userData.get("username");
        String name = (String) userData.get("name");
        String phone = (String) userData.get("phone");
        String email = (String) userData.get("email");
        String avatar = (String) userData.get("avatar");
        Object sexObj = userData.get("sex");

        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }

        if (name != null) {
            user.setName(name);
        }

        if (phone != null && !phone.isEmpty()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            queryWrapper.ne(User::getId, userId);
            User existingUser = userService.getOne(queryWrapper);
            if (existingUser != null) {
                return R.error("该手机号已被其他用户使用");
            }
            user.setPhone(phone);
        }

        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        if (avatar != null && !avatar.isEmpty()) {
            user.setAvatar(avatar);
        }

        if (sexObj != null) {
            Integer sex = null;
            if (sexObj instanceof Integer) {
                sex = (Integer) sexObj;
            } else if (sexObj instanceof String) {
                String sexStr = (String) sexObj;
                if ("男".equals(sexStr)) {
                    sex = 1;
                } else if ("女".equals(sexStr)) {
                    sex = 0;
                } else {
                    sex = 2;
                }
            }
            user.setSex(sex);
        }

        user.setUpdateTime(LocalDateTime.now());

        boolean success = userService.updateById(user);
        if (success) {
            user.setPassword(null);
            return R.success(user);
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
     * 临时接口：更新用户密码（用于修复数据库密码问题）
     * @param phone 手机号
     * @return 更新结果
     */
    @PostMapping("/updatePassword")
    public R<String> updatePassword(@RequestParam String phone) {
        log.info("更新用户密码：phone={}", phone);
        
        // 根据手机号查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);
        
        if (user == null) {
            return R.error("用户不存在");
        }
        
        // 使用BCryptPasswordEncoder生成新的加密密码
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String newPassword = "123456";
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        log.info("原始密码：{}", newPassword);
        log.info("加密后的密码：{}", encodedPassword);
        
        // 更新用户密码
        user.setPassword(encodedPassword);
        user.setUpdateTime(LocalDateTime.now());
        boolean updated = userService.updateById(user);
        
        if (updated) {
            log.info("密码更新成功");
            return R.success("密码更新成功");
        } else {
            log.info("密码更新失败");
            return R.error("密码更新失败");
        }
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/loginout")
    public R<String> loginout() {
        return R.success("退出成功");
    }
}
