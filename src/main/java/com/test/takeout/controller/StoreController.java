package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Store;
import com.test.takeout.entity.StoreFavorite;
import com.test.takeout.service.StoreFavoriteService;
import com.test.takeout.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 店铺控制器，处理店铺相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final StoreFavoriteService storeFavoriteService;

    public StoreController(StoreService storeService, StoreFavoriteService storeFavoriteService) {
        this.storeService = storeService;
        this.storeFavoriteService = storeFavoriteService;
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
     * @return 店铺列表
     */
    @GetMapping("/list")
    public R<Page<Store>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "categoryId", required = false) Long categoryId,
                                  @RequestParam(value = "status", required = false) Integer status) {
        log.info("获取店铺列表：page={}, pageSize={}, name={}, categoryId={}, status={}", page, pageSize, name, categoryId, status);

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

        queryWrapper.orderByDesc(Store::getCreateTime);

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
     * 收藏店铺
     * @param storeFavorite 收藏信息（必须包含店铺ID）
     * @return 收藏结果
     */
    @PostMapping("/favorite")
    public R<String> favorite(@RequestBody StoreFavorite storeFavorite) {
        log.info("收藏店铺：storeFavorite={}", storeFavorite);

        if (storeFavorite.getStoreId() == null) {
            return R.error("店铺ID不能为空");
        }

        Store store = storeService.getById(storeFavorite.getStoreId());
        if (store == null) {
            return R.error("店铺不存在");
        }

        Long userId = 1L;

        LambdaQueryWrapper<StoreFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreFavorite::getUserId, userId);
        queryWrapper.eq(StoreFavorite::getStoreId, storeFavorite.getStoreId());
        StoreFavorite existingFavorite = storeFavoriteService.getOne(queryWrapper);

        if (existingFavorite != null) {
            return R.error("已经收藏过该店铺");
        }

        storeFavorite.setUserId(userId);
        storeFavorite.setCreateTime(LocalDateTime.now());

        boolean success = storeFavoriteService.save(storeFavorite);
        if (success) {
            return R.success("收藏成功");
        } else {
            return R.error("收藏失败");
        }
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

}
