package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.UserCheckin;
import com.test.takeout.mapper.UserCheckinMapper;
import com.test.takeout.service.UserCheckinService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class UserCheckinServiceImpl extends ServiceImpl<UserCheckinMapper, UserCheckin> implements UserCheckinService {

    @Override
    public boolean checkin(Long userId) {
        UserCheckin checkin = new UserCheckin();
        checkin.setUserId(userId);
        checkin.setCheckinDate(LocalDate.now());
        checkin.setCreateTime(LocalDateTime.now());
        return save(checkin);
    }

    @Override
    public boolean hasCheckedToday(Long userId) {
        LambdaQueryWrapper<UserCheckin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCheckin::getUserId, userId);
        queryWrapper.eq(UserCheckin::getCheckinDate, LocalDate.now());
        return count(queryWrapper) > 0;
    }

    @Override
    public List<UserCheckin> getRecordsByMonth(Long userId, Integer year, Integer month) {
        LambdaQueryWrapper<UserCheckin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCheckin::getUserId, userId);
        
        if (year != null && month != null) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
            queryWrapper.between(UserCheckin::getCheckinDate, startDate, endDate);
        }
        
        queryWrapper.orderByDesc(UserCheckin::getCheckinDate);
        return list(queryWrapper);
    }

    @Override
    public List<UserCheckin> getRecordsByWeek(Long userId) {
        LambdaQueryWrapper<UserCheckin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCheckin::getUserId, userId);
        
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        queryWrapper.between(UserCheckin::getCheckinDate, startOfWeek, endOfWeek);
        queryWrapper.orderByAsc(UserCheckin::getCheckinDate);
        
        return list(queryWrapper);
    }
}
