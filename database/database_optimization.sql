-- ========================================
-- 数据库结构优化SQL脚本
-- 功能：添加唯一性约束和store_id字段
-- 数据库：takeout_system
-- 执行日期：2026-02-20
-- ========================================

-- ========================================
-- 第一部分：数据库备份
-- ========================================

-- 1. 创建备份目录（在MySQL命令行外执行）
-- mkdir -p /path/to/backup

-- 2. 备份整个数据库（在MySQL命令行外执行）
-- mysqldump -u root -p takeout_system > /path/to/backup/takeout_system_backup_20260220.sql

-- 3. 备份特定表（在MySQL命令行外执行）
-- mysqldump -u root -p takeout_system category > /path/to/backup/category_backup.sql
-- mysqldump -u root -p takeout_system dish > /path/to/backup/dish_backup.sql
-- mysqldump -u root -p takeout_system setmeal > /path/to/backup/setmeal_backup.sql
-- mysqldump -u root -p takeout_system employee > /path/to/backup/employee_backup.sql
-- mysqldump -u root -p takeout_system shopping_cart > /path/to/backup/shopping_cart_backup.sql
-- mysqldump -u root -p takeout_system comment > /path/to/backup/comment_backup.sql
-- mysqldump -u root -p takeout_system user_checkin > /path/to/backup/user_checkin_backup.sql
-- mysqldump -u root -p takeout_system store_favorite > /path/to/backup/store_favorite_backup.sql
-- mysqldump -u root -p takeout_system activity > /path/to/backup/activity_backup.sql
-- mysqldump -u root -p takeout_system address_book > /path/to/backup/address_book_backup.sql

-- ========================================
-- 第二部分：检查现有数据重复情况
-- ========================================

USE takeout_system;

-- 1. 检查category表重复数据
SELECT 
    store_id, 
    name, 
    COUNT(*) as duplicate_count 
FROM category 
WHERE store_id IS NOT NULL AND name IS NOT NULL
GROUP BY store_id, name 
HAVING COUNT(*) > 1;

-- 2. 检查dish表重复数据
SELECT 
    store_id, 
    name, 
    COUNT(*) as duplicate_count 
FROM dish 
WHERE store_id IS NOT NULL AND name IS NOT NULL
GROUP BY store_id, name 
HAVING COUNT(*) > 1;

-- 3. 检查setmeal表重复数据
SELECT 
    store_id, 
    name, 
    COUNT(*) as duplicate_count 
FROM setmeal 
WHERE store_id IS NOT NULL AND name IS NOT NULL
GROUP BY store_id, name 
HAVING COUNT(*) > 1;

-- 4. 检查employee表重复数据
SELECT 
    store_id, 
    username, 
    COUNT(*) as duplicate_count 
FROM employee 
WHERE store_id IS NOT NULL AND username IS NOT NULL
GROUP BY store_id, username 
HAVING COUNT(*) > 1;

-- 5. 检查shopping_cart表重复数据
SELECT 
    user_id, 
    dish_id, 
    setmeal_id, 
    dish_flavor, 
    COUNT(*) as duplicate_count 
FROM shopping_cart 
GROUP BY user_id, dish_id, setmeal_id, dish_flavor 
HAVING COUNT(*) > 1;

-- 6. 检查comment表重复数据
SELECT 
    order_id, 
    COUNT(*) as duplicate_count 
FROM comment 
WHERE order_id IS NOT NULL
GROUP BY order_id 
HAVING COUNT(*) > 1;

-- 7. 检查user_checkin表重复数据
SELECT 
    user_id, 
    DATE(create_time) as checkin_date, 
    COUNT(*) as duplicate_count 
FROM user_checkin 
WHERE user_id IS NOT NULL AND create_time IS NOT NULL
GROUP BY user_id, DATE(create_time) 
HAVING COUNT(*) > 1;

-- 8. 检查store_favorite表重复数据
SELECT 
    user_id, 
    store_id, 
    COUNT(*) as duplicate_count 
FROM store_favorite 
WHERE user_id IS NOT NULL AND store_id IS NOT NULL
GROUP BY user_id, store_id 
HAVING COUNT(*) > 1;

-- ========================================
-- 第三部分：清理重复数据（如果存在）
-- ========================================

-- 注意：如果上述查询发现有重复数据，需要先清理重复数据
-- 以下SQL会保留每组重复数据中ID最小的记录，删除其他记录

-- 1. 清理category表重复数据
DELETE c1 FROM category c1
INNER JOIN (
    SELECT store_id, name, MIN(id) as min_id
    FROM category
    WHERE store_id IS NOT NULL AND name IS NOT NULL
    GROUP BY store_id, name
    HAVING COUNT(*) > 1
) c2 ON c1.store_id = c2.store_id AND c1.name = c2.name AND c1.id > c2.min_id;

-- 2. 清理dish表重复数据
DELETE d1 FROM dish d1
INNER JOIN (
    SELECT store_id, name, MIN(id) as min_id
    FROM dish
    WHERE store_id IS NOT NULL AND name IS NOT NULL
    GROUP BY store_id, name
    HAVING COUNT(*) > 1
) d2 ON d1.store_id = d2.store_id AND d1.name = d2.name AND d1.id > d2.min_id;

-- 3. 清理setmeal表重复数据
DELETE s1 FROM setmeal s1
INNER JOIN (
    SELECT store_id, name, MIN(id) as min_id
    FROM setmeal
    WHERE store_id IS NOT NULL AND name IS NOT NULL
    GROUP BY store_id, name
    HAVING COUNT(*) > 1
) s2 ON s1.store_id = s2.store_id AND s1.name = s2.name AND s1.id > s2.min_id;

-- 4. 清理employee表重复数据
DELETE e1 FROM employee e1
INNER JOIN (
    SELECT store_id, username, MIN(id) as min_id
    FROM employee
    WHERE store_id IS NOT NULL AND username IS NOT NULL
    GROUP BY store_id, username
    HAVING COUNT(*) > 1
) e2 ON e1.store_id = e2.store_id AND e1.username = e2.username AND e1.id > e2.min_id;

-- 5. 清理shopping_cart表重复数据
DELETE sc1 FROM shopping_cart sc1
INNER JOIN (
    SELECT user_id, dish_id, setmeal_id, dish_flavor, MIN(id) as min_id
    FROM shopping_cart
    GROUP BY user_id, dish_id, setmeal_id, dish_flavor
    HAVING COUNT(*) > 1
) sc2 ON sc1.user_id = sc2.user_id 
    AND sc1.dish_id = sc2.dish_id 
    AND sc1.setmeal_id = sc2.setmeal_id 
    AND sc1.dish_flavor = sc2.dish_flavor 
    AND sc1.id > sc2.min_id;

-- 6. 清理comment表重复数据
DELETE c1 FROM comment c1
INNER JOIN (
    SELECT order_id, MIN(id) as min_id
    FROM comment
    WHERE order_id IS NOT NULL
    GROUP BY order_id
    HAVING COUNT(*) > 1
) c2 ON c1.order_id = c2.order_id AND c1.id > c2.min_id;

-- 7. 清理user_checkin表重复数据
DELETE uc1 FROM user_checkin uc1
INNER JOIN (
    SELECT user_id, DATE(create_time) as checkin_date, MIN(id) as min_id
    FROM user_checkin
    WHERE user_id IS NOT NULL AND create_time IS NOT NULL
    GROUP BY user_id, DATE(create_time)
    HAVING COUNT(*) > 1
) uc2 ON uc1.user_id = uc2.user_id 
    AND DATE(uc1.create_time) = uc2.checkin_date 
    AND uc1.id > uc2.min_id;

-- 8. 清理store_favorite表重复数据
DELETE sf1 FROM store_favorite sf1
INNER JOIN (
    SELECT user_id, store_id, MIN(id) as min_id
    FROM store_favorite
    WHERE user_id IS NOT NULL AND store_id IS NOT NULL
    GROUP BY user_id, store_id
    HAVING COUNT(*) > 1
) sf2 ON sf1.user_id = sf2.user_id 
    AND sf1.store_id = sf2.store_id 
    AND sf1.id > sf2.min_id;

-- ========================================
-- 第四部分：添加store_id字段
-- ========================================

-- 1. 为activity表添加store_id字段
ALTER TABLE activity 
ADD COLUMN store_id BIGINT AFTER status,
ADD INDEX idx_store_id (store_id);

-- 为activity表的现有数据填充store_id（默认值为1，可根据实际情况修改）
UPDATE activity SET store_id = 1 WHERE store_id IS NULL;

-- 2. 为address_book表添加store_id字段
ALTER TABLE address_book 
ADD COLUMN store_id BIGINT AFTER user_id,
ADD INDEX idx_store_id (store_id);

-- 为address_book表的现有数据填充store_id（默认值为1，可根据实际情况修改）
UPDATE address_book SET store_id = 1 WHERE store_id IS NULL;

-- 3. 为shopping_cart表添加store_id字段
ALTER TABLE shopping_cart 
ADD COLUMN store_id BIGINT AFTER user_id,
ADD INDEX idx_store_id (store_id);

-- 为shopping_cart表的现有数据填充store_id（默认值为1，可根据实际情况修改）
UPDATE shopping_cart SET store_id = 1 WHERE store_id IS NULL;

-- ========================================
-- 第五部分：添加唯一性约束
-- ========================================

-- 1. 为category表添加唯一约束
ALTER TABLE category 
ADD UNIQUE KEY uk_category_name_store (name, store_id);

-- 2. 为dish表添加唯一约束
ALTER TABLE dish 
ADD UNIQUE KEY uk_dish_name_store (name, store_id);

-- 3. 为setmeal表添加唯一约束
ALTER TABLE setmeal 
ADD UNIQUE KEY uk_setmeal_name_store (name, store_id);

-- 4. 为employee表添加唯一约束
ALTER TABLE employee 
ADD UNIQUE KEY uk_employee_username_store (username, store_id);

-- 5. 为shopping_cart表添加唯一约束
ALTER TABLE shopping_cart 
ADD UNIQUE KEY uk_shopping_cart_user_dish (user_id, dish_id, setmeal_id, dish_flavor);

-- 6. 为comment表添加唯一约束
ALTER TABLE comment 
ADD UNIQUE KEY uk_comment_order (order_id);

-- 7. 为user_checkin表添加唯一约束
ALTER TABLE user_checkin 
ADD UNIQUE KEY uk_user_checkin_user_date (user_id, DATE(create_time));

-- 8. 为store_favorite表添加唯一约束
ALTER TABLE store_favorite 
ADD UNIQUE KEY uk_store_favorite_user_store (user_id, store_id);

-- ========================================
-- 第六部分：验证修改结果
-- ========================================

-- 1. 验证store_id字段是否添加成功
SELECT 
    TABLE_NAME, 
    COLUMN_NAME, 
    COLUMN_TYPE, 
    IS_NULLABLE, 
    COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND TABLE_NAME IN ('activity', 'address_book', 'shopping_cart')
    AND COLUMN_NAME = 'store_id';

-- 2. 验证唯一性约束是否添加成功
SELECT 
    TABLE_NAME, 
    CONSTRAINT_NAME, 
    CONSTRAINT_TYPE 
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND CONSTRAINT_TYPE = 'UNIQUE'
ORDER BY TABLE_NAME, CONSTRAINT_NAME;

-- 3. 验证索引是否添加成功
SELECT 
    TABLE_NAME, 
    INDEX_NAME, 
    COLUMN_NAME, 
    SEQ_IN_INDEX 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND TABLE_NAME IN ('activity', 'address_book', 'shopping_cart', 'category', 'dish', 'setmeal', 'employee', 'comment', 'user_checkin', 'store_favorite')
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

-- 4. 测试唯一性约束是否生效（插入重复数据应该失败）
-- 测试category表唯一约束
-- INSERT INTO category (name, type, sort, status, store_id) VALUES ('测试分类', 1, 1, 1, 1);
-- INSERT INTO category (name, type, sort, status, store_id) VALUES ('测试分类', 1, 2, 1, 1); -- 应该失败

-- 测试dish表唯一约束
-- INSERT INTO dish (name, category_id, price, status, store_id) VALUES ('测试菜品', 1, 10.00, 1, 1);
-- INSERT INTO dish (name, category_id, price, status, store_id) VALUES ('测试菜品', 1, 15.00, 1, 1); -- 应该失败

-- 测试comment表唯一约束
-- INSERT INTO comment (content, user_id, user_name, order_id, store_id, rating) VALUES ('测试评论', 1, '测试用户', 999, 1, 5);
-- INSERT INTO comment (content, user_id, user_name, order_id, store_id, rating) VALUES ('测试评论2', 1, '测试用户', 999, 1, 5); -- 应该失败

-- 5. 检查数据完整性
SELECT 
    'category' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM category
UNION ALL
SELECT 
    'dish' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM dish
UNION ALL
SELECT 
    'setmeal' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM setmeal
UNION ALL
SELECT 
    'employee' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM employee
UNION ALL
SELECT 
    'shopping_cart' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM shopping_cart
UNION ALL
SELECT 
    'activity' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM activity
UNION ALL
SELECT 
    'address_book' as table_name, 
    COUNT(*) as total_count, 
    COUNT(CASE WHEN store_id IS NULL THEN 1 END) as null_store_id_count
FROM address_book;

-- ========================================
-- 第七部分：回滚脚本（如果需要）
-- ========================================

-- 如果执行过程中出现问题，可以使用以下SQL回滚修改

-- 1. 删除唯一性约束
ALTER TABLE category DROP INDEX uk_category_name_store;
ALTER TABLE dish DROP INDEX uk_dish_name_store;
ALTER TABLE setmeal DROP INDEX uk_setmeal_name_store;
ALTER TABLE employee DROP INDEX uk_employee_username_store;
ALTER TABLE shopping_cart DROP INDEX uk_shopping_cart_user_dish;
ALTER TABLE comment DROP INDEX uk_comment_order;
ALTER TABLE user_checkin DROP INDEX uk_user_checkin_user_date;
ALTER TABLE store_favorite DROP INDEX uk_store_favorite_user_store;

-- 2. 删除store_id字段
ALTER TABLE activity DROP COLUMN store_id;
ALTER TABLE address_book DROP COLUMN store_id;
ALTER TABLE shopping_cart DROP COLUMN store_id;

-- 3. 恢复备份（在MySQL命令行外执行）
-- mysql -u root -p takeout_system < /path/to/backup/takeout_system_backup_20260220.sql

-- ========================================
-- 执行完成
-- ========================================