package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long orderId;

    private Long dishId;

    private Long setmealId;

    private Integer number;

    private BigDecimal amount;

    private String image;

    /**
     * 套餐包含的菜品描述（非数据库字段）
     */
    @TableField(exist = false)
    private String dishDesc;
}
