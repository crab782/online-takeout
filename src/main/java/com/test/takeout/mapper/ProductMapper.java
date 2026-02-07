package com.test.takeout.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
}
