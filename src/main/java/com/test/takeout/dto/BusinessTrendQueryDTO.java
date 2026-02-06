package com.test.takeout.dto;
import lombok.Data;
import java.util.Date;

@Data
public class BusinessTrendQueryDTO {
    private Long shopId;
    private Date startDate;
    private Date endDate;
}
