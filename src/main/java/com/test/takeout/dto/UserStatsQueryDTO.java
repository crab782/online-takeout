package com.test.takeout.dto;
import lombok.Data;

@Data
public class UserStatsQueryDTO {
    private String type;
    private String startDate;
    private String endDate;
}
