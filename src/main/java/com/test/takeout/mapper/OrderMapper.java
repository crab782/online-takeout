package com.test.takeout.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
}
