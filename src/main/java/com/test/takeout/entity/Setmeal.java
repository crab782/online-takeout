package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("setmeal")
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String categoryId;

    private String name;

    private BigDecimal price;

    private Integer status;

    private String description;

    private String image;

    private Long storeId;

    private String storeName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    /**
     * 套餐包含的菜品描述（非数据库字段）
     */
    @TableField(exist = false)
    private String dishDesc;

    /**
     * 套餐库存（非数据库字段，动态计算）
     */
    @TableField(exist = false)
    private Integer stock;

    /**
     * 套餐库存状态（非数据库字段，动态计算）
     * 0-售罄，1-充足，2-紧张
     */
    @TableField(exist = false)
    private Integer stockStatus;
}
