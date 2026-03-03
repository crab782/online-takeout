-- ============================================
-- 库存操作示例SQL
-- ============================================

-- ============================================
-- 1. 下单时扣除库存（需要在事务中执行）
-- ============================================

-- 示例：扣除菜品库存
-- 注意：需要在事务中执行，确保原子性
START TRANSACTION;

-- 1.1 检查并扣除菜品库存
-- 假设要扣除菜品ID为1的库存5份
SET @dish_id = 1;
SET @deduct_quantity = 5;
SET @order_id = 10001;

-- 检查库存是否充足
SELECT `stock` INTO @current_stock FROM `dish` WHERE `id` = @dish_id FOR UPDATE;

-- 如果库存充足，执行扣除
IF @current_stock >= @deduct_quantity THEN
    -- 扣除库存
    UPDATE `dish` 
    SET `stock` = `stock` - @deduct_quantity 
    WHERE `id` = @dish_id AND `stock` >= @deduct_quantity;
    
    -- 记录库存日志
    INSERT INTO `stock_log` (`type`, `dish_id`, `operation`, `quantity`, `before_stock`, `after_stock`, `order_id`, `remark`)
    VALUES (1, @dish_id, 2, -@deduct_quantity, @current_stock, @current_stock - @deduct_quantity, @order_id, '订单出库');
    
    SELECT '库存扣除成功' AS result;
ELSE
    SELECT '库存不足' AS result;
END IF;

COMMIT;


-- 示例：扣除套餐库存
START TRANSACTION;

-- 假设要扣除套餐ID为1的库存2份
SET @setmeal_id = 1;
SET @deduct_quantity = 2;
SET @order_id = 10001;

-- 检查库存是否充足
SELECT `stock` INTO @current_stock FROM `setmeal` WHERE `id` = @setmeal_id FOR UPDATE;

-- 如果库存充足，执行扣除
IF @current_stock >= @deduct_quantity THEN
    -- 扣除库存
    UPDATE `setmeal` 
    SET `stock` = `stock` - @deduct_quantity 
    WHERE `id` = @setmeal_id AND `stock` >= @deduct_quantity;
    
    -- 记录库存日志
    INSERT INTO `stock_log` (`type`, `setmeal_id`, `operation`, `quantity`, `before_stock`, `after_stock`, `order_id`, `remark`)
    VALUES (2, @setmeal_id, 2, -@deduct_quantity, @current_stock, @current_stock - @deduct_quantity, @order_id, '订单出库');
    
    SELECT '套餐库存扣除成功' AS result;
ELSE
    SELECT '套餐库存不足' AS result;
END IF;

COMMIT;


-- ============================================
-- 2. 手动调整库存（商家后台使用）
-- ============================================

-- 增加菜品库存
SET @dish_id = 1;
SET @adjust_quantity = 100;
SET @user_id = 1;

START TRANSACTION;

SELECT `stock` INTO @current_stock FROM `dish` WHERE `id` = @dish_id FOR UPDATE;

UPDATE `dish` SET `stock` = `stock` + @adjust_quantity WHERE `id` = @dish_id;

INSERT INTO `stock_log` (`type`, `dish_id`, `operation`, `quantity`, `before_stock`, `after_stock`, `remark`, `create_user`)
VALUES (1, @dish_id, 3, @adjust_quantity, @current_stock, @current_stock + @adjust_quantity, '手动入库', @user_id);

COMMIT;


-- 增加套餐库存
SET @setmeal_id = 1;
SET @adjust_quantity = 50;
SET @user_id = 1;

START TRANSACTION;

SELECT `stock` INTO @current_stock FROM `setmeal` WHERE `id` = @setmeal_id FOR UPDATE;

UPDATE `setmeal` SET `stock` = `stock` + @adjust_quantity WHERE `id` = @setmeal_id;

INSERT INTO `stock_log` (`type`, `setmeal_id`, `operation`, `quantity`, `before_stock`, `after_stock`, `remark`, `create_user`)
VALUES (2, @setmeal_id, 3, @adjust_quantity, @current_stock, @current_stock + @adjust_quantity, '手动入库', @user_id);

COMMIT;


-- ============================================
-- 3. 库存查询相关SQL
-- ============================================

-- 查询库存紧张的菜品（库存<=10）
SELECT `id`, `name`, `stock`, `stock_status`, `store_name`
FROM `dish`
WHERE `stock` <= 10 AND `status` = 1
ORDER BY `stock` ASC;

-- 查询已售罄的菜品
SELECT `id`, `name`, `stock`, `store_name`
FROM `dish`
WHERE `stock` <= 0 AND `status` = 1;

-- 查询库存紧张的套餐（库存<=10）
SELECT `id`, `name`, `stock`, `stock_status`, `store_name`
FROM `setmeal`
WHERE `stock` <= 10 AND `status` = 1
ORDER BY `stock` ASC;

-- 查询已售罄的套餐
SELECT `id`, `name`, `stock`, `store_name`
FROM `setmeal`
WHERE `stock` <= 0 AND `status` = 1;

-- 查询某段时间内的库存变动记录
SELECT 
    sl.*,
    CASE 
        WHEN sl.type = 1 THEN d.name 
        WHEN sl.type = 2 THEN s.name 
    END AS item_name,
    CASE 
        WHEN sl.operation = 1 THEN '入库'
        WHEN sl.operation = 2 THEN '出库'
        WHEN sl.operation = 3 THEN '手动调整'
    END AS operation_name
FROM `stock_log` sl
LEFT JOIN `dish` d ON sl.dish_id = d.id
LEFT JOIN `setmeal` s ON sl.setmeal_id = s.id
WHERE sl.create_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)
ORDER BY sl.create_time DESC;


-- ============================================
-- 4. 库存统计报表
-- ============================================

-- 按店铺统计菜品库存情况
SELECT 
    `store_id`,
    `store_name`,
    COUNT(*) AS total_dishes,
    SUM(CASE WHEN `stock` <= 0 THEN 1 ELSE 0 END) AS sold_out_count,
    SUM(CASE WHEN `stock` > 0 AND `stock` <= 10 THEN 1 ELSE 0 END) AS low_stock_count,
    SUM(CASE WHEN `stock` > 10 THEN 1 ELSE 0 END) AS normal_stock_count
FROM `dish`
WHERE `status` = 1
GROUP BY `store_id`, `store_name`;

-- 按店铺统计套餐库存情况
SELECT 
    `store_id`,
    `store_name`,
    COUNT(*) AS total_setmeals,
    SUM(CASE WHEN `stock` <= 0 THEN 1 ELSE 0 END) AS sold_out_count,
    SUM(CASE WHEN `stock` > 0 AND `stock` <= 10 THEN 1 ELSE 0 END) AS low_stock_count,
    SUM(CASE WHEN `stock` > 10 THEN 1 ELSE 0 END) AS normal_stock_count
FROM `setmeal`
WHERE `status` = 1
GROUP BY `store_id`, `store_name`;
