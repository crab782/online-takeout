package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import com.test.takeout.service.OrderService;
import com.test.takeout.vo.ResponseVO;
import com.test.takeout.vo.OrderVO;
import com.test.takeout.dto.OrderCreateDTO;
import com.test.takeout.dto.OrderCancelDTO;
import com.test.takeout.dto.OrderConfirmDTO;
import com.test.takeout.dto.OrderAgainDTO;

@RestController
@RequestMapping("/api/backend/order")
public class OrderController {

    /**
     * 后端订单管理控制器
     * 处理商家端的订单操作
     */

    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public ResponseVO<List<OrderVO>> getOrderList(@RequestHeader("Authorization") String token) {
        return ResponseVO.success(orderService.getOrderList(token));
    }

    @GetMapping("/detail")
    public ResponseVO<OrderVO> getOrderDetail(@RequestHeader("Authorization") String token,
                                              @RequestParam Long orderId) {
        return ResponseVO.success(orderService.getOrderDetail(token, orderId));
    }

    @PostMapping("/create")
    public ResponseVO<OrderVO> createOrder(@RequestHeader("Authorization") String token,
                                           @RequestBody OrderCreateDTO createDTO) {
        return ResponseVO.success(orderService.createOrder(token, createDTO));
    }

    @PostMapping("/cancel")
    public ResponseVO<Void> cancelOrder(@RequestHeader("Authorization") String token,
                                        @RequestBody OrderCancelDTO cancelDTO) {
        orderService.cancelOrder(token, cancelDTO);
        return ResponseVO.success(null);
    }

    @PostMapping("/confirm-receipt")
    public ResponseVO<Void> confirmReceipt(@RequestHeader("Authorization") String token,
                                           @RequestBody OrderConfirmDTO confirmDTO) {
        orderService.confirmReceipt(token, confirmDTO);
        return ResponseVO.success(null);
    }

    @PostMapping("/again")
    public ResponseVO<Void> orderAgain(@RequestHeader("Authorization") String token,
                                       @RequestBody OrderAgainDTO againDTO) {
        orderService.orderAgain(token, againDTO);
        return ResponseVO.success(null);
    }
}
