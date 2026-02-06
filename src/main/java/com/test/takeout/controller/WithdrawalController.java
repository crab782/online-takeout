package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/backend/withdrawal")
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;

    @PostMapping("/apply")
    public ResponseVO<Void> applyWithdrawal(@RequestBody WithdrawalApplyDTO applyDTO) {
        withdrawalService.applyWithdrawal(applyDTO);
        return ResponseVO.success(null);
    }

    @GetMapping("/list")
    public ResponseVO<PageResponseVO<Withdrawal>> getWithdrawalList(@RequestParam Long shopId,
                                                                    @RequestParam Integer page,
                                                                    @RequestParam Integer pageSize) {
        return ResponseVO.success(withdrawalService.getWithdrawalList(shopId, page, pageSize));
    }
}
