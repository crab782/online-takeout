package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.StoreFavorite;
import com.test.takeout.mapper.StoreFavoriteMapper;
import com.test.takeout.service.StoreFavoriteService;
import org.springframework.stereotype.Service;

/**
 * 店铺收藏服务实现类
 */
@Service
public class StoreFavoriteServiceImpl extends ServiceImpl<StoreFavoriteMapper, StoreFavorite> implements StoreFavoriteService {
}
