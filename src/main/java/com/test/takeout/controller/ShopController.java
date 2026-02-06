package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/front/cart")
public class ShopController {

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
