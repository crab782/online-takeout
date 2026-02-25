package com.test.takeout.service.impl;

import com.test.takeout.entity.WithdrawalRecord;
import com.test.takeout.mapper.WithdrawalRecordMapper;
import com.test.takeout.service.WithdrawalRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalRecordServiceImpl extends ServiceImpl<WithdrawalRecordMapper, WithdrawalRecord> implements WithdrawalRecordService {
}
