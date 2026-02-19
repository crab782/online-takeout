package com.test.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.takeout.common.R;
import com.test.takeout.entity.Activity;
import com.test.takeout.entity.UserCheckin;
import com.test.takeout.service.ActivityService;
import com.test.takeout.service.UserCheckinService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final UserCheckinService userCheckinService;
    private final ActivityService activityService;

    public ActivityController(UserCheckinService userCheckinService, ActivityService activityService) {
        this.userCheckinService = userCheckinService;
        this.activityService = activityService;
    }

    /**
     * 获取用户优惠券总金额
     * @return 优惠券总金额
     */
    @GetMapping("/coupon/amount")
    public R<BigDecimal> getCouponAmount() {
        log.info("获取用户优惠券总金额");

        BigDecimal couponAmount = new BigDecimal("0.00");

        return R.success(couponAmount);
    }

    /**
     * 获取用户签到记录
     * @param year 年份（可选）
     * @param month 月份（可选）
     * @return 签到记录
     */
    @GetMapping("/checkin/records")
    public R<Map<String, Object>> getCheckinRecords(@RequestParam(required = false) Integer year,
                                                     @RequestParam(required = false) Integer month,
                                                     HttpServletRequest request) {
        log.info("获取用户签到记录，年份：{}，月份：{}", year, month);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }

        LocalDate now = LocalDate.now();
        if (year == null) {
            year = now.getYear();
        }
        if (month == null) {
            month = now.getMonthValue();
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        List<UserCheckin> checkinList = userCheckinService.getRecordsByMonth(userId, year, month);

        Set<LocalDate> checkedDates = new HashSet<>();
        for (UserCheckin checkin : checkinList) {
            checkedDates.add(checkin.getCheckinDate());
        }

        List<Map<String, Object>> records = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            Map<String, Object> record = new HashMap<>();
            record.put("date", String.format("%04d-%02d-%02d", year, month, day));
            record.put("checkedIn", checkedDates.contains(date));
            records.add(record);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("year", year);
        data.put("month", month);
        data.put("records", records);

        return R.success(data);
    }

    /**
     * 执行签到操作
     * @return 签到结果
     */
    @PostMapping("/checkin")
    public R<String> doCheckin(HttpServletRequest request) {
        log.info("执行签到操作");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }

        if (userCheckinService.hasCheckedToday(userId)) {
            return R.error("今日已签到");
        }

        boolean success = userCheckinService.checkin(userId);
        if (success) {
            return R.success("签到成功");
        } else {
            return R.error("签到失败");
        }
    }

    /**
     * 获取本周签到情况
     * @return 本周签到情况
     */
    @GetMapping("/checkin/week")
    public R<Map<String, Object>> getWeekCheckin(HttpServletRequest request) {
        log.info("获取本周签到情况");

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录，请先登录");
        }

        List<UserCheckin> checkinList = userCheckinService.getRecordsByWeek(userId);

        Set<LocalDate> checkedDates = new HashSet<>();
        for (UserCheckin checkin : checkinList) {
            checkedDates.add(checkin.getCheckinDate());
        }

        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

        List<Map<String, Object>> weekRecords = new ArrayList<>();
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            Map<String, Object> record = new HashMap<>();
            record.put("day", weekDays[i]);
            record.put("date", date.toString());
            record.put("checkedIn", checkedDates.contains(date));
            weekRecords.add(record);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("weekRecords", weekRecords);
        data.put("checkedCount", checkedDates.size());

        return R.success(data);
    }

    /**
     * 获取活动列表
     * @return 活动列表
     */
    @GetMapping("/list")
    public R<List<Activity>> getActivityList() {
        log.info("获取活动列表");

        List<Activity> activities = activityService.getActiveActivities();

        return R.success(activities);
    }
}
