package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.CartVO;
import com.test.takeout.dto.CartUpdateDTO;

public interface CartService {
    
    List<CartVO> getCartList(String token);
    
    void updateCart(String token, CartUpdateDTO updateDTO);
    
    void clearCart(String token);

}
