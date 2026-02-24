package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Category;
import com.test.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 分类管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    private final HttpServletRequest request;
    
    public CategoryController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 分页查询分类列表
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分类列表
     */
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        log.info("分页查询分类列表：page={}, pageSize={}", page, pageSize);

        // 获取当前登录用户的店铺ID
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId == null) {
            return R.error("请先登录");
        }

        // 构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据店铺ID过滤
        queryWrapper.eq(Category::getStoreId, storeId);
        
        // 添加排序条件，按照 sort 字段排序
        queryWrapper.orderByAsc(Category::getSort);

        // 执行分页查询
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据ID查询分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public R<Category> getById(@PathVariable Long id) {
        log.info("根据ID查询分类详情：id={}", id);

        // 获取当前登录用户的店铺ID
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId == null) {
            return R.error("请先登录");
        }

        // 根据ID查询分类
        Category category = categoryService.getById(id);

        if (category == null) {
            return R.error("分类不存在");
        }

        // 验证分类是否属于当前店铺
        if (!storeId.equals(category.getStoreId())) {
            return R.error("无权访问此分类");
        }

        return R.success(category);
    }

    /**
     * 删除分类（支持批量删除）
     * @param ids 分类ID数组
     * @return 删除结果
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") String ids) {
        log.info("删除分类：ids={}", ids);

        // 获取当前登录用户的店铺ID
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId == null) {
            return R.error("请先登录");
        }

        // 将字符串转换为ID列表
        String[] idArray = ids.split(",");
        List<Long> idList = Arrays.stream(idArray)
                .map(Long::parseLong)
                .toList();

        // 验证所有分类是否属于当前店铺
        for (Long id : idList) {
            Category category = categoryService.getById(id);
            if (category == null) {
                return R.error("分类不存在");
            }
            if (!storeId.equals(category.getStoreId())) {
                return R.error("无权删除此分类");
            }
        }

        // 批量删除分类
        boolean result = categoryService.removeByIds(idList);

        if (result) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 修改分类
     * @param category 分类信息
     * @return 修改结果
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类：category={}", category);

        // 获取当前登录用户的店铺ID
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId == null) {
            return R.error("请先登录");
        }

        // 验证分类是否属于当前店铺
        Category existingCategory = categoryService.getById(category.getId());
        if (existingCategory == null) {
            return R.error("分类不存在");
        }
        if (!storeId.equals(existingCategory.getStoreId())) {
            return R.error("无权修改此分类");
        }

        // 更新分类
        boolean result = categoryService.updateById(category);

        if (result) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    /**
     * 新增分类
     * @param category 分类信息
     * @return 新增结果
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增分类：category={}", category);

        // 获取当前登录用户的店铺ID
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId == null) {
            return R.error("请先登录");
        }

        // 设置分类所属店铺
        category.setStoreId(storeId);

        // 保存分类
        boolean result = categoryService.save(category);

        if (result) {
            return R.success("新增成功");
        } else {
            return R.error("新增失败");
        }
    }

    /**
     * 获取菜品分类列表
     * @param params 查询参数
     * @return 菜品分类列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(@RequestParam Map<String, Object> params) {
        log.info("获取菜品分类列表：params={}", params);

        // 获取当前登录用户的店铺ID
        Long storeId = (Long) request.getAttribute("storeId");
        if (storeId == null) {
            return R.error("请先登录");
        }

        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 根据店铺ID过滤
        queryWrapper.eq(Category::getStoreId, storeId);
        // 添加排序条件，按照 sort 字段排序
        queryWrapper.orderByAsc(Category::getSort);

        // 执行查询
        List<Category> categoryList = categoryService.list(queryWrapper);

        return R.success(categoryList);
    }
}
