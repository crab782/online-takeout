package com.test.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 批量更新套餐状态
     * @param status 状态（1-起售，0-禁售）
     * @param ids 套餐ID列表
     * @return 更新结果
     */
    boolean updateStatusByIds(Integer status, List<Long> ids);
}