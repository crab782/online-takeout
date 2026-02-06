package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/platform/finance")
public class FinanceController {

    @Resource
    private FinanceService financeService;

    @GetMapping("/data")
    public ResponseVO<FinanceDataVO> getFinanceData(@RequestParam String month,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "20") Integer pageSize) {
        return ResponseVO.success(financeService.getFinanceData(month, page, pageSize));
    }

    @GetMapping("/withdrawal-records")
    public ResponseVO<PageResult<WithdrawalRecordVO>> getWithdrawalRecords(@RequestParam(defaultValue = "1") Integer page,
                                                                           @RequestParam(defaultValue = "20") Integer pageSize,
                                                                           @RequestParam(required = false) String search,
                                                                           @RequestParam(required = false) String status) {
        return ResponseVO.success(financeService.getWithdrawalRecords(page, pageSize, search, status));
    }

    @GetMapping("/withdrawal-trend")
    public ResponseVO<List<WithdrawalTrendVO>> getWithdrawalTrend() {
        return ResponseVO.success(financeService.getWithdrawalTrend());
    }

    @GetMapping("/cash-flow-trend")
    public ResponseVO<List<CashFlowTrendVO>> getCashFlowTrend() {
        return ResponseVO.success(financeService.getCashFlowTrend());
    }

    @PostMapping("/process-withdrawal")
    public ResponseVO<Void> processWithdrawal(@RequestBody WithdrawalProcessDTO processDTO) {
        financeService.processWithdrawal(processDTO);
        return ResponseVO.success(null);
    }
}
