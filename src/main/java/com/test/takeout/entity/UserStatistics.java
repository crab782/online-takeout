package com.test.takeout.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_statistics")
public class UserStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false, length = 10)
    private String date;

    @Column(name = "total_users")
    private Integer totalUsers;

    @Column(name = "login_users")
    private Integer loginUsers;

    @Column(name = "new_users")
    private Integer newUsers;

    @Column(name = "male_users")
    private Integer maleUsers;

    @Column(name = "female_users")
    private Integer femaleUsers;

    @Column(name = "avg_consumption", precision = 10, scale = 2)
    private Double avgConsumption;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Constructors
    public UserStatistics() {
    }

    public UserStatistics(String date) {
        this.date = date;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
