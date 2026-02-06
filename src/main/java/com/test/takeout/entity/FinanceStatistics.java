package com.test.takeout.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "finance_statistics")
public class FinanceStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month", nullable = false, length = 7)
    private String month;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "shop_count")
    private Integer shopCount;

    @Column(name = "average_revenue", precision = 12, scale = 2)
    private BigDecimal averageRevenue;

    @Column(name = "total_withdrawal", precision = 12, scale = 2)
    private BigDecimal totalWithdrawal;

    @Column(name = "withdrawal_count")
    private Integer withdrawalCount;

    @Column(name = "platform_cash_flow", precision = 12, scale = 2)
    private BigDecimal platformCashFlow;

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getShopCount() {
        return shopCount;
    }

    public void setShopCount(Integer shopCount) {
        this.shopCount = shopCount;
    }

    public BigDecimal getAverageRevenue() {
        return averageRevenue;
    }

    public void setAverageRevenue(BigDecimal averageRevenue) {
        this.averageRevenue = averageRevenue;
    }

    public BigDecimal getTotalWithdrawal() {
        return totalWithdrawal;
    }

    public void setTotalWithdrawal(BigDecimal totalWithdrawal) {
        this.totalWithdrawal = totalWithdrawal;
    }

    public Integer getWithdrawalCount() {
        return withdrawalCount;
    }

    public void setWithdrawalCount(Integer withdrawalCount) {
        this.withdrawalCount = withdrawalCount;
    }

    public BigDecimal getPlatformCashFlow() {
        return platformCashFlow;
    }

    public void setPlatformCashFlow(BigDecimal platformCashFlow) {
        this.platformCashFlow = platformCashFlow;
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
    public FinanceStatistics() {
    }

    public FinanceStatistics(String month, BigDecimal totalRevenue, Integer shopCount) {
        this.month = month;
        this.totalRevenue = totalRevenue;
        this.shopCount = shopCount;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
