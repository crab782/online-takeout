package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/db")
    public R<String> testDb() {
        try {
            log.info("测试数据库连接");
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getUsername, "shop_admin1");
            Employee emp = employeeService.getOne(queryWrapper);
            
            if (emp == null) {
                log.info("用户名不存在");
                return R.error("用户名不存在");
            }
            
            log.info("找到用户：id={}, username={}, password={}, status={}", emp.getId(), emp.getUsername(), emp.getPassword(), emp.getStatus());
            return R.success("数据库连接成功，用户信息：" + emp.getUsername());
        } catch (Exception e) {
            log.error("数据库连接异常：", e);
            return R.error("数据库连接异常：" + e.getMessage());
        }
    }

    @GetMapping("/password")
    public R<String> testPassword() {
        try {
            log.info("测试密码验证");
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getUsername, "shop_admin1");
            Employee emp = employeeService.getOne(queryWrapper);
            
            if (emp == null) {
                return R.error("用户名不存在");
            }
            
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String inputPassword = "123";
            String storedPassword = emp.getPassword();
            boolean passwordMatch = encoder.matches(inputPassword, storedPassword);
            
            log.info("输入密码：{}", inputPassword);
            log.info("存储密码：{}", storedPassword);
            log.info("存储密码长度：{}", storedPassword.length());
            log.info("存储密码前缀：{}", storedPassword.substring(0, Math.min(10, storedPassword.length())));
            log.info("密码验证结果：{}", passwordMatch);
            
            // 测试BCrypt编码
            String encodedPassword = encoder.encode(inputPassword);
            log.info("BCrypt编码后的密码：{}", encodedPassword);
            
            // 比较数据库中的密码和初始化脚本中的密码
            String scriptPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
            log.info("初始化脚本中的密码：{}", scriptPassword);
            log.info("密码是否一致：{}", storedPassword.equals(scriptPassword));
            
            if (passwordMatch) {
                return R.success("密码验证成功");
            } else {
                return R.error("密码验证失败");
            }
        } catch (Exception e) {
            log.error("密码验证异常：", e);
            return R.error("密码验证异常：" + e.getMessage());
        }
    }

    @GetMapping("/jwt")
    public R<String> testJwt() {
        try {
            log.info("测试JWT生成");
            Long userId = 2L;
            String token = com.test.takeout.common.JwtUtil.generateToken(userId);
            log.info("生成的token：{}", token);
            return R.success("JWT生成成功，token：" + token);
        } catch (Exception e) {
            log.error("JWT生成异常：", e);
            return R.error("JWT生成异常：" + e.getMessage());
        }
    }

    @GetMapping("/bcrypt")
    public R<String> testBcrypt() {
        try {
            log.info("测试BCrypt密码编码和验证");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            
            // 测试编码
            String password = "123";
            String encodedPassword = encoder.encode(password);
            log.info("原始密码：{}", password);
            log.info("编码后的密码：{}", encodedPassword);
            
            // 测试验证
            boolean match1 = encoder.matches(password, encodedPassword);
            log.info("验证结果1（新编码）：{}", match1);
            
            // 测试验证数据库中的密码
            String dbPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
            boolean match2 = encoder.matches(password, dbPassword);
            log.info("验证结果2（数据库密码）：{}", match2);
            
            if (match1 && match2) {
                return R.success("BCrypt测试成功");
            } else if (!match1) {
                return R.error("BCrypt测试失败：新编码验证失败");
            } else if (!match2) {
                return R.error("BCrypt测试失败：数据库密码验证失败");
            } else {
                return R.error("BCrypt测试失败");
            }
        } catch (Exception e) {
            log.error("BCrypt测试异常：", e);
            return R.error("BCrypt测试异常：" + e.getMessage());
        }
    }
}
