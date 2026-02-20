package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.entity.Store;
import com.test.takeout.service.EmployeeService;
import com.test.takeout.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
