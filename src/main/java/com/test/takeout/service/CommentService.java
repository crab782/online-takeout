package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.CommentVO;
import com.test.takeout.dto.CommentSubmitDTO;

public interface CommentService {
    
    void submitComment(String token, CommentSubmitDTO submitDTO);
    
    List<CommentVO> getCommentList(Long storeId);

}
