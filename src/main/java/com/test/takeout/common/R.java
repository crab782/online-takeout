package com.test.takeout.common;

import java.util.HashMap;
import java.util.Map;

public class R<T> {
    private Integer code;// 1 成功 0 失败
    private String msg;//错误信息
    private T data;//数据
    private Map map=new HashMap();//动态数据

    public static <T> R<T> success(T object){
        R<T> r=new R<>();
        r.data=object;
        r.code=1;
        return r;
    }

    public static <T> R<T> error(String msg){
        R<T> r=new R<>();
        r.msg=msg;
        r.code=0;
        return r;
    }

    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
