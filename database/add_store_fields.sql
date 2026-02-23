-- 为购物车表添加店铺相关字段
ALTER TABLE shopping_cart ADD COLUMN store_id BIGINT COMMENT '店铺ID' AFTER user_id;
ALTER TABLE shopping_cart ADD COLUMN store_name VARCHAR(255) COMMENT '店铺名称' AFTER store_id;

-- 为菜品表添加店铺相关字段
ALTER TABLE dish ADD COLUMN store_id BIGINT COMMENT '店铺ID' AFTER category_id;
ALTER TABLE dish ADD COLUMN store_name VARCHAR(255) COMMENT '店铺名称' AFTER store_id;

-- 为套餐表添加店铺名称字段
ALTER TABLE setmeal ADD COLUMN store_name VARCHAR(255) COMMENT '店铺名称' AFTER store_id;

-- 为订单详情表添加图片字段
ALTER TABLE order_detail ADD COLUMN image VARCHAR(255) COMMENT '菜品图片' AFTER amount;



--结果
mysql> ALTER TABLE shopping_cart ADD COLUMN store_id BIGINT COMMENT '店铺ID' AFTER user_id;
ERROR 1060 (42S21): Duplicate column name 'store_id'
mysql> ALTER TABLE shopping_cart ADD COLUMN store_name VARCHAR(255) COMMENT '店铺名称' AFTER store_id;
Query OK, 0 rows affected (0.25 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> ALTER TABLE dish ADD COLUMN store_id BIGINT COMMENT '店铺ID' AFTER category_id;
ERROR 1060 (42S21): Duplicate column name 'store_id'
mysql> ALTER TABLE dish ADD COLUMN store_name VARCHAR(255) COMMENT '店铺名称' AFTER store_id;
Query OK, 0 rows affected (0.08 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> -- 为套餐表添加店铺名称字段
mysql> ALTER TABLE setmeal ADD COLUMN store_name VARCHAR(255) COMMENT '店铺名称' AFTER store_id;
Query OK, 0 rows affected (0.06 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql>
mysql> -- 为订单详情表添加图片字段
mysql> ALTER TABLE order_detail ADD COLUMN image VARCHAR(255) COMMENT '菜品图片' AFTER amount;
Query OK, 0 rows affected (0.04 sec)
Records: 0  Duplicates: 0  Warnings: 0