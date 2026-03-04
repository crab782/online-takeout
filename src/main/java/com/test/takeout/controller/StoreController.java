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
        } else if ("rating".equals(sortBy)) {
            // 按评分降序
            queryWrapper.orderByDesc(Store::getRating);
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
     * 取消收藏店铺
     * @param storeFavorite 收藏信息（必须包含店铺ID）
     * @return 取消收藏结果
     */
    @PostMapping("/unfavorite")
    public R<String> unfavorite(@RequestBody StoreFavorite storeFavorite) {
        log.info("取消收藏店铺：storeFavorite={}", storeFavorite);

        if (storeFavorite.getStoreId() == null) {
            return R.error("店铺ID不能为空");
        }

        Long userId = 1L;

        LambdaQueryWrapper<StoreFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreFavorite::getUserId, userId);
        queryWrapper.eq(StoreFavorite::getStoreId, storeFavorite.getStoreId());
        StoreFavorite existingFavorite = storeFavoriteService.getOne(queryWrapper);

        if (existingFavorite == null) {
            return R.error("未收藏该店铺");
        }

        boolean success = storeFavoriteService.removeById(existingFavorite.getId());
        if (success) {
            return R.success("取消收藏成功");
        } else {
            return R.error("取消收藏失败");
        }
    }

    /**
     * 获取收藏的店铺列表
     * @param page 当前页码（默认1）
     * @param pageSize 每页数量（默认12）
     * @return 收藏店铺列表
     */
    @GetMapping("/favorite/list")
    public R<Page<Store>> favoriteList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize) {
        log.info("获取收藏的店铺列表：page={}, pageSize={}", page, pageSize);

        Long userId = 1L;

        LambdaQueryWrapper<StoreFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreFavorite::getUserId, userId);
        queryWrapper.orderByDesc(StoreFavorite::getCreateTime);

        Page<StoreFavorite> favoritePage = new Page<>(page, pageSize);
        storeFavoriteService.page(favoritePage, queryWrapper);

        Page<Store> storePage = new Page<>(page, pageSize);
        storePage.setTotal(favoritePage.getTotal());
        storePage.setCurrent(favoritePage.getCurrent());
        storePage.setSize(favoritePage.getSize());
        storePage.setPages(favoritePage.getPages());

        if (favoritePage.getRecords() != null && !favoritePage.getRecords().isEmpty()) {
            java.util.List<Long> storeIds = favoritePage.getRecords().stream()
                    .map(StoreFavorite::getStoreId)
                    .collect(java.util.stream.Collectors.toList());

            LambdaQueryWrapper<Store> storeQueryWrapper = new LambdaQueryWrapper<>();
            storeQueryWrapper.in(Store::getId, storeIds);
            storeQueryWrapper.eq(Store::getStatus, 1);
            storeQueryWrapper.orderByDesc(Store::getCreateTime);

            storeService.page(storePage, storeQueryWrapper);
        }

        return R.success(storePage);
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
     * @param store 店铺信息
     * @return 申请结果
     */
    @PostMapping("/apply")
    public R<String> apply(@RequestBody Store store) {
        log.info("申请开设店铺：store={}", store);

        // 参数校验
        if (store.getName() == null || store.getName().isEmpty()) {
            return R.error("店铺名称不能为空");
        }
        if (store.getAddress() == null || store.getAddress().isEmpty()) {
            return R.error("店铺地址不能为空");
        }
        if (store.getPhone() == null || store.getPhone().isEmpty()) {
            return R.error("联系电话不能为空");
        }

        // 校验手机号格式
        String phone = store.getPhone();
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return R.error("请输入正确的手机号码");
        }

        // 设置默认值
        store.setStatus(0); // 状态设为0-待审核
        store.setSales(0); // 销量初始化为0
        store.setRating(java.math.BigDecimal.valueOf(5.0)); // 评分初始化为5.0

        // 设置默认营业时间（如果未提供）
        if (store.getOpenTime() == null || store.getOpenTime().isEmpty()) {
            store.setOpenTime("09:00");
        }
        if (store.getCloseTime() == null || store.getCloseTime().isEmpty()) {
            store.setCloseTime("22:00");
        }

        // 设置默认配送费和起送金额（如果未提供）
        if (store.getDeliveryFee() == null) {
            store.setDeliveryFee(java.math.BigDecimal.ZERO);
        }
        if (store.getMinOrderAmount() == null) {
            store.setMinOrderAmount(java.math.BigDecimal.ZERO);
        }

        // 保存店铺信息
        boolean success = storeService.save(store);
        if (success) {
            log.info("店铺申请提交成功，店铺ID：{}，名称：{}", store.getId(), store.getName());
            return R.success("店铺申请提交成功，请等待审核");
        } else {
            return R.error("店铺申请提交失败，请稍后重试");
        }
    }

}
