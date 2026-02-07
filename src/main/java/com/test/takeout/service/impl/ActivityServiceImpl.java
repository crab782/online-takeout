package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.Activity;
import com.test.takeout.mapper.ActivityMapper;
import com.test.takeout.service.ActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Override
    public List<Activity> getActiveActivities() {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getStatus, 1);
        queryWrapper.le(Activity::getStartTime, LocalDateTime.now());
        queryWrapper.ge(Activity::getEndTime, LocalDateTime.now());
        queryWrapper.orderByDesc(Activity::getCreateTime);
        return list(queryWrapper);
    }
}
