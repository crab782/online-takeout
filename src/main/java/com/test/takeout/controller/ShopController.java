package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Store;
import com.test.takeout.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopController {

    private final StoreService storeService;

    public ShopController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/page")
    public R<Page<Store>> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "status", required = false) Integer status) {
        log.info("分页查询店铺列表：page={}, pageSize={}, name={}, status={}", page, pageSize, name, status);

        Page<Store> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Store::getName, name);
        }

        if (status != null) {
            queryWrapper.eq(Store::getStatus, status);
        }

        queryWrapper.orderByDesc(Store::getCreateTime);

        storeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/{id}")
    public R<Map<String, Object>> getById(@PathVariable("id") Long id) {
        log.info("根据ID获取店铺详情：id={}", id);

        if (id == null) {
            return R.error("店铺ID不能为空");
        }

        Store store = storeService.getById(id);
        if (store == null) {
            return R.error("店铺不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", store.getId());
        result.put("name", store.getName());
        result.put("description", store.getDescription());
        result.put("image", store.getImage());
        result.put("address", store.getAddress());
        result.put("phone", store.getPhone());
        result.put("status", store.getStatus());
        result.put("categoryId", store.getCategoryId());
        result.put("openTime", store.getOpenTime());
        result.put("closeTime", store.getCloseTime());
        result.put("deliveryFee", store.getDeliveryFee());
        result.put("minOrderAmount", store.getMinOrderAmount());
        result.put("rating", 4.5);
        result.put("sales", 1000);
        result.put("createTime", store.getCreateTime());
        result.put("updateTime", store.getUpdateTime());

        return R.success(result);
    }

    @PostMapping
    public R<String> add(@RequestBody Store store) {
        log.info("新增店铺：store={}", store);

        if (store.getName() == null || store.getName().isEmpty()) {
            return R.error("店铺名称不能为空");
        }

        if (store.getPhone() == null || store.getPhone().isEmpty()) {
            return R.error("店铺电话不能为空");
        }

        if (store.getAddress() == null || store.getAddress().isEmpty()) {
            return R.error("店铺地址不能为空");
        }

        store.setCreateTime(LocalDateTime.now());
        store.setUpdateTime(LocalDateTime.now());

        boolean success = storeService.save(store);
        if (success) {
            return R.success("新增店铺成功");
        } else {
            return R.error("新增店铺失败");
        }
    }

    @PutMapping
    public R<String> update(@RequestBody Store store) {
        log.info("更新店铺信息：store={}", store);

        if (store.getId() == null) {
            return R.error("店铺ID不能为空");
        }

        Store existingStore = storeService.getById(store.getId());
        if (existingStore == null) {
            return R.error("店铺不存在");
        }

        store.setUpdateTime(LocalDateTime.now());

        boolean success = storeService.updateById(store);
        if (success) {
            return R.success("更新店铺信息成功");
        } else {
            return R.error("更新店铺信息失败");
        }
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("id") Long id) {
        log.info("删除店铺：id={}", id);

        if (id == null) {
            return R.error("店铺ID不能为空");
        }

        Store store = storeService.getById(id);
        if (store == null) {
            return R.error("店铺不存在");
        }

        boolean success = storeService.removeById(id);
        if (success) {
            return R.success("删除店铺成功");
        } else {
            return R.error("删除店铺失败");
        }
    }

    @PostMapping("/status")
    public R<String> updateStatus(@RequestBody Map<String, Object> params) {
        log.info("更新店铺状态：params={}", params);

        Long id = params.get("id") != null ? Long.valueOf(params.get("id").toString()) : null;
        Integer status = params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null;

        if (id == null) {
            return R.error("店铺ID不能为空");
        }

        if (status == null) {
            return R.error("店铺状态不能为空");
        }

        Store store = storeService.getById(id);
        if (store == null) {
            return R.error("店铺不存在");
        }

        store.setStatus(status);
        store.setUpdateTime(LocalDateTime.now());

        boolean success = storeService.updateById(store);
        if (success) {
            return R.success("更新店铺状态成功");
        } else {
            return R.error("更新店铺状态失败");
        }
    }

    @GetMapping("/list")
    public R<Page<Store>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "categoryId", required = false) Long categoryId) {
        log.info("获取店铺列表：page={}, pageSize={}, name={}, status={}, categoryId={}", page, pageSize, name, status, categoryId);

        Page<Store> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Store::getName, name);
        }

        if (status != null) {
            queryWrapper.eq(Store::getStatus, status);
        }

        if (categoryId != null) {
            queryWrapper.eq(Store::getCategoryId, categoryId);
        }

        queryWrapper.orderByDesc(Store::getCreateTime);

        storeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @PostMapping("/audit")
    public R<String> audit(@RequestBody Map<String, Object> params) {
        log.info("审核店铺：params={}", params);

        Long id = params.get("id") != null ? Long.valueOf(params.get("id").toString()) : null;
        Integer approveResult = params.get("approveResult") != null ? Integer.valueOf(params.get("approveResult").toString()) : null;
        String auditRemark = params.get("auditRemark") != null ? params.get("auditRemark").toString() : null;

        if (id == null) {
            return R.error("店铺ID不能为空");
        }

        if (approveResult == null) {
            return R.error("审核结果不能为空");
        }

        if (approveResult != 0 && approveResult != 1) {
            return R.error("审核结果不合法");
        }

        Store store = storeService.getById(id);
        if (store == null) {
            return R.error("店铺不存在");
        }

        store.setStatus(approveResult);
        store.setUpdateTime(LocalDateTime.now());

        boolean success = storeService.updateById(store);
        if (success) {
            return R.success("审核店铺成功");
        } else {
            return R.error("审核店铺失败");
        }
    }
}
