package com.test.takeout.dto;
import lombok.Data;
import java.util.List;

@Data
public class OrderCreateDTO {

    private Long addressBookId;
    private Long storeId;
    private String storeName;
    private Double amount;
    private Integer totalQuantity;
    private List<OrderDetailDTO> orderDetails;
}
