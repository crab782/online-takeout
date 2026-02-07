package com.test.takeout.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.Shop;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopMapper extends BaseMapper<Shop> {
    
}
