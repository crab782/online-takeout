package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Dish;
import com.test.takeout.entity.Category;
import com.test.takeout.service.DishService;
import com.test.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜品控制器
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询菜品列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param name 菜品名称（可选）
     * @param shopId 店铺ID（可选）
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public R<Page<Dish>> getDishPage(int page, int pageSize, String name, Long shopId) {
        log.info("分页查询菜品列表：page={}, pageSize={}, name={}, shopId={}", page, pageSize, name, shopId);

        // 构建分页对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Dish::getName, name);
        }
        
        // 处理店铺ID参数
        if (shopId != null) {
            // 直接查询该店铺下的所有菜品，包括未分类的菜品
            queryWrapper.eq(Dish::getStoreId, shopId);
        }
        
        // 按照价格从小到大排序
        queryWrapper.orderByAsc(Dish::getPrice);

        // 执行分页查询
        dishService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据ID查询菜品详情
     * @param id 菜品ID
     * @return 菜品详情
     */
    @GetMapping("/{id}")
    public R<Dish> getDishById(@PathVariable Long id) {
        log.info("根据ID查询菜品详情：id={}", id);

        Dish dish = dishService.getById(id);
        if (dish != null) {
            return R.success(dish);
        } else {
            return R.error("菜品不存在");
        }
    }

    /**
     * 新增菜品
     * @param dish 菜品信息
     * @return 新增结果
     */
    @PostMapping
    public R<String> addDish(@RequestBody Dish dish) {
        log.info("新增菜品：{}", dish);

        boolean success = dishService.save(dish);
        if (success) {
            return R.success("新增菜品成功");
        } else {
            return R.error("新增菜品失败");
        }
    }

    /**
     * 修改菜品信息
     * @param dish 菜品信息
     * @return 修改结果
     */
    @PutMapping
    public R<String> updateDish(@RequestBody Dish dish) {
        log.info("修改菜品信息：{}", dish);

        boolean success = dishService.updateById(dish);
        if (success) {
            return R.success("修改菜品成功");
        } else {
            return R.error("修改菜品失败");
        }
    }

    /**
     * 删除菜品（支持批量删除）
     * @param ids 菜品ID数组
     * @return 删除结果
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam List<Long> ids) {
        log.info("删除菜品：ids={}", ids);

        boolean success = dishService.removeByIds(ids);
        if (success) {
            return R.success("删除菜品成功");
        } else {
            return R.error("删除菜品失败");
        }
    }

    /**
     * 菜品批量起售/停售
     * @param status 状态（0-停售，1-起售）
     * @param ids 菜品ID数组
     * @return 操作结果
     */
    @PostMapping("/status/{status}")
    public R<String> updateDishStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("修改菜品状态：status={}, ids={}", status, ids);

        // 构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);

        // 批量更新状态
        Dish dish = new Dish();
        dish.setStatus(status);

        boolean success = dishService.update(dish, queryWrapper);
        if (success) {
            return R.success("修改菜品状态成功");
        } else {
            return R.error("修改菜品状态失败");
        }
    }

    /**
     * 查询菜品列表（不分页）
     * @param params 查询参数
     * @return 菜品列表
     */
    @GetMapping("/list")
    public R<List<Dish>> getDishList(@RequestParam Map<String, Object> params) {
        log.info("查询菜品列表：params={}", params);

        // 构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        
        // 处理店铺ID参数
        if (params.containsKey("storeId")) {
            try {
                Long storeId = Long.parseLong(params.get("storeId").toString());
                // 直接查询该店铺下的所有菜品，包括未分类的菜品
                queryWrapper.eq(Dish::getStoreId, storeId);
            } catch (NumberFormatException e) {
                log.error("店铺ID参数格式错误：{}", params.get("storeId"));
            }
        }
        
        // 处理分类ID参数
        if (params.containsKey("categoryId")) {
            try {
                Long categoryId = Long.parseLong(params.get("categoryId").toString());
                queryWrapper.eq(Dish::getCategoryId, categoryId);
            } catch (NumberFormatException e) {
                log.error("分类ID参数格式错误：{}", params.get("categoryId"));
            }
        }
        
        // 处理类型参数
        if (params.containsKey("type")) {
            try {
                Integer type = Integer.parseInt(params.get("type").toString());
                queryWrapper.eq(Dish::getStatus, type);
            } catch (NumberFormatException e) {
                log.error("类型参数格式错误：{}", params.get("type"));
            }
        }
        // 移除默认状态查询，允许查询所有状态的菜品
        
        // 处理排序参数
        if (params.containsKey("sortBy")) {
            String sortBy = params.get("sortBy").toString();
            if (sortBy.equals("price_asc")) {
                queryWrapper.orderByAsc(Dish::getPrice);
            } else if (sortBy.equals("price_desc")) {
                queryWrapper.orderByDesc(Dish::getPrice);
            } else if (sortBy.equals("update_time")) {
                queryWrapper.orderByDesc(Dish::getUpdateTime);
            }
        } else {
            // 默认按照价格排序
            queryWrapper.orderByAsc(Dish::getPrice);
        }

        List<Dish> dishList = dishService.list(queryWrapper);

        return R.success(dishList);
    }

    /**
     * 修改菜品库存
     * @param params 包含菜品ID和库存数量的参数
     * @return 修改结果
     */
    @PutMapping("/stock")
    public R<String> updateDishStock(@RequestBody Map<String, Object> params) {
        log.info("修改菜品库存：params={}", params);

        try {
            Long dishId = Long.parseLong(params.get("id").toString());
            Integer stock = Integer.parseInt(params.get("stock").toString());

            if (stock < 0) {
                return R.error("库存数量不能为负数");
            }

            Dish dish = dishService.getById(dishId);
            if (dish == null) {
                return R.error("菜品不存在");
            }

            // 更新库存
            dish.setStock(stock);
            // 自动计算库存状态
            if (stock <= 0) {
                dish.setStockStatus(0); // 售罄
            } else if (stock <= 10) {
                dish.setStockStatus(2); // 紧张
            } else {
                dish.setStockStatus(1); // 充足
            }

            boolean success = dishService.updateById(dish);
            if (success) {
                log.info("菜品库存修改成功：dishId={}, stock={}", dishId, stock);
                return R.success("库存修改成功");
            } else {
                return R.error("库存修改失败");
            }
        } catch (Exception e) {
            log.error("修改菜品库存失败：", e);
            return R.error("修改库存失败：" + e.getMessage());
        }
    }

}