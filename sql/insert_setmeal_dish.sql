-- 为setmeal表中的套餐生成对应的setmeal_dish数据
-- 清空现有的setmeal_dish数据（可选，如果需要重新生成）
-- DELETE FROM setmeal_dish;

-- 1. 单人套餐A (id: 1, price: 35.00) - 香辣鸡腿堡+薯条+可乐
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(1, 1, '香辣鸡腿堡', 19.00, 1, 1),
(1, 5, '薯条（大）', 12.00, 1, 2),
(1, 7, '可乐', 9.00, 1, 3);

-- 2. 双人套餐A (id: 2, price: 68.00) - 2个香辣鸡腿堡+2份薯条+2杯可乐
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(2, 1, '香辣鸡腿堡', 19.00, 2, 1),
(2, 5, '薯条（大）', 12.00, 2, 2),
(2, 7, '可乐', 9.00, 2, 3);

-- 3. 全家桶 (id: 3, price: 128.00) - 多种炸鸡组合
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(3, 3, '原味鸡', 12.00, 2, 1),
(3, 4, '香辣鸡翅', 13.00, 2, 2),
(3, 5, '薯条（大）', 12.00, 2, 3),
(3, 1, '香辣鸡腿堡', 19.00, 2, 4),
(3, 7, '可乐', 9.00, 2, 5);

-- 4. 1+1随心配 (id: 4, price: 13.90) - 任选2款小食
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(4, 5, '薯条（大）', 12.00, 1, 1),
(4, 6, '蛋挞', 8.00, 1, 2);

-- 5. 超值套餐 (id: 5, price: 39.00) - 巨无霸+薯条+可乐
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(5, 2, '奥尔良烤鸡腿堡', 20.00, 1, 1),
(5, 5, '薯条（大）', 12.00, 1, 2),
(5, 7, '可乐', 9.00, 1, 3);

-- 6. 下午茶套餐 (id: 6, price: 50.00) - 拿铁+可颂
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(6, 3, '原味鸡', 12.00, 2, 1),
(6, 5, '薯条（大）', 12.00, 2, 2);

-- 7. 咖啡套餐 (id: 7, price: 60.00) - 2杯拿铁+芝士蛋糕
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(7, 3, '原味鸡', 12.00, 2, 1),
(7, 6, '蛋挞', 8.00, 2, 2);

-- 8. 超级至尊套餐 (id: 8, price: 128.00) - 超级至尊披萨+意大利肉酱面+炸鸡翅
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(8, 3, '原味鸡', 12.00, 3, 1),
(8, 4, '香辣鸡翅', 13.00, 3, 2),
(8, 5, '薯条（大）', 12.00, 2, 3);

-- 9. 情侣套餐 (id: 9, price: 158.00) - 2个披萨+2份意面+2份甜点
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(9, 3, '原味鸡', 12.00, 4, 1),
(9, 4, '香辣鸡翅', 13.00, 4, 2),
(9, 5, '薯条（大）', 12.00, 3, 3);

-- 10. 闺蜜套餐 (id: 10, price: 50.00) - 2杯芝芝莓莓+蛋挞
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(10, 6, '蛋挞', 8.00, 3, 1),
(10, 3, '原味鸡', 12.00, 2, 2);

-- 11. 下午茶套餐 (id: 11, price: 45.00) - 满杯水果茶+蛋糕
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(11, 6, '蛋挞', 8.00, 3, 1),
(11, 3, '原味鸡', 12.00, 2, 2);

-- 12. 青春活力套餐 (id: 12, price: 48.00) - 芝芝莓莓+满杯水果茶+鸡米花
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(12, 6, '蛋挞', 8.00, 3, 1),
(12, 3, '原味鸡', 12.00, 2, 2);

-- 13. 闺蜜聚会套餐 (id: 14, price: 88.00) - 2杯芝芝芒芒+2杯多肉葡萄+2份鸡翅
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(14, 4, '香辣鸡翅', 13.00, 3, 1),
(14, 3, '原味鸡', 12.00, 3, 2),
(14, 6, '蛋挞', 8.00, 2, 3);

-- 14. 家庭分享套餐 (id: 15, price: 128.00) - 多种组合
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(15, 1, '香辣鸡腿堡', 19.00, 2, 1),
(15, 3, '原味鸡', 12.00, 3, 2),
(15, 4, '香辣鸡翅', 13.00, 2, 3),
(15, 5, '薯条（大）', 12.00, 2, 4);

-- 15. 单人下午茶套餐 (id: 16, price: 38.00) - 珍珠奶茶+蛋挞+洋葱圈
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(16, 6, '蛋挞', 8.00, 2, 1),
(16, 3, '原味鸡', 12.00, 2, 2);

-- 16. 双人下午茶套餐 (id: 17, price: 68.00) - 2杯抹茶奶茶+2份蛋糕+鸡柳
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(17, 6, '蛋挞', 8.00, 3, 1),
(17, 3, '原味鸡', 12.00, 3, 2);

-- 17. 夏日清爽套餐 (id: 18, price: 58.00) - 满杯西瓜茶+满杯菠萝茶+薯条+洋葱圈
INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies, sort) VALUES
(18, 5, '薯条（大）', 12.00, 2, 1),
(18, 3, '原味鸡', 12.00, 2, 2);

-- 查询生成的setmeal_dish数据
SELECT * FROM setmeal_dish ORDER BY setmeal_id, sort;