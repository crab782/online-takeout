-- 创建店铺提现账户表
CREATE TABLE IF NOT EXISTS `store_withdrawal_account` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `store_id` BIGINT(20) NOT NULL COMMENT '店铺ID',
  `account_type` INT(11) DEFAULT NULL COMMENT '账户类型：1-银行卡，2-支付宝，3-微信',
  `account_name` VARCHAR(255) DEFAULT NULL COMMENT '账户名称',
  `account_number` VARCHAR(255) DEFAULT NULL COMMENT '账号',
  `bank_name` VARCHAR(255) DEFAULT NULL COMMENT '银行名称',
  `bank_branch` VARCHAR(255) DEFAULT NULL COMMENT '银行支行',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺提现账户表';

-- 确保提现记录表结构正确
CREATE TABLE IF NOT EXISTS `withdrawal_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `store_id` BIGINT(20) NOT NULL COMMENT '店铺ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '提现金额',
  `status` INT(11) DEFAULT NULL COMMENT '状态：0-申请中，1-成功，2-失败',
  `bank_name` VARCHAR(255) DEFAULT NULL COMMENT '银行名称',
  `bank_account` VARCHAR(255) DEFAULT NULL COMMENT '银行账号',
  `account_name` VARCHAR(255) DEFAULT NULL COMMENT '账户名称',
  `apply_time` DATETIME DEFAULT NULL COMMENT '申请时间',
  `process_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录表';