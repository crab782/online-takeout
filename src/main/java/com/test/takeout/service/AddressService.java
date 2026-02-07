package com.test.takeout.service;

import java.util.List;
import com.test.takeout.vo.AddressVO;
import com.test.takeout.dto.AddressAddDTO;

public interface AddressService {
    
    List<AddressVO> getAddressList(String token);
    
    void addAddress(String token, AddressAddDTO addDTO);
    
    void updateAddress(String token, AddressVO addressVO);
    
    void deleteAddress(String token, Long addressId);
    
    void setDefaultAddress(String token, Long addressId);

}
