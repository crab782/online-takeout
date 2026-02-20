package com.test.takeout.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器，用于测试接口是否能正常响应
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试接口
     * @return 测试响应
     */
    @GetMapping("/hello")
    public String hello() {
        log.info("开始测试接口");
        return "Hello, World!";
    }

}