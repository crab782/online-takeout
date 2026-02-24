package com.test.takeout.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatusUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer status;

    private String statusStr;

    // 用于接收前端发送的字符串状态
    private String statusValue;
}
