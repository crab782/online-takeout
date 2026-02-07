package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import com.test.takeout.service.UserStatsService;
import com.test.takeout.vo.ResponseVO;
import com.test.takeout.vo.UserStatsVO;
import com.test.takeout.vo.GenderDistributionVO;
import com.test.takeout.vo.AgeDistributionVO;
import com.test.takeout.vo.UserTrendVO;

@RestController
@RequestMapping("/api/platform/user")
public class PlatformUserController {

    @Resource
    private UserStatsService userStatsService;

    @GetMapping("/stats")
    public ResponseVO<UserStatsVO> getUserStats() {
        return ResponseVO.success(userStatsService.getUserStats());
    }

    @GetMapping("/gender-distribution")
    public ResponseVO<List<GenderDistributionVO>> getGenderDistribution() {
        return ResponseVO.success(userStatsService.getGenderDistribution());
    }

    @GetMapping("/age-distribution")
    public ResponseVO<List<AgeDistributionVO>> getAgeDistribution() {
        return ResponseVO.success(userStatsService.getAgeDistribution());
    }

    @GetMapping("/consumption")
    public ResponseVO<List<UserTrendVO>> getUserConsumption() {
        return ResponseVO.success(userStatsService.getUserConsumption());
    }

    @GetMapping("/trend")
    public ResponseVO<List<UserTrendVO>> getUserTrend(@RequestParam String type) {
        return ResponseVO.success(userStatsService.getUserTrend(type));
    }
}
