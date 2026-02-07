package com.test.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.takeout.entity.Activity;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityService extends IService<Activity> {

    List<Activity> getActiveActivities();
}
