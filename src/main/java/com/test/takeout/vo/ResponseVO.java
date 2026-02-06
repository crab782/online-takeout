package com.test.takeout.vo;
import lombok.Data;

@Data
public class ResponseVO<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> ResponseVO<T> success(T data) {
        ResponseVO<T> response = new ResponseVO<>();
        response.setCode(1);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static <T> ResponseVO<T> fail(String msg) {
        ResponseVO<T> response = new ResponseVO<>();
        response.setCode(0);
        response.setMsg(msg);
        response.setData(null);
        return response;
    }
}
