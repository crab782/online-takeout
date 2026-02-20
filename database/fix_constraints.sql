-- ========================================
-- 修复数据库约束问题
-- 执行日期：2026-02-20
-- ========================================

USE takeout_system;

-- ========================================
-- 问题1：删除store_favorite表重复的唯一性约束
-- ========================================

-- 删除重复的约束uk_user_store
ALTER TABLE store_favorite DROP INDEX uk_user_store;

-- 验证：检查约束是否删除成功
SELECT 
    TABLE_NAME, 
    CONSTRAINT_NAME, 
    CONSTRAINT_TYPE 
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND TABLE_NAME = 'store_favorite' 
    AND CONSTRAINT_TYPE = 'UNIQUE';

-- ========================================
-- 问题2：修复user_checkin表唯一性约束
-- ========================================

-- 步骤1：删除现有的约束
ALTER TABLE user_checkin DROP INDEX uk_user_date;

-- 步骤2：添加checkin_date字段
ALTER TABLE user_checkin ADD COLUMN checkin_date DATE AFTER create_time;

-- 步骤3：为现有数据填充checkin_date
UPDATE user_checkin SET checkin_date = DATE(create_time) WHERE checkin_date IS NULL;

-- 步骤4：添加正确的唯一性约束
ALTER TABLE user_checkin ADD UNIQUE KEY uk_user_checkin_user_date (user_id, checkin_date);

-- 验证：检查约束是否添加成功
SELECT 
    TABLE_NAME, 
    CONSTRAINT_NAME, 
    CONSTRAINT_TYPE 
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND TABLE_NAME = 'user_checkin' 
    AND CONSTRAINT_TYPE = 'UNIQUE';

-- ========================================
-- 问题3：优化shopping_cart表唯一性约束
-- ========================================

-- 方案1：添加store_id到唯一性约束（推荐）
-- 删除现有约束
ALTER TABLE shopping_cart DROP INDEX uk_shopping_cart_user_dish;

-- 添加包含store_id的约束
ALTER TABLE shopping_cart ADD UNIQUE KEY uk_shopping_cart_user_dish (user_id, dish_id, setmeal_id, dish_flavor, store_id);

-- 验证：检查约束是否添加成功
SELECT 
    TABLE_NAME, 
    CONSTRAINT_NAME, 
    CONSTRAINT_TYPE 
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND TABLE_NAME = 'shopping_cart' 
    AND CONSTRAINT_TYPE = 'UNIQUE';

-- ========================================
-- 验证所有修复结果
-- ========================================

-- 1. 检查所有唯一性约束
SELECT 
    TABLE_NAME, 
    CONSTRAINT_NAME, 
    CONSTRAINT_TYPE 
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND CONSTRAINT_TYPE = 'UNIQUE'
ORDER BY TABLE_NAME, CONSTRAINT_NAME;

-- 2. 检查所有索引
SELECT 
    TABLE_NAME, 
    INDEX_NAME, 
    COLUMN_NAME, 
    SEQ_IN_INDEX 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'takeout_system' 
    AND TABLE_NAME IN ('activity', 'address_book', 'shopping_cart', 'category', 'dish', 'setmeal', 'employee', 'comment', 'user_checkin', 'store_favorite')
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

-- 3. 测试唯一性约束
-- 测试store_favorite表唯一性约束
-- INSERT INTO store_favorite (user_id, store_id) VALUES (999, 1);
-- INSERT INTO store_favorite (user_id, store_id) VALUES (999, 1); -- 应该失败

-- 测试user_checkin表唯一性约束
-- INSERT INTO user_checkin (user_id, checkin_date) VALUES (999, '2026-02-20');
-- INSERT INTO user_checkin (user_id, checkin_date) VALUES (999, '2026-02-20'); -- 应该失败

-- 测试shopping_cart表唯一性约束
-- INSERT INTO shopping_cart (user_id, dish_id, setmeal_id, store_id, number, amount) VALUES (999, 1, NULL, 1, 1, 10.00);
-- INSERT INTO shopping_cart (user_id, dish_id, setmeal_id, store_id, number, amount) VALUES (999, 1, NULL, 1, 1, 10.00); -- 应该失败

-- ========================================
-- 执行完成
-- ========================================