-- 更新菜品表中的店铺信息
UPDATE dish d
SET d.store_id = (
    SELECT s.id FROM store s
    WHERE s.id = 1
    LIMIT 1
),
d.store_name = (
    SELECT s.name FROM store s
    WHERE s.id = 1
    LIMIT 1
)
WHERE d.store_id IS NULL;

-- 更新套餐表中的店铺信息
UPDATE setmeal sm
SET sm.store_name = (
    SELECT s.name FROM store s
    WHERE s.id = 1
    LIMIT 1
)
WHERE sm.store_name IS NULL;

-- 更新购物车表中的店铺信息
UPDATE shopping_cart sc
SET sc.store_id = (
    SELECT d.store_id FROM dish d
    WHERE d.id = sc.dish_id
    LIMIT 1
),
sc.store_name = (
    SELECT d.store_name FROM dish d
    WHERE d.id = sc.dish_id
    LIMIT 1
)
WHERE sc.dish_id IS NOT NULL AND sc.store_id IS NULL;

UPDATE shopping_cart sc
SET sc.store_id = (
    SELECT sm.store_id FROM setmeal sm
    WHERE sm.id = sc.setmeal_id
    LIMIT 1
),
sc.store_name = (
    SELECT sm.store_name FROM setmeal sm
    WHERE sm.id = sc.setmeal_id
    LIMIT 1
)
WHERE sc.setmeal_id IS NOT NULL AND sc.store_id IS NULL;


--结果
mysql> -- 更新菜品表中的店铺信息
mysql> UPDATE dish d
    -> SET d.store_id = (
    ->     SELECT s.id FROM store s
    ->     WHERE s.id = 1
    ->     LIMIT 1
    -> ),
    -> d.store_name = (
    ->     SELECT s.name FROM store s
    ->     WHERE s.id = 1
    ->     LIMIT 1
    -> )
    -> WHERE d.store_id IS NULL;
Query OK, 0 rows affected (0.02 sec)
Rows matched: 0  Changed: 0  Warnings: 0

mysql>
mysql> -- 更新套餐表中的店铺信息
mysql> UPDATE setmeal sm
    -> SET sm.store_name = (
    ->     SELECT s.name FROM store s
    ->     WHERE s.id = 1
    ->     LIMIT 1
    -> )
    -> WHERE sm.store_name IS NULL;
Query OK, 17 rows affected (0.01 sec)
Rows matched: 17  Changed: 17  Warnings: 0

mysql>
mysql> -- 更新购物车表中的店铺信息
mysql> UPDATE shopping_cart sc
    -> SET sc.store_id = (
    ->     SELECT d.store_id FROM dish d
    ->     WHERE d.id = sc.dish_id
    ->     LIMIT 1
    -> ),
    -> sc.store_name = (
    ->     SELECT d.store_name FROM dish d
    ->     WHERE d.id = sc.dish_id
    ->     LIMIT 1
    -> )
    -> WHERE sc.dish_id IS NOT NULL AND sc.store_id IS NULL;
Query OK, 0 rows affected (0.00 sec)
Rows matched: 0  Changed: 0  Warnings: 0

mysql>
mysql> UPDATE shopping_cart sc
    -> SET sc.store_id = (
    ->     SELECT sm.store_id FROM setmeal sm
    ->     WHERE sm.id = sc.setmeal_id
    ->     LIMIT 1
    -> ),
    -> sc.store_name = (
    ->     SELECT sm.store_name FROM setmeal sm
    ->     WHERE sm.id = sc.setmeal_id
    ->     LIMIT 1
    -> )
    -> WHERE sc.setmeal_id IS NOT NULL AND sc.store_id IS NULL;
Query OK, 0 rows affected (0.00 sec)
Rows matched: 0  Changed: 0  Warnings: 0

mysql>
mysql>