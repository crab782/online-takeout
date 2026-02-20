package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.JwtUtil;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @GetMapping("/login")
    public R<Employee> login(@RequestParam String username, @RequestParam String password) {
        try {
            log.info("登录请求：username={}, password={}", username, password);
            
            // 查询用户名数据库
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getUsername, username);
            Employee emp = employeeService.getOne(queryWrapper);

            // 没有用户名则返回失败结果
            if (emp == null) {
                log.info("用户名不存在：{}", username);
                return R.error("用户名不存在");
            }

            log.info("找到用户：id={}, username={}, password={}, status={}", emp.getId(), emp.getUsername(), emp.getPassword(), emp.getStatus());

            // 密码错误则返回失败结果
            // 使用BCryptPasswordEncoder验证密码
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean passwordMatch = encoder.matches(password, emp.getPassword());
            log.info("密码验证结果：{}", passwordMatch);
            if (!passwordMatch) {
                log.info("密码错误：输入密码={}", password);
                return R.error("密码错误");
            }

            // 员工状态返回员工禁用结果
            if (emp.getStatus() == 0) {
                log.info("账号已禁用：{}", username);
                return R.error("账号已禁用");
            }

            // 登录成功，生成JWT token
            log.info("准备生成token，用户ID：{}", emp.getId());
            String token = JwtUtil.generateToken(emp.getId());
            log.info("登录成功：username={}, token={}", username, token);
            
            // 清空密码，不返回给前端
            emp.setPassword(null);
            
            // 构建响应数据
            log.info("准备构建响应");
            R<Employee> response = R.success(emp);
            log.info("准备添加token到响应中");
            response.add("token", token);
            log.info("登录响应构建完成");
            
            return response;
        } catch (Exception e) {
            log.error("登录异常：", e);
            return R.error("系统内部错误：" + e.getMessage());
        }
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
