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

/**
 * 分类管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类列表
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分类列表
     */
    @GetMapping("/page")
    public R<Map<String, Object>> page(int page, int pageSize) {
        log.info("分页查询分类列表：page={}, pageSize={}", page, pageSize);

        // 构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，按照 sort 字段排序
        queryWrapper.orderByAsc(Category::getSort);

        // 执行分页查询
        categoryService.page(pageInfo, queryWrapper);

        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());

        return R.success(data);
    }

    /**
     * 根据ID查询分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public R<Category> getById(@PathVariable Long id) {
        log.info("根据ID查询分类详情：id={}", id);

        // 根据ID查询分类
        Category category = categoryService.getById(id);

        if (category == null) {
            return R.error("分类不存在");
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

        // 将字符串转换为ID列表
        String[] idArray = ids.split(",");
        List<Long> idList = Arrays.stream(idArray)
                .map(Long::parseLong)
                .toList();

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

        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，按照 sort 字段排序
        queryWrapper.orderByAsc(Category::getSort);

        // 执行查询
        List<Category> categoryList = categoryService.list(queryWrapper);

        return R.success(categoryList);
    }

}
