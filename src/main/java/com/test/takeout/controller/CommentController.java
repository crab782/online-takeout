package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Comment;
import com.test.takeout.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 评论管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/backend/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取评论列表
     * @param page 页码
     * @param pageSize 每页数量
     * @return 评论列表
     */
    @GetMapping("/list")
    public R<Map<String, Object>> list(int page, int pageSize) {
        log.info("获取评论列表：page={}, pageSize={}", page, pageSize);

        // 构造分页构造器
        Page<Comment> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加排序条件，按照创建时间倒序
        queryWrapper.orderByDesc(Comment::getCreateTime);

        // 执行分页查询
        commentService.page(pageInfo, queryWrapper);

        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());

        return R.success(data);
    }

    /**
     * 回复评论
     * @param commentId 评论ID
     * @param content 回复内容
     * @return 回复结果
     */
    @PostMapping("/reply")
    public R<String> reply(@RequestParam("commentId") Long commentId, @RequestParam("content") String content) {
        log.info("回复评论：commentId={}, content={}", commentId, content);

        // 查询评论是否存在
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            return R.error("评论不存在");
        }

        // 更新评论的回复内容和回复时间
        comment.setReplyContent(content);
        comment.setReplyTime(LocalDateTime.now());
        comment.setStatus(1); // 标记为已回复

        // 保存更新
        boolean result = commentService.updateById(comment);

        if (result) {
            return R.success("回复成功");
        } else {
            return R.error("回复失败");
        }
    }

    /**
     * 获取评论详情
     * @param id 评论ID
     * @return 评论详情
     */
    @GetMapping("/detail/{id}")
    public R<Comment> detail(@PathVariable Long id) {
        log.info("获取评论详情：id={}", id);

        // 查询评论详情
        Comment comment = commentService.getById(id);

        if (comment != null) {
            return R.success(comment);
        } else {
            return R.error("评论不存在");
        }
    }

    /**
     * 删除评论
     * @param id 评论ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable Long id) {
        log.info("删除评论：id={}", id);

        // 查询评论是否存在
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return R.error("评论不存在");
        }

        // 删除评论
        boolean result = commentService.removeById(id);

        if (result) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

}
