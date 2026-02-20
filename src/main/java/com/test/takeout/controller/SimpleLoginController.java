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
 * 简化登录控制器
 */
@Slf4j
@RestController
@RequestMapping("/simple-login")
public class SimpleLoginController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<String> login(@RequestBody java.util.Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            // 直接返回成功，不进行任何数据库查询
            return R.success("登录成功");
        } catch (Exception e) {
            log.error("登录异常", e);
            return R.error("登录失败");
        }
    }
}
