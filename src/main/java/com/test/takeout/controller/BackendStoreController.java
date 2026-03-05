package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.entity.Store;
import com.test.takeout.service.EmployeeService;
import com.test.takeout.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 后端店铺控制器，处理后端管理相关的店铺请求
 */
@Slf4j
@RestController
@RequestMapping("/backend/store")
public class BackendStoreController {

    private final StoreService storeService;
    private final EmployeeService employeeService;

    public BackendStoreController(StoreService storeService, EmployeeService employeeService) {
        this.storeService = storeService;
        this.employeeService = employeeService;
    }

    /**
     * 获取店铺信息（后端管理用）
     * @param request HttpServletRequest
     * @return 店铺信息
     */
    @GetMapping("/info")
    public R<Store> getShopInfo(HttpServletRequest request) {
        log.info("获取店铺信息（后端管理用）");

        // 从请求中获取用户ID
        Long userId = (Long) request.getAttribute("userId");
        log.info("用户ID: {}", userId);

        // 查询员工信息，获取store_id
        Employee employee = employeeService.getById(userId);
        if (employee == null) {
            return R.error("员工信息不存在");
        }

        Long storeId = employee.getStoreId();
        log.info("店铺ID: {}", storeId);

        // 根据store_id查询店铺信息
        Store store = storeService.getById(storeId);
        if (store == null) {
            return R.error("店铺信息不存在");
        }

        return R.success(store);
    }

    /**
     * 更新店铺信息（后端管理用）
     * @param request HttpServletRequest
     * @param store 店铺信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public R<String> updateShopInfo(HttpServletRequest request, @RequestBody Store store) {
        log.info("更新店铺信息（后端管理用），店铺信息: {}", store);

        // 从请求中获取用户ID
        Long userId = (Long) request.getAttribute("userId");
        log.info("用户ID: {}", userId);

        // 查询员工信息，获取store_id
        Employee employee = employeeService.getById(userId);
        if (employee == null) {
            return R.error("员工信息不存在");
        }

        Long storeId = employee.getStoreId();
        log.info("店铺ID: {}", storeId);

        // 设置店铺ID
        store.setId(storeId);
        // 设置更新时间
        store.setUpdateTime(LocalDateTime.now());

        // 更新店铺信息
        boolean success = storeService.updateById(store);
        if (!success) {
            return R.error("店铺信息更新失败");
        }

        return R.success("店铺信息更新成功");
    }

    /**
     * 修改当前员工密码（后端管理用）
     * @param request HttpServletRequest
     * @param params 密码参数
     * @return 修改结果
     */
    @PutMapping("/password")
    public R<String> updatePassword(HttpServletRequest request, @RequestBody Map<String, String> params) {
        log.info("修改员工密码（后端管理用）");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }
        log.info("用户ID: {}", userId);

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        if (oldPassword == null || oldPassword.isEmpty()) {
            return R.error("原密码不能为空");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            return R.error("新密码不能为空");
        }

        // 查询员工信息
        Employee employee = employeeService.getById(userId);
        if (employee == null) {
            return R.error("员工信息不存在");
        }

        // 验证原密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, employee.getPassword())) {
            return R.error("原密码错误");
        }

        // 检查新密码是否与原密码相同
        if (encoder.matches(newPassword, employee.getPassword())) {
            return R.error("新密码不能与原密码相同");
        }

        // 加密新密码
        String encodedNewPassword = encoder.encode(newPassword);

        // 更新密码
        employee.setPassword(encodedNewPassword);
        employee.setUpdatedAt(LocalDateTime.now());
        boolean success = employeeService.updateById(employee);

        if (!success) {
            return R.error("密码修改失败");
        }

        log.info("密码修改成功，用户ID: {}", userId);
        return R.success("密码修改成功");
    }
}
