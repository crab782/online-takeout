-- 检查购物车表结构
DESCRIBE shopping_cart;

-- 检查菜品表结构
DESCRIBE dish;

-- 检查套餐表结构
DESCRIBE setmeal;

-- 检查订单详情表结构
DESCRIBE order_detail;

-- 检查订单表结构
DESCRIBE orders;


--结果
mysql> use takeout_system
Database changed
mysql> DESCRIBE shopping_cart;
+-------------+---------------+------+-----+-------------------+-------------------+
| Field       | Type          | Null | Key | Default           | Extra             |
+-------------+---------------+------+-----+-------------------+-------------------+
| id          | bigint        | NO   | PRI | NULL              | auto_increment    |
| name        | varchar(50)   | YES  |     | NULL              |                   |
| image       | varchar(255)  | YES  |     | NULL              |                   |
| user_id     | bigint        | NO   | MUL | NULL              |                   |
| store_id    | bigint        | YES  | MUL | NULL              |                   |
| dish_id     | bigint        | YES  | MUL | NULL              |                   |
| setmeal_id  | bigint        | YES  | MUL | NULL              |                   |
| dish_flavor | varchar(50)   | YES  |     | NULL              |                   |
| number      | int           | NO   |     | NULL              |                   |
| amount      | decimal(10,2) | NO   |     | NULL              |                   |
| create_time | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+-------------+---------------+------+-----+-------------------+-------------------+
11 rows in set (0.01 sec)

mysql>
mysql> -- 检查菜品表结构
mysql> DESCRIBE dish;
+-------------+---------------+------+-----+-------------------+-----------------------------------------------+
| Field       | Type          | Null | Key | Default           | Extra                                         |
+-------------+---------------+------+-----+-------------------+-----------------------------------------------+
| id          | bigint        | NO   | PRI | NULL              | auto_increment                                |
| name        | varchar(50)   | NO   | MUL | NULL              |                                               |
| category_id | bigint        | YES  | MUL | NULL              |                                               |
| price       | decimal(10,2) | NO   |     | NULL              |                                               |
| description | varchar(255)  | YES  |     | NULL              |                                               |
| image       | varchar(255)  | YES  |     | NULL              |                                               |
| status      | tinyint       | YES  | MUL | 1                 |                                               |
| create_time | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_time | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
| create_user | bigint        | YES  |     | NULL              |                                               |
| update_user | bigint        | YES  |     | NULL              |                                               |
| store_id    | bigint        | YES  | MUL | NULL              |                                               |
+-------------+---------------+------+-----+-------------------+-----------------------------------------------+
12 rows in set (0.00 sec)

mysql>
mysql> -- 检查套餐表结构
mysql> DESCRIBE setmeal;
+-------------+---------------+------+-----+-------------------+-----------------------------------------------+
| Field       | Type          | Null | Key | Default           | Extra                                         |
+-------------+---------------+------+-----+-------------------+-----------------------------------------------+
| id          | bigint        | NO   | PRI | NULL              | auto_increment                                |
| category_id | varchar(50)   | YES  | MUL | NULL              |                                               |
| name        | varchar(50)   | NO   | MUL | NULL              |                                               |
| price       | decimal(10,2) | NO   |     | NULL              |                                               |
| status      | tinyint       | YES  | MUL | 1                 |                                               |
| description | varchar(255)  | YES  |     | NULL              |                                               |
| image       | varchar(255)  | YES  |     | NULL              |                                               |
| create_time | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_time | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
| create_user | bigint        | YES  |     | NULL              |                                               |
| update_user | bigint        | YES  |     | NULL              |                                               |
| store_id    | bigint        | YES  | MUL | NULL              |                                               |
+-------------+---------------+------+-----+-------------------+-----------------------------------------------+
12 rows in set (0.00 sec)

mysql>
mysql> -- 检查订单详情表结构
mysql> DESCRIBE order_detail;
+------------+---------------+------+-----+---------+----------------+
| Field      | Type          | Null | Key | Default | Extra          |
+------------+---------------+------+-----+---------+----------------+
| id         | bigint        | NO   | PRI | NULL    | auto_increment |
| name       | varchar(50)   | YES  |     | NULL    |                |
| order_id   | bigint        | NO   | MUL | NULL    |                |
| dish_id    | bigint        | YES  | MUL | NULL    |                |
| setmeal_id | bigint        | YES  | MUL | NULL    |                |
| number     | int           | YES  |     | NULL    |                |
| amount     | decimal(10,2) | YES  |     | NULL    |                |
+------------+---------------+------+-----+---------+----------------+
7 rows in set (0.00 sec)

mysql>
mysql> -- 检查订单表结构
mysql> DESCRIBE orders;
+-----------------+---------------+------+-----+-------------------+-----------------------------------------------+
| Field           | Type          | Null | Key | Default           | Extra                                         |
+-----------------+---------------+------+-----+-------------------+-----------------------------------------------+
| id              | bigint        | NO   | PRI | NULL              | auto_increment                                |
| number          | varchar(50)   | NO   | UNI | NULL              |                                               |
| status          | tinyint       | YES  | MUL | 0                 |                                               |
| user_id         | bigint        | NO   | MUL | NULL              |                                               |
| user_name       | varchar(50)   | YES  |     | NULL              |                                               |
| store_id        | bigint        | NO   | MUL | NULL              |                                               |
| address_book_id | bigint        | YES  |     | NULL              |                                               |
| order_time      | datetime      | YES  |     | NULL              |                                               |
| checkout_time   | datetime      | YES  |     | NULL              |                                               |
| pay_time        | datetime      | YES  |     | NULL              |                                               |
| store_name      | varchar(100)  | YES  |     | NULL              |                                               |
| amount          | decimal(10,2) | NO   |     | NULL              |                                               |
| pay_method      | tinyint       | YES  |     | NULL              |                                               |
| pay_status      | tinyint       | YES  |     | 0                 |                                               |
| receiver        | varchar(50)   | YES  |     | NULL              |                                               |
| address         | varchar(255)  | YES  |     | NULL              |                                               |
| phone           | varchar(20)   | YES  |     | NULL              |                                               |
| remark          | varchar(255)  | YES  |     | NULL              |                                               |
| create_time     | datetime      | YES  | MUL | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_time     | datetime      | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
+-----------------+---------------+------+-----+-------------------+-----------------------------------------------+
20 rows in set (0.00 sec)

mysql>