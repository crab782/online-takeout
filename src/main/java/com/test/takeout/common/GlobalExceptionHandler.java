package com.test.takeout.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice




/**
 * 全局异常处理
 */

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String split=ex.getMessage().split(" ")[2] + "已存在";
            return R.error(split);
        }
        return R.error("未知错误");
    }





    @ExceptionHandler(BusinessException.class)
    public ResponseVO<Void> handleBusinessException(BusinessException e) {
        return ResponseVO.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO<Void> handleException(Exception e) {
        // 记录日志
        return ResponseVO.fail("系统异常，请稍后重试");
    }





}
