package com.test.takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String number;

    /**
     * 订单状态（0-待处理，1-已完成，2-已取消）
     */
    private Integer status;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 支付方式（1-微信，2-支付宝）
     */
    private Integer payMethod;

    /**
     * 支付状态（0-未支付，1-已支付）
     */
    private Integer payStatus;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 订单详情（非数据库字段）
     */
    @TableField(exist = false)
    private List<OrderDetail> orderDetails;

}