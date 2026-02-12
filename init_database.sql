-- 创建数据库
CREATE DATABASE IF NOT EXISTS takeout_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE takeout_system;

-- 1. 员工表
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `status` INT DEFAULT 1 COMMENT '状态（0-禁用，1-正常）',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- 2. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `sex` INT DEFAULT NULL COMMENT '性别（0-女，1-男）',
  `id_number` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `status` INT DEFAULT 1 COMMENT '状态（0-禁用，1-正常）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 3. 分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `type` INT DEFAULT NULL COMMENT '类型（1-菜品分类，2-套餐分类）',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT DEFAULT NULL COMMENT '创建人',
  `update_user` BIGINT DEFAULT NULL COMMENT '修改人',
  `store_id` BIGINT DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 4. 菜品表
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '菜品名称',
  `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
  `price` DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '图片',
  `status` INT DEFAULT 1 COMMENT '状态（0-停售，1-起售）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT DEFAULT NULL COMMENT '创建人',
  `update_user` BIGINT DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品表';

-- 5. 套餐表
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` VARCHAR(50) DEFAULT NULL COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '套餐名称',
  `price` DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `status` INT DEFAULT 1 COMMENT '状态（0-停售，1-起售）',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '图片',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` BIGINT DEFAULT NULL COMMENT '创建人',
  `update_user` BIGINT DEFAULT NULL COMMENT '修改人',
  `store_id` BIGINT DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐表';

-- 6. 套餐菜品关系表
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `setmeal_id` BIGINT NOT NULL COMMENT '套餐ID',
  `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '菜品名称',
  `copies` INT DEFAULT NULL COMMENT '份数',
  `sort` INT DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_setmeal_id` (`setmeal_id`),
  KEY `idx_dish_id` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐菜品关系表';

-- 7. 订单表
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` VARCHAR(50) NOT NULL COMMENT '订单编号',
  `status` INT DEFAULT 0 COMMENT '订单状态（0-待处理，1-已完成，2-已取消）',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `user_name` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `store_id` BIGINT DEFAULT NULL COMMENT '店铺ID',
  `store_name` VARCHAR(50) DEFAULT NULL COMMENT '店铺名称',
  `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '订单金额',
  `pay_method` INT DEFAULT NULL COMMENT '支付方式（1-微信，2-支付宝）',
  `pay_status` INT DEFAULT 0 COMMENT '支付状态（0-未支付，1-已支付）',
  `receiver` VARCHAR(50) DEFAULT NULL COMMENT '收货人',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '收货地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 8. 订单详情表
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '名称',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `dish_id` BIGINT DEFAULT NULL COMMENT '菜品ID',
  `setmeal_id` BIGINT DEFAULT NULL COMMENT '套餐ID',
  `number` INT DEFAULT NULL COMMENT '数量',
  `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';

-- 9. 购物车表
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '名称',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '图片',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `dish_id` BIGINT DEFAULT NULL COMMENT '菜品ID',
  `setmeal_id` BIGINT DEFAULT NULL COMMENT '套餐ID',
  `dish_flavor` VARCHAR(50) DEFAULT NULL COMMENT '菜品口味',
  `number` INT DEFAULT NULL COMMENT '数量',
  `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '金额',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 10. 地址簿表
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `consignee` VARCHAR(50) DEFAULT NULL COMMENT '收货人姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `sex` INT DEFAULT NULL COMMENT '性别（0-女，1-男）',
  `province_code` VARCHAR(20) DEFAULT NULL COMMENT '省级区划编号',
  `province_name` VARCHAR(50) DEFAULT NULL COMMENT '省级名称',
  `city_code` VARCHAR(20) DEFAULT NULL COMMENT '市级区划编号',
  `city_name` VARCHAR(50) DEFAULT NULL COMMENT '市级名称',
  `district_code` VARCHAR(20) DEFAULT NULL COMMENT '区级区划编号',
  `district_name` VARCHAR(50) DEFAULT NULL COMMENT '区级名称',
  `detail` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
  `label` VARCHAR(50) DEFAULT NULL COMMENT '标签（公司、家、学校）',
  `is_default` INT DEFAULT 0 COMMENT '是否默认（0-否，1-是）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地址簿表';

-- 11. 店铺表
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '店铺名称',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '店铺地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '店铺简介',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '店铺图片',
  `status` INT DEFAULT 1 COMMENT '营业状态（0-打烊，1-营业中）',
  `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
  `open_time` VARCHAR(10) DEFAULT NULL COMMENT '营业开始时间',
  `close_time` VARCHAR(10) DEFAULT NULL COMMENT '营业结束时间',
  `delivery_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '配送费',
  `min_order_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '起送金额',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';

-- 12. 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '评论内容',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `user_name` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '用户头像',
  `order_id` BIGINT DEFAULT NULL COMMENT '订单ID',
  `order_number` VARCHAR(50) DEFAULT NULL COMMENT '订单编号',
  `store_id` BIGINT DEFAULT NULL COMMENT '店铺ID',
  `rating` INT DEFAULT NULL COMMENT '评分（1-5）',
  `images` VARCHAR(500) DEFAULT NULL COMMENT '评论图片（多个图片路径，用逗号分隔）',
  `reply_content` VARCHAR(500) DEFAULT NULL COMMENT '回复内容',
  `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
  `status` INT DEFAULT 0 COMMENT '状态（0-未回复，1-已回复）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 13. 活动表
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '活动标题',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '活动描述',
  `image_url` VARCHAR(255) DEFAULT NULL COMMENT '活动图片',
  `status` INT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';

-- 14. 店铺收藏表
DROP TABLE IF EXISTS `store_favorite`;
CREATE TABLE `store_favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `store_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_store` (`user_id`, `store_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺收藏表';

-- 15. 用户签到表
DROP TABLE IF EXISTS `user_checkin`;
CREATE TABLE `user_checkin` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `checkin_date` DATE NOT NULL COMMENT '签到日期',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户签到表';

-- 插入初始数据

-- 插入管理员账号（密码：admin，MD5加密后为：21232f297a57a5a743894a0e4a801fc3）
INSERT INTO `employee` (`username`, `password`, `name`, `phone`, `email`, `status`) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '13800138000', 'admin@example.com', 1);

-- 插入测试用户（密码：123456）
INSERT INTO `user` (`username`, `password`, `name`, `phone`, `email`, `sex`, `status`) VALUES
('testuser', '123456', '测试用户', '13900139000', 'test@example.com', 1, 1);

-- 插入菜品分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`) VALUES
('热菜', 1, 1, 1),
('凉菜', 1, 2, 1),
('主食', 1, 3, 1),
('饮料', 1, 4, 1),
('套餐', 2, 1, 1);

-- 插入店铺分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`) VALUES
('中餐', 3, 1, 1),
('西餐', 3, 2, 1),
('快餐', 3, 3, 1),
('饮品', 3, 4, 1);

-- 插入测试店铺
INSERT INTO `store` (`name`, `address`, `phone`, `description`, `image`, `status`, `category_id`, `open_time`, `close_time`, `delivery_fee`, `min_order_amount`) VALUES
('美味餐厅', '北京市朝阳区建国路88号', '010-12345678', '提供美味的中餐和快餐', '', 1, 16, '09:00', '21:00', 5.00, 20.00),
('快餐小屋', '北京市海淀区中关村大街100号', '010-87654321', '快捷美味的快餐', '', 1, 17, '08:00', '22:00', 3.00, 15.00);

-- 插入测试菜品
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`) VALUES
('宫保鸡丁', 1, 38.00, '经典川菜，香辣可口', '', 1),
('鱼香肉丝', 1, 32.00, '酸甜可口，下饭神器', '', 1),
('凉拌黄瓜', 2, 12.00, '清爽开胃', '', 1),
('蛋炒饭', 3, 15.00, '经典主食', '', 1),
('可乐', 4, 5.00, '冰镇可乐', '', 1);

-- 插入测试套餐
INSERT INTO `setmeal` (`category_id`, `name`, `price`, `status`, `description`, `image`, `store_id`) VALUES
('5', '单人套餐', 45.00, 1, '包含一份主菜和一份主食', '', 16),
('5', '双人套餐', 88.00, 1, '包含两份主菜和两份主食', '', 16);

-- 插入测试活动
INSERT INTO `activity` (`title`, `description`, `image_url`, `status`, `start_time`, `end_time`) VALUES
('新用户专享', '新用户注册即送优惠券', '', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('限时优惠', '全场菜品8折优惠', '', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59');
