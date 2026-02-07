package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.Comment;
import com.test.takeout.entity.Orders;
import com.test.takeout.service.CommentService;
import com.test.takeout.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/front/comment")
public class FrontCommentController {

    private final CommentService commentService;
    private final OrdersService ordersService;

    public FrontCommentController(CommentService commentService, OrdersService ordersService) {
        this.commentService = commentService;
        this.ordersService = ordersService;
    }

    @PostMapping("/submit")
    public R<String> submitComment(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        log.info("提交订单评论：{}", params);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            userId = 1L;
        }

        Object orderIdObj = params.get("orderId");
        Object orderNumberObj = params.get("orderNumber");
        Object ratingObj = params.get("rating");
        Object contentObj = params.get("content");
        Object imagesObj = params.get("images");

        if (orderIdObj == null || orderNumberObj == null || ratingObj == null || contentObj == null) {
            return R.error("订单ID、订单编号、评分和评论内容不能为空");
        }

        Long orderId = Long.valueOf(orderIdObj.toString());
        String orderNumber = orderNumberObj.toString();
        Integer rating = Integer.valueOf(ratingObj.toString());
        String content = contentObj.toString();

        if (rating < 1 || rating > 5) {
            return R.error("评分必须在1-5之间");
        }

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getOrderId, orderId);
        queryWrapper.eq(Comment::getUserId, userId);
        long count = commentService.count(queryWrapper);
        if (count > 0) {
            return R.error("该订单已评论");
        }

        Orders order = ordersService.getById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }

        Comment comment = new Comment();
        comment.setOrderId(orderId);
        comment.setOrderNumber(orderNumber);
        comment.setUserId(userId);
        comment.setRating(rating);
        comment.setContent(content);
        comment.setStoreId(order.getStoreId());
        comment.setStatus(0);

        if (imagesObj != null) {
            if (imagesObj instanceof List) {
                List<?> imageList = (List<?>) imagesObj;
                String imagesStr = String.join(",", imageList.stream().map(Object::toString).toArray(String[]::new));
                comment.setImages(imagesStr);
            } else if (imagesObj instanceof String) {
                comment.setImages((String) imagesObj);
            }
        }

        boolean result = commentService.save(comment);

        if (result) {
            return R.success("评论提交成功");
        } else {
            return R.error("评论提交失败");
        }
    }

    @GetMapping("/status/{orderId}")
    public R<Map<String, Object>> getCommentStatus(@PathVariable Long orderId, HttpServletRequest request) {
        log.info("获取订单评论状态：orderId={}", orderId);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            userId = 1L;
        }

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getOrderId, orderId);
        queryWrapper.eq(Comment::getUserId, userId);
        Comment comment = commentService.getOne(queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("hasCommented", comment != null);
        if (comment != null) {
            data.put("comment", comment);
        }

        return R.success(data);
    }

    @GetMapping("/list")
    public R<Map<String, Object>> getUserComments(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   HttpServletRequest request) {
        log.info("获取用户评论列表：page={}, pageSize={}", page, pageSize);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            userId = 1L;
        }

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getUserId, userId);
        queryWrapper.orderByDesc(Comment::getCreateTime);

        List<Comment> list = commentService.list(queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", list.size());

        return R.success(data);
    }
}
