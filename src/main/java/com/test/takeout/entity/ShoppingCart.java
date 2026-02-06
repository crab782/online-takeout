package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 */
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;

    private Integer number;

    private String name;

    private String image;

    private Double amount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Long getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(Long setmealId) {
        this.setmealId = setmealId;
    }

    public String getDishFlavor() {
        return dishFlavor;
    }

    public void setDishFlavor(String dishFlavor) {
        this.dishFlavor = dishFlavor;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    // Constructors
    public ShoppingCart() {
    }

    public ShoppingCart(Long userId, Long dishId, Long setmealId, String dishFlavor, 
                       Integer number, String name, String image, Double amount) {
        this.userId = userId;
        this.dishId = dishId;
        this.setmealId = setmealId;
        this.dishFlavor = dishFlavor;
        this.number = number;
        this.name = name;
        this.image = image;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", userId=" + userId +
                ", dishId=" + dishId +
                ", setmealId=" + setmealId +
                ", dishFlavor='" + dishFlavor + '\'' +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", amount=" + amount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
