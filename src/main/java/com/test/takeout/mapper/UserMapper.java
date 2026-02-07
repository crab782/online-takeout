package com.test.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
