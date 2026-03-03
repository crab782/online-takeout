package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Setmeal;
import com.test.takeout.entity.SetmealDish;
import com.test.takeout.service.SetmealDishService;
import com.test.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页查询套餐列表
     * @param page 页码
     * @param pageSize 每页大小
     * @param name 套餐名称（可选）
     * @param shopId 店铺ID（可选）
     * @return 套餐列表
     */
    @GetMapping("/page")
    public R<Map<String, Object>> page(int page, int pageSize, String name, Long shopId) {
        log.info("分页查询套餐列表：page={}, pageSize={}, name={}, shopId={}", page, pageSize, name, shopId);

        // 构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加名称模糊查询条件
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Setmeal::getName, name);
        }
        
        // 添加店铺ID查询条件
        if (shopId != null) {
            queryWrapper.eq(Setmeal::getStoreId, shopId);
        }
        
        // 添加排序条件，按照创建时间倒序
        queryWrapper.orderByDesc(Setmeal::getCreateTime);

        // 执行分页查询
        setmealService.page(pageInfo, queryWrapper);

        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());

        return R.success(data);
    }

    /**
     * 删除套餐（支持批量删除）
     * @param ids 套餐ID数组
     * @return 删除结果
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") String ids) {
        log.info("删除套餐：ids={}", ids);

        // 将字符串转换为ID列表
        String[] idArray = ids.split(",");
        List<Long> idList = Arrays.stream(idArray)
                .map(Long::parseLong)
                .toList();

        // 批量删除套餐
        boolean result = setmealService.removeByIds(idList);

        if (result) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 修改套餐信息
     * @param setmeal 套餐信息
     * @return 修改结果
     */
    @PutMapping
    public R<String> update(@RequestBody Setmeal setmeal) {
        log.info("修改套餐信息：setmeal={}", setmeal);

        // 更新套餐
        boolean result = setmealService.updateById(setmeal);

        if (result) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    /**
     * 新增套餐
     * @param setmeal 套餐信息
     * @return 新增结果
     */
    @PostMapping
    public R<String> save(@RequestBody Map<String, Object> setmealData) {
        log.info("新增套餐：setmealData={}", setmealData);

        try {
            // 提取套餐基本信息
            Setmeal setmeal = new Setmeal();
            setmeal.setName((String) setmealData.get("name"));
            // 处理categoryId，支持数字和字符串类型
            Object categoryIdObj = setmealData.get("categoryId");
            if (categoryIdObj != null) {
                if (categoryIdObj instanceof Integer) {
                    setmeal.setCategoryId(categoryIdObj.toString());
                } else {
                    setmeal.setCategoryId((String) categoryIdObj);
                }
            }
            // 处理price，支持数字和字符串类型
            Object priceObj = setmealData.get("price");
            if (priceObj != null) {
                if (priceObj instanceof Number) {
                    setmeal.setPrice(new BigDecimal(priceObj.toString()));
                } else {
                    setmeal.setPrice(new BigDecimal(priceObj.toString()));
                }
            }
            setmeal.setDescription((String) setmealData.get("description"));
            setmeal.setImage((String) setmealData.get("image"));
            // 处理status，支持数字和字符串类型
            Object statusObj = setmealData.get("status");
            if (statusObj != null) {
                if (statusObj instanceof Integer) {
                    setmeal.setStatus((Integer) statusObj);
                } else {
                    setmeal.setStatus(Integer.parseInt(statusObj.toString()));
                }
            }
            // 处理storeId，支持数字和字符串类型
            Object storeIdObj = setmealData.get("storeId");
            if (storeIdObj != null) {
                if (storeIdObj instanceof Long) {
                    setmeal.setStoreId((Long) storeIdObj);
                } else if (storeIdObj instanceof Integer) {
                    setmeal.setStoreId(((Integer) storeIdObj).longValue());
                } else {
                    setmeal.setStoreId(Long.parseLong(storeIdObj.toString()));
                }
            }

            // 保存套餐
            boolean result = setmealService.save(setmeal);

            if (result) {
                // 处理套餐包含的菜品
                List<Map<String, Object>> setmealDishes = (List<Map<String, Object>>) setmealData.get("setmealDishes");
                if (setmealDishes != null && !setmealDishes.isEmpty()) {
                    for (Map<String, Object> dish : setmealDishes) {
                        SetmealDish setmealDish = new SetmealDish();
                        setmealDish.setSetmealId(setmeal.getId());
                        // 处理dishId，支持数字和字符串类型
                        Object dishIdObj = dish.get("dishId");
                        if (dishIdObj != null) {
                            if (dishIdObj instanceof Long) {
                                setmealDish.setDishId((Long) dishIdObj);
                            } else if (dishIdObj instanceof Integer) {
                                setmealDish.setDishId(((Integer) dishIdObj).longValue());
                            } else {
                                setmealDish.setDishId(Long.parseLong(dishIdObj.toString()));
                            }
                        }
                        setmealDish.setName((String) dish.get("name"));
                        // 处理price，支持数字和字符串类型
                        Object dishPriceObj = dish.get("price");
                        if (dishPriceObj != null) {
                            if (dishPriceObj instanceof Number) {
                                setmealDish.setPrice(new BigDecimal(dishPriceObj.toString()));
                            } else {
                                setmealDish.setPrice(new BigDecimal(dishPriceObj.toString()));
                            }
                        }
                        // 处理copies，支持数字和字符串类型
                        Object copiesObj = dish.get("copies");
                        if (copiesObj != null) {
                            if (copiesObj instanceof Integer) {
                                setmealDish.setCopies((Integer) copiesObj);
                            } else {
                                setmealDish.setCopies(Integer.parseInt(copiesObj.toString()));
                            }
                        }
                        setmealDish.setSort(0); // 默认排序
                        setmealDishService.save(setmealDish);
                    }
                }
                return R.success("新增成功");
            } else {
                return R.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增套餐失败：", e);
            return R.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询套餐详情
     * @param id 套餐ID
     * @return 套餐详情
     */
    @GetMapping("/{id}")
    public R<Setmeal> getById(@PathVariable Long id) {
        log.info("根据ID查询套餐详情：id={}", id);

        // 根据ID查询套餐
        Setmeal setmeal = setmealService.getById(id);

        if (setmeal != null) {
            return R.success(setmeal);
        } else {
            return R.error("套餐不存在");
        }
    }

    /**
     * 套餐批量起售/禁售
     * @param status 状态（1-起售，0-禁售）
     * @param ids 套餐ID数组
     * @return 操作结果
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam("ids") String ids) {
        log.info("套餐批量起售/禁售：status={}, ids={}", status, ids);

        // 将字符串转换为ID列表
        String[] idArray = ids.split(",");
        List<Long> idList = Arrays.stream(idArray)
                .map(Long::parseLong)
                .toList();

        // 批量更新状态
        boolean result = setmealService.updateStatusByIds(status, idList);

        if (result) {
            return R.success("操作成功");
        } else {
            return R.error("操作失败");
        }
    }

    /**
     * 查询套餐列表（不分页）
     * @param params 查询参数
     * @return 套餐列表
     */
    @GetMapping("/list")
    public R<List<Setmeal>> getSetmealList(@RequestParam Map<String, Object> params) {
        log.info("查询套餐列表：params={}", params);

        // 构建查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        
        // 处理店铺ID参数
        if (params.containsKey("storeId")) {
            try {
                Long storeId = Long.parseLong(params.get("storeId").toString());
                queryWrapper.eq(Setmeal::getStoreId, storeId);
            } catch (NumberFormatException e) {
                log.error("店铺ID参数格式错误：{}", params.get("storeId"));
            }
        }
        
        // 处理分类ID参数
        if (params.containsKey("categoryId")) {
            try {
                String categoryId = params.get("categoryId").toString();
                queryWrapper.eq(Setmeal::getCategoryId, categoryId);
            } catch (Exception e) {
                log.error("分类ID参数格式错误：{}", params.get("categoryId"));
            }
        }
        
        // 处理状态参数
        if (params.containsKey("status")) {
            try {
                Integer status = Integer.parseInt(params.get("status").toString());
                queryWrapper.eq(Setmeal::getStatus, status);
            } catch (NumberFormatException e) {
                log.error("状态参数格式错误：{}", params.get("status"));
            }
        } else {
            // 默认只查询起售状态的套餐
            queryWrapper.eq(Setmeal::getStatus, 1);
        }
        
        // 按照价格排序
        queryWrapper.orderByAsc(Setmeal::getPrice);

        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        return R.success(setmealList);
    }

    /**
     * 获取套餐包含的所有菜品详情
     * @param id 套餐ID
     * @return 套餐菜品列表
     */
    @GetMapping("/dish/{id}")
    public R<List<SetmealDish>> getDishList(@PathVariable Long id) {
        log.info("获取套餐包含的所有菜品详情：id={}", id);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        queryWrapper.orderByAsc(SetmealDish::getSort);

        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);

        return R.success(setmealDishList);
    }

}