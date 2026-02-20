package com.test.takeout.controller;

import com.test.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 简单测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/simple")
public class SimpleTestController {

    @GetMapping("/test")
    public R<String> test() {
        try {
            log.info("简单测试");
            return R.success("简单测试成功");
        } catch (Exception e) {
            log.error("简单测试异常：", e);
            return R.error("简单测试异常：" + e.getMessage());
        }
    }

    @PostMapping("/test")
    public R<String> testPost(@RequestBody String body) {
        try {
            log.info("简单POST测试：{}", body);
            return R.success("简单POST测试成功");
        } catch (Exception e) {
            log.error("简单POST测试异常：", e);
            return R.error("简单POST测试异常：" + e.getMessage());
        }
    }
}
