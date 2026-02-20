-- 为shop_admin5（喜茶）添加测试数据
-- 创建日期：2026-02-20

SET FOREIGN_KEY_CHECKS = 0;

USE `takeout_system`;

-- ============================================
-- 1. 为喜茶（店铺ID:5）添加更多菜品数据
-- ============================================
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`, `store_id`, `create_time`, `update_time`) VALUES
-- 奶茶分类（category_id:21）
('芝芝芒芒', 21, 29.00, '芝芝芒芒，新鲜芒果与芝士的完美结合', 'https://example.com/ht/milktea3.jpg', 1, 5, NOW(), NOW()),
('芝芝桃桃', 21, 28.00, '芝芝桃桃，新鲜桃子与芝士的完美结合', 'https://example.com/ht/milktea4.jpg', 1, 5, NOW(), NOW()),
('黑糖珍珠奶茶', 21, 24.00, '黑糖珍珠奶茶，经典台式风味', 'https://example.com/ht/milktea5.jpg', 1, 5, NOW(), NOW()),
('抹茶奶茶', 21, 26.00, '抹茶奶茶，日式抹茶风味', 'https://example.com/ht/milktea6.jpg', 1, 5, NOW(), NOW()),
('焦糖奶茶', 21, 24.00, '焦糖奶茶，香甜可口', 'https://example.com/ht/milktea7.jpg', 1, 5, NOW(), NOW()),
('燕麦奶茶', 21, 25.00, '燕麦奶茶，健康营养', 'https://example.com/ht/milktea8.jpg', 1, 5, NOW(), NOW()),
('红豆奶茶', 21, 23.00, '红豆奶茶，软糯香甜', 'https://example.com/ht/milktea9.jpg', 1, 5, NOW(), NOW()),
('芋泥奶茶', 21, 25.00, '芋泥奶茶，细腻绵密', 'https://example.com/ht/milktea10.jpg', 1, 5, NOW(), NOW()),

-- 果茶分类（category_id:22）
('满杯橙子茶', 22, 26.00, '满杯橙子茶，清新果香', 'https://example.com/ht/fruittea3.jpg', 1, 5, NOW(), NOW()),
('满杯蓝莓茶', 22, 28.00, '满杯蓝莓茶，抗氧化', 'https://example.com/ht/fruittea4.jpg', 1, 5, NOW(), NOW()),
('满杯猕猴桃茶', 22, 27.00, '满杯猕猴桃茶，维C丰富', 'https://example.com/ht/fruittea5.jpg', 1, 5, NOW(), NOW()),
('满杯菠萝茶', 22, 25.00, '满杯菠萝茶，热带风情', 'https://example.com/ht/fruittea6.jpg', 1, 5, NOW(), NOW()),
('满杯草莓茶', 22, 27.00, '满杯草莓茶，香甜可口', 'https://example.com/ht/fruittea7.jpg', 1, 5, NOW(), NOW()),
('满杯西瓜茶', 22, 24.00, '满杯西瓜茶，清爽解渴', 'https://example.com/ht/fruittea8.jpg', 1, 5, NOW(), NOW()),

-- 纯茶分类（category_id:23）
('茉莉花茶', 23, 16.00, '茉莉花茶，清香淡雅', 'https://example.com/ht/greentea2.jpg', 1, 5, NOW(), NOW()),
('乌龙茶', 23, 18.00, '乌龙茶，香气四溢', 'https://example.com/ht/greentea3.jpg', 1, 5, NOW(), NOW()),
('红茶', 23, 16.00, '红茶，醇厚香甜', 'https://example.com/ht/greentea4.jpg', 1, 5, NOW(), NOW()),
('普洱茶', 23, 20.00, '普洱茶，醇厚回甘', 'https://example.com/ht/greentea5.jpg', 1, 5, NOW(), NOW()),

-- 小食分类（category_id:24）
('鸡米花', 24, 15.00, '鸡米花，外酥里嫩', 'https://example.com/ht/snack1.jpg', 1, 5, NOW(), NOW()),
('薯条', 24, 12.00, '薯条，金黄酥脆', 'https://example.com/ht/snack2.jpg', 1, 5, NOW(), NOW()),
('鸡翅', 24, 18.00, '鸡翅，香辣可口', 'https://example.com/ht/snack3.jpg', 1, 5, NOW(), NOW()),
('鸡柳', 24, 15.00, '鸡柳，鲜嫩多汁', 'https://example.com/ht/snack4.jpg', 1, 5, NOW(), NOW()),
('洋葱圈', 24, 10.00, '洋葱圈，酥脆可口', 'https://example.com/ht/snack5.jpg', 1, 5, NOW(), NOW()),
('上校鸡块', 24, 12.00, '上校鸡块，经典美味', 'https://example.com/ht/snack6.jpg', 1, 5, NOW(), NOW());

-- ============================================
-- 2. 为喜茶（店铺ID:5）添加更多套餐数据
-- ============================================
INSERT INTO `setmeal` (`name`, `category_id`, `price`, `status`, `description`, `image`, `store_id`, `create_time`, `update_time`) VALUES
('青春活力套餐', NULL, 48.00, 1, '芝芝莓莓+满杯水果茶+鸡米花', 'https://example.com/ht/setmeal3.jpg', 5, NOW(), NOW()),
('闺蜜聚会套餐', NULL, 88.00, 1, '2杯芝芝芒芒+2杯多肉葡萄+2份鸡翅', 'https://example.com/ht/setmeal4.jpg', 5, NOW(), NOW()),
('家庭分享套餐', NULL, 128.00, 1, '2杯芝芝桃桃+2杯满杯橙子茶+鸡米花+薯条+上校鸡块', 'https://example.com/ht/setmeal5.jpg', 5, NOW(), NOW()),
('单人下午茶套餐', NULL, 38.00, 1, '珍珠奶茶+蛋挞+洋葱圈', 'https://example.com/ht/setmeal6.jpg', 5, NOW(), NOW()),
('双人下午茶套餐', NULL, 68.00, 1, '2杯抹茶奶茶+2份蛋糕+鸡柳', 'https://example.com/ht/setmeal7.jpg', 5, NOW(), NOW()),
('夏日清爽套餐', NULL, 58.00, 1, '满杯西瓜茶+满杯菠萝茶+薯条+洋葱圈', 'https://example.com/ht/setmeal8.jpg', 5, NOW(), NOW());

-- ============================================
-- 3. 为喜茶（店铺ID:5）添加订单数据
-- ============================================
-- 插入订单数据
INSERT INTO `orders` (`id`, `number`, `status`, `user_id`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_method`, `pay_time`, `amount`, `remark`, `create_time`, `update_time`) VALUES
-- 订单1：待付款
(101, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '001'), 1, 1, 5, 1, NOW(), NULL, NULL, NULL, 38.00, '少冰少糖', NOW(), NOW()),
-- 订单2：待接单
(102, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '002'), 2, 2, 5, NULL, NOW(), NOW(), 1, NOW(), 68.00, '正常冰正常糖', NOW(), NOW()),
-- 订单3：待派送
(103, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '003'), 3, 3, 5, NULL, NOW(), NOW(), 1, NOW(), 48.00, '多冰多糖', NOW(), NOW()),
-- 订单4：派送中
(104, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '004'), 4, 1, 5, 1, NOW(), NOW(), 2, NOW(), 88.00, '少冰正常糖', NOW(), NOW()),
-- 订单5：已完成
(105, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '005'), 5, 2, 5, NULL, NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 50 MINUTE, 1, NOW() - INTERVAL 50 MINUTE, 128.00, '正常冰少糖', NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 10 MINUTE),
-- 订单6：已完成
(106, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '006'), 5, 3, 5, NULL, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 1 HOUR 50 MINUTE, 2, NOW() - INTERVAL 1 HOUR 50 MINUTE, 58.00, '多冰少糖', NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 1 HOUR),
-- 订单7：已完成
(107, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '007'), 5, 1, 5, 2, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 HOUR 50 MINUTE, 1, NOW() - INTERVAL 2 HOUR 50 MINUTE, 38.00, '少冰多糖', NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 HOUR),
-- 订单8：已完成
(108, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '008'), 5, 2, 5, NULL, NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 3 HOUR 50 MINUTE, 2, NOW() - INTERVAL 3 HOUR 50 MINUTE, 68.00, '正常冰多糖', NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 3 HOUR),
-- 订单9：已完成
(109, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '009'), 5, 3, 5, NULL, NOW() - INTERVAL 5 HOUR, NOW() - INTERVAL 4 HOUR 50 MINUTE, 1, NOW() - INTERVAL 4 HOUR 50 MINUTE, 48.00, '多冰正常糖', NOW() - INTERVAL 5 HOUR, NOW() - INTERVAL 4 HOUR),
-- 订单10：已完成
(110, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '010'), 5, 1, 5, 1, NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 5 HOUR 50 MINUTE, 2, NOW() - INTERVAL 5 HOUR 50 MINUTE, 88.00, '少冰少糖', NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 5 HOUR);

-- 插入订单明细数据
INSERT INTO `order_detail` (`id`, `order_id`, `dish_id`, `setmeal_id`, `name`, `image`, `quantity`, `amount`, `create_time`) VALUES
-- 订单1明细
(201, 101, 132, NULL, '芝芝莓莓', 'https://example.com/ht/milktea1.jpg', 1, 28.00, NOW()),
(202, 101, NULL, NULL, '蛋挞', 'https://example.com/ht/tart1.jpg', 1, 12.00, NOW()),
-- 订单2明细
(203, 102, 132, NULL, '芝芝莓莓', 'https://example.com/ht/milktea1.jpg', 1, 28.00, NOW()),
(204, 102, 133, NULL, '多肉葡萄', 'https://example.com/ht/milktea2.jpg', 1, 28.00, NOW()),
(205, 102, NULL, NULL, '鸡翅', 'https://example.com/ht/snack3.jpg', 1, 12.00, NOW()),
-- 订单3明细
(206, 103, 132, NULL, '芝芝莓莓', 'https://example.com/ht/milktea1.jpg', 1, 28.00, NOW()),
(207, 103, 134, NULL, '满杯水果茶', 'https://example.com/ht/fruittea1.jpg', 1, 32.00, NOW()),
(208, 103, NULL, NULL, '鸡米花', 'https://example.com/ht/snack1.jpg', 1, 15.00, NOW()),
-- 订单4明细
(209, 104, 133, NULL, '多肉葡萄', 'https://example.com/ht/milktea2.jpg', 2, 56.00, NOW()),
(210, 104, NULL, NULL, '鸡翅', 'https://example.com/ht/snack3.jpg', 2, 36.00, NOW()),
-- 订单5明细
(211, 105, 132, NULL, '芝芝莓莓', 'https://example.com/ht/milktea1.jpg', 2, 56.00, NOW()),
(212, 105, 134, NULL, '满杯水果茶', 'https://example.com/ht/fruittea1.jpg', 2, 64.00, NOW()),
(213, 105, NULL, NULL, '鸡米花', 'https://example.com/ht/snack1.jpg', 1, 15.00, NOW()),
(214, 105, NULL, NULL, '薯条', 'https://example.com/ht/snack2.jpg', 1, 12.00, NOW()),
-- 订单6明细
(215, 106, 134, NULL, '满杯水果茶', 'https://example.com/ht/fruittea1.jpg', 1, 32.00, NOW()),
(216, 106, 135, NULL, '满杯红柚茶', 'https://example.com/ht/fruittea2.jpg', 1, 28.00, NOW()),
(217, 106, NULL, NULL, '薯条', 'https://example.com/ht/snack2.jpg', 1, 12.00, NOW()),
(218, 106, NULL, NULL, '洋葱圈', 'https://example.com/ht/snack5.jpg', 1, 10.00, NOW()),
-- 订单7明细
(219, 107, 136, NULL, '珍珠奶茶', 'https://example.com/ht/bobatea1.jpg', 1, 22.00, NOW()),
(220, 107, NULL, NULL, '蛋挞', 'https://example.com/ht/tart1.jpg', 1, 12.00, NOW()),
(221, 107, NULL, NULL, '洋葱圈', 'https://example.com/ht/snack5.jpg', 1, 10.00, NOW()),
-- 订单8明细
(222, 108, 136, NULL, '珍珠奶茶', 'https://example.com/ht/bobatea1.jpg', 2, 44.00, NOW()),
(223, 108, NULL, NULL, '蛋糕', 'https://example.com/ht/cake1.jpg', 2, 36.00, NOW()),
(224, 108, NULL, NULL, '鸡柳', 'https://example.com/ht/snack4.jpg', 1, 15.00, NOW()),
-- 订单9明细
(225, 109, 132, NULL, '芝芝莓莓', 'https://example.com/ht/milktea1.jpg', 1, 28.00, NOW()),
(226, 109, 134, NULL, '满杯水果茶', 'https://example.com/ht/fruittea1.jpg', 1, 32.00, NOW()),
(227, 109, NULL, NULL, '鸡米花', 'https://example.com/ht/snack1.jpg', 1, 15.00, NOW()),
-- 订单10明细
(228, 110, 133, NULL, '多肉葡萄', 'https://example.com/ht/milktea2.jpg', 2, 56.00, NOW()),
(229, 110, NULL, NULL, '鸡翅', 'https://example.com/ht/snack3.jpg', 2, 36.00, NOW());

-- ============================================
-- 4. 为喜茶（店铺ID:5）添加购物车数据
-- ============================================
INSERT INTO `shopping_cart` (`id`, `user_id`, `store_id`, `dish_id`, `setmeal_id`, `dish_flavor`, `number`, `amount`, `create_time`) VALUES
(301, 1, 5, 132, NULL, NULL, 2, 56.00, NOW()),
(302, 1, 5, 133, NULL, NULL, 1, 28.00, NOW()),
(303, 2, 5, 134, NULL, NULL, 1, 32.00, NOW()),
(304, 2, 5, NULL, NULL, NULL, 1, 15.00, NOW()),
(305, 3, 5, 135, NULL, NULL, 2, 56.00, NOW()),
(306, 3, 5, NULL, NULL, NULL, 2, 24.00, NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 数据插入完成
-- ============================================
SELECT 'shop_admin5测试数据插入完成！' AS message;

-- 验证数据插入结果
SELECT 
    (SELECT COUNT(*) FROM dish WHERE store_id = 5) AS dish_count,
    (SELECT COUNT(*) FROM setmeal WHERE store_id = 5) AS setmeal_count,
    (SELECT COUNT(*) FROM orders WHERE store_id = 5) AS order_count,
    (SELECT COUNT(*) FROM order_detail WHERE order_id IN (SELECT id FROM orders WHERE store_id = 5)) AS order_detail_count,
    (SELECT COUNT(*) FROM shopping_cart WHERE store_id = 5) AS cart_count;
