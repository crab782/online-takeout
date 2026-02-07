package com.test.takeout.service;

import com.test.takeout.vo.UserVO;
import com.test.takeout.vo.BalanceVO;
import com.test.takeout.dto.UserLoginDTO;
import com.test.takeout.dto.UserRegisterDTO;

public interface UserService {
    
    UserVO login(UserLoginDTO loginDTO);
    
    void register(UserRegisterDTO registerDTO);
    
    UserVO getUserInfo(String token);
    
    void updateUserInfo(String token, UserVO userVO);
    
    BalanceVO getBalance(String token);

}
