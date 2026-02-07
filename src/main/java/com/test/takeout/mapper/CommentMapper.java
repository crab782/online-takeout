package com.test.takeout.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
}
