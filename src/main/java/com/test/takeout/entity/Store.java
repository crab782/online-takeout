package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺实体类
 */
@Data
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 店铺简介
     */
    private String description;

    /**
     * 店铺图片
     */
    private String image;

    /**
     * 营业状态（0-打烊，1-营业中）
     */
    private Integer status;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 营业开始时间
     */
    private String openTime;

    /**
     * 营业结束时间
     */
    private String closeTime;

    /**
     * 配送费
     */
    private java.math.BigDecimal deliveryFee;

    /**
     * 起送金额
     */
    private java.math.BigDecimal minOrderAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
