package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.OrderVO;
import com.test.takeout.dto.OrderCreateDTO;
import com.test.takeout.dto.OrderCancelDTO;
import com.test.takeout.dto.OrderConfirmDTO;
import com.test.takeout.dto.OrderAgainDTO;

public interface OrderService {
    
    List<OrderVO> getOrderList(String token);
    
    OrderVO getOrderDetail(String token, Long orderId);
    
    OrderVO createOrder(String token, OrderCreateDTO createDTO);
    
    void cancelOrder(String token, OrderCancelDTO cancelDTO);
    
    void confirmReceipt(String token, OrderConfirmDTO confirmDTO);
    
    void orderAgain(String token, OrderAgainDTO againDTO);

}
