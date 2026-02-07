package com.test.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.takeout.entity.AddressBook;
import com.test.takeout.mapper.AddressBookMapper;
import com.test.takeout.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * 地址簿服务实现类
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
