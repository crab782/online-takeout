package com.test.takeout.vo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private String orderNumber;
    private Long storeId;
    private String storeName;
    private String userName;
    private String phone;
    private String address;
    private BigDecimal amount;
    private String status;
    private Date orderTime;
    private Integer totalQuantity;
    private List<OrderDetailVO> orderDetails;



}
