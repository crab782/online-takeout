package com.test.takeout.service.impl;

import org.springframework.stereotype.Service;
import com.test.takeout.service.UserService;
import com.test.takeout.vo.UserVO;
import com.test.takeout.vo.BalanceVO;
import com.test.takeout.dto.UserLoginDTO;
import com.test.takeout.dto.UserRegisterDTO;

@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public UserVO login(UserLoginDTO loginDTO) {
        // TODO: 实现用户登录的逻辑
        return null;
    }
    
    @Override
    public void register(UserRegisterDTO registerDTO) {
        // TODO: 实现用户注册的逻辑
    }
    
    @Override
    public UserVO getUserInfo(String token) {
        // TODO: 实现获取用户信息的逻辑
        return null;
    }
    
    @Override
    public void updateUserInfo(String token, UserVO userVO) {
        // TODO: 实现更新用户信息的逻辑
    }
    
    @Override
    public BalanceVO getBalance(String token) {
        // TODO: 实现获取用户余额的逻辑
        return null;
    }

}
