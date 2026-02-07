package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.Store;
import com.test.takeout.mapper.StoreMapper;
import com.test.takeout.service.StoreService;
import org.springframework.stereotype.Service;

/**
 * 店铺服务实现类
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
}
