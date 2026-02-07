package com.test.takeout.controller;

import com.test.takeout.common.R;
import com.test.takeout.entity.User;
import com.test.takeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端用户控制器，处理前端用户相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class FrontUserController {

    private final UserService userService;

    public FrontUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 手机号密码登录
     * @param loginData 登录数据
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        log.info("手机号密码登录：loginData={}", loginData);

        String phone = loginData.get("phone");
        String code = loginData.get("code");

        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }

        if (code == null || code.isEmpty()) {
            return R.error("密码不能为空");
        }

        // 查询用户信息
        // 这里假设使用手机号和密码进行登录验证
        // 实际项目中应该使用加密后的密码进行比对
        // 这里简化处理，直接查询手机号对应的用户
        // 实际项目中需要根据业务需求实现登录逻辑
        
        // 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", "mock_token_" + System.currentTimeMillis());
        result.put("userId", 1L);
        result.put("phone", phone);
        
        return R.success(result);
    }

    /**
     * 获取手机验证码
     * @param params 验证码参数
     * @return 验证码发送结果
     */
    @GetMapping("/code")
    public R<String> getPhoneCode(@RequestParam Map<String, String> params) {
        log.info("获取手机验证码：params={}", params);

        String phone = params.get("phone");

        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }

        // 这里应该实现发送验证码的逻辑
        // 实际项目中需要调用短信服务发送验证码
        // 这里简化处理，直接返回成功
        
        return R.success("验证码发送成功");
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/loginout")
    public R<String> loginout() {
        log.info("退出登录");

        return R.success("退出成功");
    }

}
