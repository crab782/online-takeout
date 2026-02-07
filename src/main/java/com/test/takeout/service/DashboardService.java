package com.test.takeout.service;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import com.test.takeout.vo.OrderVO;
import com.test.takeout.vo.BusinessTrendVO;
import com.test.takeout.vo.OrderStatusDistributionVO;
import com.test.takeout.vo.OrderPriceDistributionVO;
import com.test.takeout.vo.ShopViewVO;
import com.test.takeout.dto.BusinessTrendQueryDTO;
import com.test.takeout.dto.ShopViewsQueryDTO;

public interface DashboardService {
    
    List<OrderVO> getPendingOrders(Long shopId);
    
    Map<String, Object> getTodayOrderStats(Long shopId);
    
    List<BusinessTrendVO> getBusinessTrend(BusinessTrendQueryDTO queryDTO);
    
    List<Map<String, Object>> getUserExpenses(Long shopId, String sortBy, String sortOrder);
    
    List<OrderStatusDistributionVO> getTodayOrderStatus(Long shopId);
    
    List<OrderPriceDistributionVO> getTodayOrderPrice(Long shopId);
    
    List<ShopViewVO> getShopViews(ShopViewsQueryDTO queryDTO);
    
    BigDecimal getAvailableWithdrawalAmount(Long shopId);

}

