package com.test.takeout.dto;
import lombok.Data;
import java.util.Date;

@Data
public class OrderQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Long shopId;
    private String orderNumber;
    private Date beginTime;
    private Date endTime;
    private String status;
}
