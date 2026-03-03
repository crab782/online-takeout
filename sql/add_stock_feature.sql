-- ============================================
-- 库存功能数据库结构升级脚本
-- ============================================

-- 1. 为菜品表添加库存字段
ALTER TABLE `dish`
ADD COLUMN `stock` INT DEFAULT 999 COMMENT '库存数量，默认999表示充足',
ADD COLUMN `stock_status` TINYINT DEFAULT 1 COMMENT '库存状态：1-充足，2-紧张，0-售罄';

-- 2. 为套餐表添加库存字段
ALTER TABLE `setmeal`
ADD COLUMN `stock` INT DEFAULT 999 COMMENT '库存数量，默认999表示充足',
ADD COLUMN `stock_status` TINYINT DEFAULT 1 COMMENT '库存状态：1-充足，2-紧张，0-售罄';

-- 3. 创建库存日志表（用于记录库存变动历史）
CREATE TABLE IF NOT EXISTS `stock_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type` TINYINT NOT NULL COMMENT '类型：1-菜品，2-套餐',
    `dish_id` BIGINT DEFAULT NULL COMMENT '菜品ID',
    `setmeal_id` BIGINT DEFAULT NULL COMMENT '套餐ID',
    `operation` TINYINT NOT NULL COMMENT '操作：1-入库，2-出库（下单），3-手动调整',
    `quantity` INT NOT NULL COMMENT '变动数量（正数增加，负数减少）',
    `before_stock` INT NOT NULL COMMENT '变动前库存',
    `after_stock` INT NOT NULL COMMENT '变动后库存',
    `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID（下单时）',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user` BIGINT DEFAULT NULL COMMENT '操作人',
    PRIMARY KEY (`id`),
    KEY `idx_dish_id` (`dish_id`),
    KEY `idx_setmeal_id` (`setmeal_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动日志表';

-- 4. 初始化现有数据的库存（设置为默认值999）
UPDATE `dish` SET `stock` = 999, `stock_status` = 1 WHERE `stock` IS NULL;
UPDATE `setmeal` SET `stock` = 999, `stock_status` = 1 WHERE `stock` IS NULL;

-- 5. 添加触发器：当库存变化时自动更新库存状态
-- 菜品库存状态自动更新
DELIMITER //
CREATE TRIGGER `trg_dish_stock_status`
BEFORE UPDATE ON `dish`
FOR EACH ROW
BEGIN
    IF NEW.stock <= 0 THEN
        SET NEW.stock_status = 0;
    ELSEIF NEW.stock <= 10 THEN
        SET NEW.stock_status = 2;
    ELSE
        SET NEW.stock_status = 1;
    END IF;
END//
DELIMITER ;

-- 套餐库存状态自动更新
DELIMITER //
CREATE TRIGGER `trg_setmeal_stock_status`
BEFORE UPDATE ON `setmeal`
FOR EACH ROW
BEGIN
    IF NEW.stock <= 0 THEN
        SET NEW.stock_status = 0;
    ELSEIF NEW.stock <= 10 THEN
        SET NEW.stock_status = 2;
    ELSE
        SET NEW.stock_status = 1;
    END IF;
END//
DELIMITER ;

-- ============================================
-- 查询验证
-- ============================================

-- 查看菜品表结构
DESC `dish`;

-- 查看套餐表结构
DESC `setmeal`;

-- 查看库存日志表结构
DESC `stock_log`;

-- 查看当前菜品库存情况
SELECT `id`, `name`, `stock`, `stock_status` FROM `dish` LIMIT 10;

-- 查看当前套餐库存情况
SELECT `id`, `name`, `stock`, `stock_status` FROM `setmeal` LIMIT 10;



//输出结果
mysql> -- ============================================
mysql> -- 库存功能数据库结构升级脚本
mysql> -- ============================================
mysql>
mysql> -- 1. 为菜品表添加库存字段
mysql> ALTER TABLE `dish`
    -> ADD COLUMN `stock` INT DEFAULT 999 COMMENT '库存数量，默认999表示充足',
    -> ADD COLUMN `stock_status` TINYINT DEFAULT 1 COMMENT '库存状态：1-充足，2-紧张，0-售罄';
Query OK, 0 rows affected (0.34 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql>
mysql> -- 2. 为套餐表添加库存字段
mysql> ALTER TABLE `setmeal`
    -> ADD COLUMN `stock` INT DEFAULT 999 COMMENT '库存数量，默认999表示充足',
    -> ADD COLUMN `stock_status` TINYINT DEFAULT 1 COMMENT '库存状态：1-充足，2-紧张，0-售罄';
Query OK, 0 rows affected (0.04 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql>
mysql> -- 3. 创建库存日志表（用于记录库存变动历史）
mysql> CREATE TABLE IF NOT EXISTS `stock_log` (
    ->     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    ->     `type` TINYINT NOT NULL COMMENT '类型：1-菜品，2-套餐',
    ->     `dish_id` BIGINT DEFAULT NULL COMMENT '菜品ID',
    ->     `setmeal_id` BIGINT DEFAULT NULL COMMENT '套餐ID',
    ->     `operation` TINYINT NOT NULL COMMENT '操作：1-入库，2-出库（下单），3-手动调整',
    ->     `quantity` INT NOT NULL COMMENT '变动数量（正数增加，负数减少）',
    ->     `before_stock` INT NOT NULL COMMENT '变动前库存',
    ->     `after_stock` INT NOT NULL COMMENT '变动后库存',
    ->     `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID（下单时）',
    ->     `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    ->     `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ->     `create_user` BIGINT DEFAULT NULL COMMENT '操作人',
    ->     PRIMARY KEY (`id`),
    ->     KEY `idx_dish_id` (`dish_id`),
    ->     KEY `idx_setmeal_id` (`setmeal_id`),
    ->     KEY `idx_order_id` (`order_id`),
    ->     KEY `idx_create_time` (`create_time`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动日志表';
Query OK, 0 rows affected (0.05 sec)

mysql>
mysql> -- 4. 初始化现有数据的库存（设置为默认值999）
mysql> UPDATE `dish` SET `stock` = 999, `stock_status` = 1 WHERE `stock` IS NULL;
Query OK, 0 rows affected (0.01 sec)
Rows matched: 0  Changed: 0  Warnings: 0

mysql> UPDATE `setmeal` SET `stock` = 999, `stock_status` = 1 WHERE `stock` IS NULL;
Query OK, 0 rows affected (0.00 sec)
Rows matched: 0  Changed: 0  Warnings: 0

mysql>
mysql> -- 5. 添加触发器：当库存变化时自动更新库存状态
mysql> -- 菜品库存状态自动更新
mysql> DELIMITER //
mysql> CREATE TRIGGER `trg_dish_stock_status`
    -> BEFORE UPDATE ON `dish`
    -> FOR EACH ROW
    -> BEGIN
    ->     IF NEW.stock <= 0 THEN
    ->         SET NEW.stock_status = 0;
    ->     ELSEIF NEW.stock <= 10 THEN
    ->         SET NEW.stock_status = 2;
    ->     ELSE
    ->         SET NEW.stock_status = 1;
    ->     END IF;
    -> END//
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
mysql>
mysql> -- 套餐库存状态自动更新
mysql> DELIMITER //
mysql> CREATE TRIGGER `trg_setmeal_stock_status`
    -> BEFORE UPDATE ON `setmeal`
    -> FOR EACH ROW
    -> BEGIN
    ->     IF NEW.stock <= 0 THEN
    ->         SET NEW.stock_status = 0;
    ->     ELSEIF NEW.stock <= 10 THEN
    ->         SET NEW.stock_status = 2;
    ->     ELSE
    ->         SET NEW.stock_status = 1;
    ->     END IF;
    -> END//
Query OK, 0 rows affected (0.01 sec)

mysql> DELIMITER ;
mysql>
mysql> -- ============================================
mysql> -- 查询验证
mysql> -- ============================================
mysql>
mysql> -- 查看菜品表结构
mysql> DESC `dish`;
+--------------+---------------+------+-----+-------------------+-----------------------------------------------+
| Field        | Type          | Null | Key | Default           | Extra                                         |
+--------------+---------------+------+-----+-------------------+-----------------------------------------------+
| id           | bigint        | NO   | PRI | NULL              | auto_increment                                |
| name         | varchar(50)   | NO   | MUL | NULL              |                                               |
| category_id  | bigint        | YES  | MUL | NULL              |                                               |
| price        | decimal(10,2) | NO   |     | NULL              |                                               |
| description  | varchar(255)  | YES  |     | NULL              |                                               |
| image        | varchar(255)  | YES  |     | NULL              |                                               |
| status       | tinyint       | YES  | MUL | 1                 |                                               |
| create_time  | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_time  | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
| create_user  | bigint        | YES  |     | NULL              |                                               |
| update_user  | bigint        | YES  |     | NULL              |                                               |
| store_id     | bigint        | YES  | MUL | NULL              |                                               |
| store_name   | varchar(255)  | YES  |     | NULL              |                                               |
| stock        | int           | YES  |     | 999               |                                               |
| stock_status | tinyint       | YES  |     | 1                 |                                               |
+--------------+---------------+------+-----+-------------------+-----------------------------------------------+
15 rows in set (0.00 sec)

mysql>
mysql> -- 查看套餐表结构
mysql> DESC `setmeal`;
+--------------+---------------+------+-----+-------------------+-----------------------------------------------+
| Field        | Type          | Null | Key | Default           | Extra                                         |
+--------------+---------------+------+-----+-------------------+-----------------------------------------------+
| id           | bigint        | NO   | PRI | NULL              | auto_increment                                |
| category_id  | varchar(50)   | YES  | MUL | NULL              |                                               |
| name         | varchar(50)   | NO   | MUL | NULL              |                                               |
| price        | decimal(10,2) | NO   |     | NULL              |                                               |
| status       | tinyint       | YES  | MUL | 1                 |                                               |
| description  | varchar(255)  | YES  |     | NULL              |                                               |
| image        | varchar(255)  | YES  |     | NULL              |                                               |
| create_time  | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_time  | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
| create_user  | bigint        | YES  |     | NULL              |                                               |
| update_user  | bigint        | YES  |     | NULL              |                                               |
| store_id     | bigint        | YES  | MUL | NULL              |                                               |
| store_name   | varchar(255)  | YES  |     | NULL              |                                               |
| stock        | int           | YES  |     | 999               |                                               |
| stock_status | tinyint       | YES  |     | 1                 |                                               |
+--------------+---------------+------+-----+-------------------+-----------------------------------------------+
15 rows in set (0.00 sec)

mysql>
mysql> -- 查看库存日志表结构
mysql> DESC `stock_log`;
+--------------+--------------+------+-----+-------------------+-------------------+
| Field        | Type         | Null | Key | Default           | Extra             |
+--------------+--------------+------+-----+-------------------+-------------------+
| id           | bigint       | NO   | PRI | NULL              | auto_increment    |
| type         | tinyint      | NO   |     | NULL              |                   |
| dish_id      | bigint       | YES  | MUL | NULL              |                   |
| setmeal_id   | bigint       | YES  | MUL | NULL              |                   |
| operation    | tinyint      | NO   |     | NULL              |                   |
| quantity     | int          | NO   |     | NULL              |                   |
| before_stock | int          | NO   |     | NULL              |                   |
| after_stock  | int          | NO   |     | NULL              |                   |
| order_id     | bigint       | YES  | MUL | NULL              |                   |
| remark       | varchar(255) | YES  |     | NULL              |                   |
| create_time  | datetime     | YES  | MUL | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| create_user  | bigint       | YES  |     | NULL              |                   |
+--------------+--------------+------+-----+-------------------+-------------------+
12 rows in set (0.00 sec)

mysql>
mysql> -- 查看当前菜品库存情况
mysql> SELECT `id`, `name`, `stock`, `stock_status` FROM `dish` LIMIT 10;
+----+----------------+-------+--------------+
| id | name           | stock | stock_status |
+----+----------------+-------+--------------+
|  1 | 香辣鸡腿堡     |   999 |            1 |
|  2 | 奥尔良烤鸡腿堡 |   999 |            1 |
|  3 | 原味鸡         |   999 |            1 |
|  4 | 香辣鸡翅       |   999 |            1 |
|  5 | 薯条（大）     |   999 |            1 |
|  6 | 蛋挞           |   999 |            1 |
|  7 | 可乐           |   999 |            1 |
|  8 | 雪碧           |   999 |            1 |
|  9 | 巨无霸         |   999 |            1 |
| 10 | 麦辣鸡腿堡     |   999 |            1 |
+----+----------------+-------+--------------+
10 rows in set (0.00 sec)

mysql>
mysql> -- 查看当前套餐库存情况
mysql> SELECT `id`, `name`, `stock`, `stock_status` FROM `setmeal` LIMIT 10;
+-----+--------------------------+-------+--------------+
| id  | name                     | stock | stock_status |
+-----+--------------------------+-------+--------------+
| 164 | 两个原味鸡               |   999 |            1 |
| 165 | 1+1随心选                |   999 |            1 |
| 166 | 套餐                     |   999 |            1 |
| 168 | 联动                     |   999 |            1 |
| 169 | 无上至尊豪华皇帝披萨套餐 |   999 |            1 |
+-----+--------------------------+-------+--------------+
5 rows in set (0.00 sec)

mysql>
mysql>