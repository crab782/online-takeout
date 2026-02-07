package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.test.takeout.service.CartService;
import com.test.takeout.vo.CartVO;
import com.test.takeout.dto.CartUpdateDTO;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    
    @Override
    public List<CartVO> getCartList(String token) {
        // TODO: 实现获取购物车列表的逻辑
        return null;
    }
    
    @Override
    public void updateCart(String token, CartUpdateDTO updateDTO) {
        // TODO: 实现更新购物车的逻辑
    }
    
    @Override
    public void clearCart(String token) {
        // TODO: 实现清空购物车的逻辑
    }

}
