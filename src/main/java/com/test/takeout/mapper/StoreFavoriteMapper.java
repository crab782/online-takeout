package com.test.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.StoreFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺收藏数据访问接口
 */
@Mapper
public interface StoreFavoriteMapper extends BaseMapper<StoreFavorite> {
}
