package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.test.takeout.service.ShopService;
import com.test.takeout.vo.ShopVO;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    
    @Override
    public List<ShopVO> getShopList() {
        // TODO: 实现获取店铺列表的逻辑
        return null;
    }
    
    @Override
    public ShopVO getShopDetail(Long shopId) {
        // TODO: 实现获取店铺详情的逻辑
        return null;
    }

}
