package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import com.test.takeout.service.UserService;
import com.test.takeout.vo.ResponseVO;
import com.test.takeout.vo.UserVO;
import com.test.takeout.vo.BalanceVO;
import com.test.takeout.dto.UserLoginDTO;
import com.test.takeout.dto.UserRegisterDTO;

@RestController
@RequestMapping("/api/front/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ResponseVO<UserVO> login(@RequestBody UserLoginDTO loginDTO) {
        return ResponseVO.success(userService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseVO<Void> register(@RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return ResponseVO.success(null);
    }

    @GetMapping("/info")
    public ResponseVO<UserVO> getUserInfo(@RequestHeader("Authorization") String token) {
        return ResponseVO.success(userService.getUserInfo(token));
    }

    @PutMapping("/info")
    public ResponseVO<Void> updateUserInfo(@RequestHeader("Authorization") String token,
                                           @RequestBody UserVO userVO) {
        userService.updateUserInfo(token, userVO);
        return ResponseVO.success(null);
    }

    @GetMapping("/balance")
    public ResponseVO<BalanceVO> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseVO.success(userService.getBalance(token));
    }
}
