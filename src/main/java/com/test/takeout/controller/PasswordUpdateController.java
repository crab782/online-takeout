package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 密码更新控制器
 */
@Slf4j
@RestController
@RequestMapping("/password")
public class PasswordUpdateController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/update")
    public R<String> updatePassword() {
        try {
            log.info("更新密码");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String newPassword = encoder.encode("123");
            log.info("新密码：{}", newPassword);
            
            LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Employee::getUsername, "shop_admin1");
            updateWrapper.set(Employee::getPassword, newPassword);
            
            boolean success = employeeService.update(updateWrapper);
            log.info("更新结果：{}", success);
            
            if (success) {
                return R.success("密码更新成功");
            } else {
                return R.error("密码更新失败");
            }
        } catch (Exception e) {
            log.error("密码更新异常：", e);
            return R.error("密码更新异常：" + e.getMessage());
        }
    }
}
