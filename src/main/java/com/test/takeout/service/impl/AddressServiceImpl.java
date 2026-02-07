package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.test.takeout.service.AddressService;
import com.test.takeout.vo.AddressVO;
import com.test.takeout.dto.AddressAddDTO;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    
    @Override
    public List<AddressVO> getAddressList(String token) {
        // TODO: 实现获取地址列表的逻辑
        return null;
    }
    
    @Override
    public void addAddress(String token, AddressAddDTO addDTO) {
        // TODO: 实现添加地址的逻辑
    }
    
    @Override
    public void updateAddress(String token, AddressVO addressVO) {
        // TODO: 实现更新地址的逻辑
    }
    
    @Override
    public void deleteAddress(String token, Long addressId) {
        // TODO: 实现删除地址的逻辑
    }
    
    @Override
    public void setDefaultAddress(String token, Long addressId) {
        // TODO: 实现设置默认地址的逻辑
    }

}
