package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额实体类
 */
@TableName("user_balance")
public class UserBalance implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private BigDecimal balance;

    private BigDecimal totalRecharge;

    private BigDecimal totalConsume;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalRecharge() {
        return totalRecharge;
    }

    public void setTotalRecharge(BigDecimal totalRecharge) {
        this.totalRecharge = totalRecharge;
    }

    public BigDecimal getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(BigDecimal totalConsume) {
        this.totalConsume = totalConsume;
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
    public UserBalance() {
    }

    public UserBalance(Long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
        this.totalRecharge = BigDecimal.ZERO;
        this.totalConsume = BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "UserBalance{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                ", totalRecharge=" + totalRecharge +
                ", totalConsume=" + totalConsume +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
