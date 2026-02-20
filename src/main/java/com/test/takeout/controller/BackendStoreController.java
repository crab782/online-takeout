package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.Store;
import com.test.takeout.service.StoreService;
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

    public BackendStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * 获取店铺信息（后端管理用）
     * @return 店铺信息
     */
    @GetMapping("/info")
    public R<Store> getShopInfo() {
        log.info("获取店铺信息（后端管理用）");

        // 查询店铺信息，这里假设只有一家店铺，查询ID为1的店铺
        Store store = storeService.getById(1L);
        if (store == null) {
            return R.error("店铺信息不存在");
        }

        return R.success(store);
    }
}
