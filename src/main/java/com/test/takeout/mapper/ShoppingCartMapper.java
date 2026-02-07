package com.test.takeout.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    
}
