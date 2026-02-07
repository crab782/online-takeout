package com.test.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.takeout.entity.UserCheckin;

import java.time.LocalDate;
import java.util.List;

public interface UserCheckinService extends IService<UserCheckin> {

    boolean checkin(Long userId);

    boolean hasCheckedToday(Long userId);

    List<UserCheckin> getRecordsByMonth(Long userId, Integer year, Integer month);

    List<UserCheckin> getRecordsByWeek(Long userId);
}
