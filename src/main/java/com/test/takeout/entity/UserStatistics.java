package com.test.takeout.entity;
import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户统计实体类
 */
@TableName("user_statistics")
public class UserStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String date;

    private Integer totalUsers;

    private Integer loginUsers;

    private Integer newUsers;

    private Integer maleUsers;

    private Integer femaleUsers;

    private Double avgConsumption;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getLoginUsers() {
        return loginUsers;
    }

    public void setLoginUsers(Integer loginUsers) {
        this.loginUsers = loginUsers;
    }

    public Integer getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Integer newUsers) {
        this.newUsers = newUsers;
    }

    public Integer getMaleUsers() {
        return maleUsers;
    }

    public void setMaleUsers(Integer maleUsers) {
        this.maleUsers = maleUsers;
    }

    public Integer getFemaleUsers() {
        return femaleUsers;
    }

    public void setFemaleUsers(Integer femaleUsers) {
        this.femaleUsers = femaleUsers;
    }

    public Double getAvgConsumption() {
        return avgConsumption;
    }

    public void setAvgConsumption(Double avgConsumption) {
        this.avgConsumption = avgConsumption;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Constructors
    public UserStatistics() {
    }

    public UserStatistics(String date) {
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
