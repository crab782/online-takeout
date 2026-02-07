package com.test.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.Store;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺数据访问接口
 */
@Mapper
public interface StoreMapper extends BaseMapper<Store> {
}
