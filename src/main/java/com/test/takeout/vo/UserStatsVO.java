package com.test.takeout.vo;
import lombok.Data;

@Data
public class UserStatsVO {
    private Integer totalUsers;
    private Integer totalLoginUsers;
    private Integer newUsers;
    private Double avgConsumption;
}
