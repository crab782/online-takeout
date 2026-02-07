package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.JwtUtil;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee) {
        // 加密密码
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 查询用户名数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 没有用户名则返回失败结果
        if (emp == null) {
            return R.error("用户名不存在");
        }

        // 密码错误则返回失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        // 员工状态返回员工禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        // 登录成功，生成JWT token
        String token = JwtUtil.generateToken(emp.getId());
        
        // 构建响应数据
        R<Employee> response = R.success(emp);
        response.add("token", token);
        
        return response;
    }

    /**
     * 员工登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    public R<String> logout() {
        // 登出成功
        return R.success("退出成功");
    }

}
