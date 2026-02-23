package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.Dish;
import com.test.takeout.entity.Setmeal;
import com.test.takeout.entity.ShoppingCart;
import com.test.takeout.entity.Store;
import com.test.takeout.service.DishService;
import com.test.takeout.service.SetmealService;
import com.test.takeout.service.ShoppingCartService;
import com.test.takeout.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private StoreService storeService;

    /**
     * 查询购物车商品列表（按店铺分组）
     * @param request 请求对象
     * @return 购物车商品列表（按店铺分组）
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpServletRequest request) {
        log.info("查询购物车商品列表");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByAsc(ShoppingCart::getStoreId, ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        // 填充店铺名称
        for (ShoppingCart item : list) {
            if (item.getStoreId() != null && item.getStoreName() == null) {
                Store store = storeService.getById(item.getStoreId());
                if (store != null) {
                    item.setStoreName(store.getName());
                }
            }
        }

        return R.success(list);
    }

    /**
     * 添加商品到购物车
     * @param shoppingCart 购物车商品信息
     * @param request 请求对象
     * @return 添加结果
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        log.info("添加商品到购物车：shoppingCart={}", shoppingCart);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        Long storeId = shoppingCart.getStoreId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            if (shoppingCart.getDishFlavor() != null) {
                queryWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
            }
        } else if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart cartOne = shoppingCartService.getOne(queryWrapper);

        if (cartOne != null) {
            Integer number = cartOne.getNumber();
            cartOne.setNumber(number + shoppingCart.getNumber());
            shoppingCartService.updateById(cartOne);
        } else {
            if (dishId != null) {
                Dish dish = dishService.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setStoreId(dish.getStoreId());
                shoppingCart.setStoreName(dish.getStoreName());
            } else if (setmealId != null) {
                Setmeal setmeal = setmealService.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setStoreId(setmeal.getStoreId());
                shoppingCart.setStoreName(setmeal.getStoreName());
            }
            shoppingCart.setNumber(shoppingCart.getNumber());
            shoppingCartService.save(shoppingCart);
            cartOne = shoppingCart;
        }

        return R.success(cartOne);
    }

    /**
     * 购物车修改商品（减少数量）
     * @param shoppingCart 购物车商品信息
     * @param request 请求对象
     * @return 修改结果
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        log.info("购物车修改商品：shoppingCart={}", shoppingCart);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            if (shoppingCart.getDishFlavor() != null) {
                queryWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
            }
        } else if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart cartOne = shoppingCartService.getOne(queryWrapper);

        if (cartOne != null) {
            Integer number = cartOne.getNumber();
            cartOne.setNumber(number - shoppingCart.getNumber());
            if (cartOne.getNumber() <= 0) {
                shoppingCartService.removeById(cartOne.getId());
            } else {
                shoppingCartService.updateById(cartOne);
            }
        }

        return R.success(cartOne);
    }

    /**
     * 清空购物车
     * @param request 请求对象
     * @return 清空结果
     */
    @DeleteMapping("/clean")
    public R<String> clean(HttpServletRequest request) {
        log.info("清空购物车");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
}
