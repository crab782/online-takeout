package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.Setmeal;
import com.test.takeout.mapper.SetmealMapper;
import com.test.takeout.service.SetmealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    /**
     * 批量更新套餐状态
     * @param status 状态（1-起售，0-禁售）
     * @param ids 套餐ID列表
     * @return 更新结果
     */
    @Override
    public boolean updateStatusByIds(Integer status, List<Long> ids) {
        // 构建更新条件
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId, ids);
        updateWrapper.set(Setmeal::getStatus, status);

        // 执行批量更新
        return this.update(updateWrapper);
    }

}