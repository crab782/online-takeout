package com.test.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.takeout.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    
}
