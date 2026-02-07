package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.Dish;
import com.test.takeout.mapper.DishMapper;
import com.test.takeout.service.DishService;
import org.springframework.stereotype.Service;

/**
 * 菜品服务实现类
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}