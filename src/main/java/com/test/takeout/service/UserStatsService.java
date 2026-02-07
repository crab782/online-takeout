package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.UserStatsVO;
import com.test.takeout.vo.GenderDistributionVO;
import com.test.takeout.vo.AgeDistributionVO;
import com.test.takeout.vo.UserTrendVO;

public interface UserStatsService {
    
    UserStatsVO getUserStats();
    
    List<GenderDistributionVO> getGenderDistribution();
    
    List<AgeDistributionVO> getAgeDistribution();
    
    List<UserTrendVO> getUserConsumption();
    
    List<UserTrendVO> getUserTrend(String type);

}
