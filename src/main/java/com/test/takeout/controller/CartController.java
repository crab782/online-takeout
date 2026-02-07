package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import com.test.takeout.service.CartService;
import com.test.takeout.vo.ResponseVO;
import com.test.takeout.vo.CartVO;
import com.test.takeout.dto.CartUpdateDTO;

@RestController
@RequestMapping("/api/front/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @GetMapping("/list")
    public ResponseVO<List<CartVO>> getCartList(@RequestHeader("Authorization") String token) {
        return ResponseVO.success(cartService.getCartList(token));
    }

    @PostMapping("/update")
    public ResponseVO<Void> updateCart(@RequestHeader("Authorization") String token,
                                       @RequestBody CartUpdateDTO updateDTO) {
        cartService.updateCart(token, updateDTO);
        return ResponseVO.success(null);
    }

    @DeleteMapping("/clear")
    public ResponseVO<Void> clearCart(@RequestHeader("Authorization") String token) {
        cartService.clearCart(token);
        return ResponseVO.success(null);
    }
}
