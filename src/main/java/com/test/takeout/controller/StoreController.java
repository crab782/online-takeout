package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Employee;
import com.test.takeout.entity.Store;
import com.test.takeout.service.EmployeeService;
import com.test.takeout.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 店铺控制器，处理店铺相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final EmployeeService employeeService;

    public StoreController(StoreService storeService, EmployeeService employeeService) {
        this.storeService = storeService;
        this.employeeService = employeeService;
    }

    /**
     * 获取推荐店铺
     * @param limit 返回数量限制（默认10）
     * @return 推荐店铺列表
     */
    @GetMapping("/recommend")
    public R<Page<Store>> recommend(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        log.info("获取推荐店铺：limit={}", limit);

        Page<Store> pageInfo = new Page<>(1, limit);

        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getStatus, 1);
        queryWrapper.orderByDesc(Store::getCreateTime);

        storeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 获取热门店铺
     * @param limit 返回数量限制（默认10）
     * @return 热门店铺列表
     */
    @GetMapping("/hot")
    public R<Page<Store>> hot(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        log.info("获取热门店铺：limit={}", limit);

        Page<Store> pageInfo = new Page<>(1, limit);

        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getStatus, 1);
        queryWrapper.orderByDesc(Store::getCreateTime);

        storeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 搜索店铺
     * @param keyword 搜索关键词
     * @param page 当前页码（默认1）
     * @param pageSize 每页数量（默认12）
     * @return 搜索结果
     */
    @GetMapping("/search")
    public R<Page<Store>> search(@RequestParam("keyword") String keyword,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize) {
        log.info("搜索店铺：keyword={}, page={}, pageSize={}", keyword, page, pageSize);

        if (keyword == null || keyword.isEmpty()) {
            return R.error("搜索关键词不能为空");
        }

        Page<Store> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Store::getName, keyword)
                   .or()
                   .like(Store::getDescription, keyword);
        queryWrapper.eq(Store::getStatus, 1);
        queryWrapper.orderByDesc(Store::getCreateTime);

        storeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 获取店铺列表（分页）
     * @param page 当前页码（默认1）
     * @param pageSize 每页数量（默认12）
     * @param name 店铺名称（可选）
     * @param categoryId 分类ID（可选）
     * @param status 店铺状态（可选，1-营业中，0-已关闭）
     * @param sortBy 排序方式（可选，recommend-推荐，sales-销量，rating-评分）
     * @return 店铺列表
     */
    @GetMapping("/list")
    public R<Page<Store>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "categoryId", required = false) Long categoryId,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "sortBy", required = false) String sortBy) {
        log.info("获取店铺列表：page={}, pageSize={}, name={}, categoryId={}, status={}, sortBy={}", page, pageSize, name, categoryId, status, sortBy);

        Page<Store> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Store::getName, name);
        }

        if (categoryId != null) {
            queryWrapper.eq(Store::getCategoryId, categoryId);
        }

        if (status != null) {
            queryWrapper.eq(Store::getStatus, status);
        }

        // 根据排序方式设置排序规则
        if ("sales".equals(sortBy)) {
            // 按销量降序
            queryWrapper.orderByDesc(Store::getSales);
        } else {
            // 默认按创建时间降序（推荐）
            queryWrapper.orderByDesc(Store::getCreateTime);
        }

        storeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 获取店铺详情
     * @param id 店铺ID
     * @return 店铺详情
     */
    @GetMapping("/detail/{id}")
    public R<Store> detail(@PathVariable("id") Long id) {
        log.info("获取店铺详情：id={}", id);

        if (id == null) {
            return R.error("店铺ID不能为空");
        }

        Store store = storeService.getById(id);
        if (store == null) {
            return R.error("店铺不存在");
        }

        return R.success(store);
    }



    /**
     * 获取店铺信息
     * @return 店铺信息
     */
    @GetMapping("/info")
    public R<Store> getShopInfo() {
        log.info("获取店铺信息");

        // 查询店铺信息，这里假设只有一家店铺，查询ID为1的店铺
        Store store = storeService.getById(1L);
        if (store == null) {
            return R.error("店铺信息不存在");
        }

        return R.success(store);
    }

    /**
     * 申请开设店铺
     * @param params 参数（包含店铺信息和管理员信息）
     * @return 申请结果
     */
    @PostMapping("/apply")
    @Transactional(rollbackFor = Exception.class)
    public R<String> apply(@RequestBody Map<String, Object> params) {
        log.info("申请开设店铺：params={}", params);

        // 获取店铺信息
        String name = params.get("name") != null ? params.get("name").toString() : null;
        String address = params.get("address") != null ? params.get("address").toString() : null;
        String phone = params.get("phone") != null ? params.get("phone").toString() : null;
        String description = params.get("description") != null ? params.get("description").toString() : null;
        String image = params.get("image") != null ? params.get("image").toString() : null;
        String openTime = params.get("openTime") != null ? params.get("openTime").toString() : null;
        String closeTime = params.get("closeTime") != null ? params.get("closeTime").toString() : null;
        
        // 获取管理员信息
        String username = params.get("username") != null ? params.get("username").toString() : null;
        String password = params.get("password") != null ? params.get("password").toString() : null;

        // 参数校验
        if (name == null || name.isEmpty()) {
            return R.error("店铺名称不能为空");
        }
        if (address == null || address.isEmpty()) {
            return R.error("店铺地址不能为空");
        }
        if (phone == null || phone.isEmpty()) {
            return R.error("联系电话不能为空");
        }
        if (username == null || username.isEmpty()) {
            return R.error("管理员账号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return R.error("管理员密码不能为空");
        }

        // 校验手机号格式（支持手机号和 400 电话）
        if (!phone.matches("^(1[3-9]\\d{9}|400(-\\d{3,4}){2})$")) {
            return R.error("请输入正确的手机号或 400 电话格式");
        }

        // 检查管理员账号是否已存在
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee existingEmployee = employeeService.getOne(queryWrapper);
        if (existingEmployee != null) {
            return R.error("管理员账号已存在，请更换账号");
        }

        // 创建店铺对象
        Store store = new Store();
        store.setName(name);
        store.setAddress(address);
        store.setPhone(phone);
        store.setDescription(description);
        
        // 如果没有上传图片，使用默认图片
        if (image == null || image.isEmpty()) {
            store.setImage("https://example.com/images/kfc.jpg");
        } else {
            store.setImage(image);
        }
        
        store.setStatus(0); // 状态设为 0-待审核
        store.setSales(0); // 销量初始化为 0

        // 设置默认营业时间（如果未提供）
        if (openTime == null || openTime.isEmpty()) {
            store.setOpenTime("09:00");
        } else {
            store.setOpenTime(openTime);
        }
        
        if (closeTime == null || closeTime.isEmpty()) {
            store.setCloseTime("22:00");
        } else {
            store.setCloseTime(closeTime);
        }

        // 设置默认配送费和起送金额
        store.setDeliveryFee(java.math.BigDecimal.ZERO);
        store.setMinOrderAmount(java.math.BigDecimal.ZERO);

        store.setCreateTime(LocalDateTime.now());
        store.setUpdateTime(LocalDateTime.now());

        // 保存店铺信息
        boolean storeSaved = storeService.save(store);
        if (!storeSaved) {
            return R.error("店铺申请提交失败，请稍后重试");
        }

        // 创建管理员账户
        Employee employee = new Employee();
        employee.setUsername(username);
        
        // 使用 BCrypt 加密密码
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = 
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        employee.setPassword(encodedPassword);
        
        employee.setName(name + "管理员");
        employee.setPhone(phone);
        employee.setRole(2); // 角色设为 2（店铺管理员）
        employee.setStoreId(store.getId());
        employee.setStatus(0); // 状态设为 0-待审核（与店铺状态一致）
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        // 保存管理员账户
        boolean employeeSaved = employeeService.save(employee);
        if (!employeeSaved) {
            // 回滚店铺保存
            storeService.removeById(store.getId());
            return R.error("店铺申请提交失败，请稍后重试");
        }

        log.info("店铺申请提交成功，店铺 ID：{}，名称：{}，管理员账号：{}", 
                store.getId(), store.getName(), username);
        return R.success("店铺申请提交成功，请等待审核");
    }
}
