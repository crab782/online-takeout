package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import com.test.takeout.service.ShopService;
import com.test.takeout.vo.ResponseVO;
import com.test.takeout.vo.ShopVO;

@RestController
@RequestMapping("/api/front/shop")
public class ShopController {

    @Resource
    private ShopService shopService;

    /**
     * 获取店铺列表
     * @return
     */
    @GetMapping("/list")
    public ResponseVO<List<ShopVO>> getShopList() {
        return ResponseVO.success(shopService.getShopList());
    }

    /**
     * 获取店铺详情
     * @param shopId
     * @return
     */
    @GetMapping("/detail")
    public ResponseVO<ShopVO> getShopDetail(@RequestParam Long shopId) {
        return ResponseVO.success(shopService.getShopDetail(shopId));
    }

}
