package com.test.takeout.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用响应类
 */
public class R<T> {

    /**
     * 响应码：1成功，0失败
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 动态数据
     */
    private Map<String, Object> map = new HashMap<>();

    /**
     * 成功响应
     * @param object 响应数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        r.msg = "success";
        return r;
    }
    
    /**
     * 成功响应（带自定义消息）
     * @param msg 响应消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> R<T> success(String msg) {
        R<T> r = new R<>();
        r.code = 1;
        r.msg = msg;
        return r;
    }

    /**
     * 失败响应
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = 0;
        r.data = null;
        return r;
    }

    /**
     * 添加动态数据
     * @param key 键
     * @param value 值
     * @return 响应对象
     */
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    // Getter and Setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

}
