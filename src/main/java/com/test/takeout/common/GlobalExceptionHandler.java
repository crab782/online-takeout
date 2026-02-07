package com.test.takeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有异常
     * @param ex 异常对象
     * @return 统一响应格式
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R<Object> handleException(Exception ex) {
        log.error("发生异常：", ex);
        return R.error("系统内部错误");
    }

}
