package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.ShopVO;

public interface ShopService {
    
    List<ShopVO> getShopList();
    
    ShopVO getShopDetail(Long shopId);

}
