package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.test.takeout.service.CommentService;
import com.test.takeout.vo.CommentVO;
import com.test.takeout.dto.CommentSubmitDTO;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Override
    public void submitComment(String token, CommentSubmitDTO submitDTO) {
        // TODO: 实现提交评论的逻辑
    }
    
    @Override
    public List<CommentVO> getCommentList(Long storeId) {
        // TODO: 实现获取评论列表的逻辑
        return null;
    }

}
