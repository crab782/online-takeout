package com.test.takeout.vo;
import lombok.Data;
import java.util.Date;

@Data
public class CommentVO {
    private Long id;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long orderId;
    private String orderNumber;
    private Long storeId;
    private String productName;
    private Integer rating;
    private String content;
    private String images;
    private Date createdAt;
}
