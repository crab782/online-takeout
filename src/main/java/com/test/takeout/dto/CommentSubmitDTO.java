package com.test.takeout.dto;
import lombok.Data;

@Data
public class CommentSubmitDTO {
    private Long orderId;
    private String orderNumber;
    private Long storeId;
    private Long productId;
    private String productName;
    private Integer rating;
    private String content;
    private String images;
}
