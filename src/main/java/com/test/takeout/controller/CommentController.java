package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import com.test.takeout.service.CommentService;
import com.test.takeout.vo.ResponseVO;
import com.test.takeout.vo.CommentVO;
import com.test.takeout.dto.CommentSubmitDTO;

@RestController
@RequestMapping("/api/front/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/submit")
    public ResponseVO<Void> submitComment(@RequestHeader("Authorization") String token,
                                          @RequestBody CommentSubmitDTO submitDTO) {
        commentService.submitComment(token, submitDTO);
        return ResponseVO.success(null);
    }

    @GetMapping("/list")
    public ResponseVO<List<CommentVO>> getCommentList(@RequestParam Long storeId) {
        return ResponseVO.success(commentService.getCommentList(storeId));
    }
}
