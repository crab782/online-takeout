-- 外卖系统初始数据插入脚本
-- 创建日期：2026-02-13

SET FOREIGN_KEY_CHECKS = 0;

USE `takeout_system`;

-- ============================================
-- 1. 插入店铺数据
-- ============================================
INSERT INTO `store` (`id`, `name`, `address`, `phone`, `description`, `image`, `status`, `category_id`, `open_time`, `close_time`, `delivery_fee`, `min_order_amount`, `create_time`, `update_time`) VALUES
(1, '肯德基', '北京市朝阳区建国路88号', '400-823-8230', '肯德基（KFC），全球知名快餐连锁品牌，主营炸鸡、汉堡等西式快餐', 'https://example.com/images/kfc.jpg', 1, 1, '06:00', '23:00', 5.00, 20.00, NOW(), NOW()),
(2, '麦当劳', '北京市朝阳区建国路99号', '400-920-0205', '麦当劳（McDonald''s），全球最大快餐连锁企业，主营汉堡、薯条等', 'https://example.com/images/mcdonalds.jpg', 1, 1, '06:00', '23:00', 5.00, 20.00, NOW(), NOW()),
(3, '星巴克', '北京市朝阳区建国路100号', '400-820-6988', '星巴克（Starbucks），全球知名咖啡连锁品牌，主营咖啡、茶饮等', 'https://example.com/images/starbucks.jpg', 1, 2, '07:00', '22:00', 3.00, 15.00, NOW(), NOW()),
(4, '必胜客', '北京市朝阳区建国路101号', '400-812-3123', '必胜客（Pizza Hut），全球知名披萨连锁品牌，主营披萨、意面等', 'https://example.com/images/pizzahut.jpg', 1, 1, '10:00', '22:00', 6.00, 30.00, NOW(), NOW()),
(5, '喜茶', '北京市朝阳区建国路102号', '400-888-8888', '喜茶（HEYTEA），中国知名新式茶饮品牌，主营奶茶、果茶等', 'https://example.com/images/heytea.jpg', 1, 2, '09:00', '22:00', 4.00, 10.00, NOW(), NOW());

-- ============================================
-- 2. 插入员工数据（商家管理员和平台管理员）
-- 密码使用BCrypt加密：
-- 123456 -> $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- 123 -> $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
-- ============================================
INSERT INTO `employee` (`id`, `username`, `password`, `name`, `phone`, `email`, `role`, `store_id`, `status`, `created_at`, `updated_at`) VALUES
-- 平台管理员
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '系统管理员', '13800138000', 'admin@takeout.com', 1, NULL, 1, NOW(), NOW()),
-- 商家管理员（肯德基）
(2, 'shop_admin1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '肯德基管理员', '13800138001', 'admin1@kfc.com', 2, 1, 1, NOW(), NOW()),
-- 商家管理员（麦当劳）
(3, 'shop_admin2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '麦当劳管理员', '13800138002', 'admin2@mcdonalds.com', 2, 2, 1, NOW(), NOW()),
-- 商家管理员（星巴克）
(4, 'shop_admin3', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '星巴克管理员', '13800138003', 'admin3@starbucks.com', 2, 3, 1, NOW(), NOW()),
-- 商家管理员（必胜客）
(5, 'shop_admin4', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '必胜客管理员', '13800138004', 'admin4@pizzahut.com', 2, 4, 1, NOW(), NOW()),
-- 商家管理员（喜茶）
(6, 'shop_admin5', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '喜茶管理员', '13800138005', 'admin5@heytea.com', 2, 5, 1, NOW(), NOW());

-- ============================================
-- 3. 插入前端用户数据
-- 密码使用BCrypt加密：123456 -> $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- ============================================
INSERT INTO `user` (`id`, `username`, `password`, `name`, `phone`, `email`, `sex`, `id_number`, `avatar`, `status`, `create_time`, `update_time`) VALUES
(1, 'test1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '测试用户1', '13900139001', 'test1@example.com', 1, '110101199001011234', 'https://example.com/avatar1.jpg', 1, NOW(), NOW()),
(2, 'test2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '测试用户2', '13900139002', 'test2@example.com', 0, '110101199002022345', 'https://example.com/avatar2.jpg', 1, NOW(), NOW()),
(3, 'test3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '测试用户3', '13900139003', 'test3@example.com', 1, '110101199003033456', 'https://example.com/avatar3.jpg', 1, NOW(), NOW());

-- ============================================
-- 4. 插入分类数据
-- ============================================
-- 肯德基分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`, `create_time`, `update_time`, `store_id`) VALUES
('汉堡', 1, 1, 1, NOW(), NOW(), 1),
('炸鸡', 1, 2, 1, NOW(), NOW(), 1),
('小食', 1, 3, 1, NOW(), NOW(), 1),
('饮料', 1, 4, 1, NOW(), NOW(), 1),
('套餐', 2, 5, 1, NOW(), NOW(), 1);

-- 麦当劳分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`, `create_time`, `update_time`, `store_id`) VALUES
('汉堡', 1, 1, 1, NOW(), NOW(), 2),
('小食', 1, 2, 1, NOW(), NOW(), 2),
('甜品', 1, 3, 1, NOW(), NOW(), 2),
('饮料', 1, 4, 1, NOW(), NOW(), 2),
('套餐', 2, 5, 1, NOW(), NOW(), 2);

-- 星巴克分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`, `create_time`, `update_time`, `store_id`) VALUES
('咖啡', 1, 1, 1, NOW(), NOW(), 3),
('茶饮', 1, 2, 1, NOW(), NOW(), 3),
('轻食', 1, 3, 1, NOW(), NOW(), 3),
('甜点', 1, 4, 1, NOW(), NOW(), 3);

-- 必胜客分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`, `create_time`, `update_time`, `store_id`) VALUES
('披萨', 1, 1, 1, NOW(), NOW(), 4),
('意面', 1, 2, 1, NOW(), NOW(), 4),
('小食', 1, 3, 1, NOW(), NOW(), 4),
('甜品', 1, 4, 1, NOW(), NOW(), 4),
('套餐', 2, 5, 1, NOW(), NOW(), 4);

-- 喜茶分类
INSERT INTO `category` (`name`, `type`, `sort`, `status`, `create_time`, `update_time`, `store_id`) VALUES
('奶茶', 1, 1, 1, NOW(), NOW(), 5),
('果茶', 1, 2, 1, NOW(), NOW(), 5),
('纯茶', 1, 3, 1, NOW(), NOW(), 5),
('小食', 1, 4, 1, NOW(), NOW(), 5);

-- ============================================
-- 5. 插入菜品数据
-- ============================================
-- 肯德基菜品
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`, `store_id`, `create_time`, `update_time`) VALUES
('香辣鸡腿堡', 1, 19.00, '香辣鸡腿堡，外酥里嫩，香辣可口', 'https://example.com/kfc/burger1.jpg', 1, 1, NOW(), NOW()),
('奥尔良烤鸡腿堡', 1, 20.00, '奥尔良烤鸡腿堡，鲜嫩多汁', 'https://example.com/kfc/burger2.jpg', 1, 1, NOW(), NOW()),
('原味鸡', 2, 12.00, '原味鸡，经典美味', 'https://example.com/kfc/chicken1.jpg', 1, 1, NOW(), NOW()),
('香辣鸡翅', 2, 13.00, '香辣鸡翅，外酥里嫩', 'https://example.com/kfc/wing1.jpg', 1, 1, NOW(), NOW()),
('薯条（大）', 3, 12.00, '薯条（大），金黄酥脆', 'https://example.com/kfc/fries1.jpg', 1, 1, NOW(), NOW()),
('蛋挞', 3, 8.00, '葡式蛋挞，香甜可口', 'https://example.com/kfc/tart1.jpg', 1, 1, NOW(), NOW()),
('可乐', 4, 9.00, '可口可乐，冰爽解渴', 'https://example.com/kfc/coke1.jpg', 1, 1, NOW(), NOW()),
('雪碧', 4, 9.00, '雪碧，清爽怡人', 'https://example.com/kfc/sprite1.jpg', 1, 1, NOW(), NOW());

-- 麦当劳菜品
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`, `store_id`, `create_time`, `update_time`) VALUES
('巨无霸', 6, 24.00, '巨无霸，双层牛肉，满足感十足', 'https://example.com/mcd/burger1.jpg', 1, 2, NOW(), NOW()),
('麦辣鸡腿堡', 6, 19.00, '麦辣鸡腿堡，香辣过瘾', 'https://example.com/mcd/burger2.jpg', 1, 2, NOW(), NOW()),
('麦乐鸡', 7, 18.00, '麦乐鸡，外酥里嫩', 'https://example.com/mcd/nuggets1.jpg', 1, 2, NOW(), NOW()),
('薯条（中）', 7, 10.00, '薯条（中），金黄酥脆', 'https://example.com/mcd/fries1.jpg', 1, 2, NOW(), NOW()),
('麦旋风', 8, 15.00, '麦旋风，香浓美味', 'https://example.com/mcd/shake1.jpg', 1, 2, NOW(), NOW()),
('可乐', 9, 9.00, '可口可乐，冰爽解渴', 'https://example.com/mcd/coke1.jpg', 1, 2, NOW(), NOW());

-- 星巴克菜品
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`, `store_id`, `create_time`, `update_time`) VALUES
('拿铁', 11, 32.00, '拿铁，香浓咖啡与丝滑牛奶的完美融合', 'https://example.com/sb/latte1.jpg', 1, 3, NOW(), NOW()),
('美式咖啡', 11, 25.00, '美式咖啡，纯正咖啡香', 'https://example.com/sb/americano1.jpg', 1, 3, NOW(), NOW()),
('卡布奇诺', 11, 32.00, '卡布奇诺，经典意式咖啡', 'https://example.com/sb/cappuccino1.jpg', 1, 3, NOW(), NOW()),
('抹茶星冰乐', 12, 35.00, '抹茶星冰乐，清爽冰爽', 'https://example.com/sb/matcha1.jpg', 1, 3, NOW(), NOW()),
('芒果西番莲果茶', 12, 30.00, '芒果西番莲果茶，果香浓郁', 'https://example.com/sb/fruittea1.jpg', 1, 3, NOW(), NOW()),
('芝士蛋糕', 14, 28.00, '芝士蛋糕，香甜可口', 'https://example.com/sb/cake1.jpg', 1, 3, NOW(), NOW()),
('可颂', 13, 18.00, '可颂，酥脆可口', 'https://example.com/sb/croissant1.jpg', 1, 3, NOW(), NOW());

-- 必胜客菜品
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`, `store_id`, `create_time`, `update_time`) VALUES
('超级至尊披萨', 16, 89.00, '超级至尊披萨，多种配料，丰富美味', 'https://example.com/ph/pizza1.jpg', 1, 4, NOW(), NOW()),
('夏威夷风情披萨', 16, 79.00, '夏威夷风情披萨，清爽果香', 'https://example.com/ph/pizza2.jpg', 1, 4, NOW(), NOW()),
('意大利肉酱面', 17, 45.00, '意大利肉酱面，经典意式风味', 'https://example.com/ph/pasta1.jpg', 1, 4, NOW(), NOW()),
('奶油蘑菇汤', 18, 18.00, '奶油蘑菇汤，香浓美味', 'https://example.com/ph/soup1.jpg', 1, 4, NOW(), NOW()),
('炸鸡翅', 18, 28.00, '炸鸡翅，外酥里嫩', 'https://example.com/ph/wing1.jpg', 1, 4, NOW(), NOW()),
('提拉米苏', 19, 32.00, '提拉米苏，经典意式甜点', 'https://example.com/ph/tiramisu1.jpg', 1, 4, NOW(), NOW());

-- 喜茶菜品
INSERT INTO `dish` (`name`, `category_id`, `price`, `description`, `image`, `status`, `store_id`, `create_time`, `update_time`) VALUES
('芝芝莓莓', 21, 28.00, '芝芝莓莓，新鲜草莓与芝士的完美结合', 'https://example.com/ht/milktea1.jpg', 1, 5, NOW(), NOW()),
('多肉葡萄', 21, 28.00, '多肉葡萄，果肉饱满，口感丰富', 'https://example.com/ht/milktea2.jpg', 1, 5, NOW(), NOW()),
('满杯水果茶', 22, 32.00, '满杯水果茶，多种水果，清爽解渴', 'https://example.com/ht/fruittea1.jpg', 1, 5, NOW(), NOW()),
('满杯红柚茶', 22, 28.00, '满杯红柚茶，清新果香', 'https://example.com/ht/fruittea2.jpg', 1, 5, NOW(), NOW()),
('纯绿茶', 23, 18.00, '纯绿茶，清香淡雅', 'https://example.com/ht/greentea1.jpg', 1, 5, NOW(), NOW()),
('珍珠奶茶', 21, 22.00, '珍珠奶茶，经典港式风味', 'https://example.com/ht/bobatea1.jpg', 1, 5, NOW(), NOW()),
('蛋挞', 24, 12.00, '葡式蛋挞，香甜可口', 'https://example.com/ht/tart1.jpg', 1, 5, NOW(), NOW()),
('蛋糕', 24, 18.00, '蛋糕，松软香甜', 'https://example.com/ht/cake1.jpg', 1, 5, NOW(), NOW());

-- ============================================
-- 6. 插入套餐数据
-- ============================================
-- 肯德基套餐
INSERT INTO `setmeal` (`name`, `category_id`, `price`, `status`, `description`, `image`, `store_id`, `create_time`, `update_time`) VALUES
('单人套餐A', '5', 35.00, 1, '香辣鸡腿堡+薯条（中）+可乐', 'https://example.com/kfc/setmeal1.jpg', 1, NOW(), NOW()),
('双人套餐A', '5', 68.00, 1, '2个香辣鸡腿堡+2份薯条（中）+2杯可乐', 'https://example.com/kfc/setmeal2.jpg', 1, NOW(), NOW()),
('全家桶', '5', 128.00, 1, '多种炸鸡组合，适合全家分享', 'https://example.com/kfc/setmeal3.jpg', 1, NOW(), NOW());

-- 麦当劳套餐
INSERT INTO `setmeal` (`name`, `category_id`, `price`, `status`, `description`, `image`, `store_id`, `create_time`, `update_time`) VALUES
('1+1随心配', '10', 13.90, 1, '任选2款小食或饮料', 'https://example.com/mcd/setmeal1.jpg', 2, NOW(), NOW()),
('超值套餐', '10', 39.00, 1, '巨无霸+薯条（中）+可乐', 'https://example.com/mcd/setmeal2.jpg', 2, NOW(), NOW());

-- 星巴克套餐
INSERT INTO `setmeal` (`name`, `category_id`, `price`, `status`, `description`, `image`, `store_id`, `create_time`, `update_time`) VALUES
('下午茶套餐', NULL, 50.00, 1, '拿铁+可颂', 'https://example.com/sb/setmeal1.jpg', 3, NOW(), NOW()),
('咖啡套餐', NULL, 60.00, 1, '2杯拿铁+芝士蛋糕', 'https://example.com/sb/setmeal2.jpg', 3, NOW(), NOW());

-- 必胜客套餐
INSERT INTO `setmeal` (`name`, `category_id`, `price`, `status`, `description`, `image`, `store_id`, `create_time`, `update_time`) VALUES
('超级至尊套餐', '20', 128.00, 1, '超级至尊披萨+意大利肉酱面+炸鸡翅', 'https://example.com/ph/setmeal1.jpg', 4, NOW(), NOW()),
('情侣套餐', '20', 158.00, 1, '2个披萨+2份意面+2份甜点', 'https://example.com/ph/setmeal2.jpg', 4, NOW(), NOW());

-- 喜茶套餐
INSERT INTO `setmeal` (`name`, `category_id`, `price`, `status`, `description`, `image`, `store_id`, `create_time`, `update_time`) VALUES
('闺蜜套餐', NULL, 50.00, 1, '2杯芝芝莓莓+蛋挞', 'https://example.com/ht/setmeal1.jpg', 5, NOW(), NOW()),
('下午茶套餐', NULL, 45.00, 1, '满杯水果茶+蛋糕', 'https://example.com/ht/setmeal2.jpg', 5, NOW(), NOW());

-- ============================================
-- 7. 插入活动数据
-- ============================================
INSERT INTO `activity` (`title`, `description`, `image_url`, `status`, `start_time`, `end_time`, `create_time`, `update_time`) VALUES
('新用户专享优惠', '新用户注册即送50元优惠券', 'https://example.com/activity1.jpg', 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', NOW(), NOW()),
('限时秒杀', '每日10点限时秒杀，低至5折', 'https://example.com/activity2.jpg', 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', NOW(), NOW()),
('满减活动', '满100减20，满200减50', 'https://example.com/activity3.jpg', 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', NOW(), NOW());

-- ============================================
-- 8. 插入地址簿数据（测试用户1）
-- ============================================
INSERT INTO `address_book` (`user_id`, `consignee`, `phone`, `sex`, `province_code`, `province_name`, `city_code`, `city_name`, `district_code`, `district_name`, `detail`, `label`, `is_default`, `create_time`, `update_time`) VALUES
(1, '张三', '13900139001', 1, '110000', '北京市', '110100', '北京市', '110101', '东城区', '建国路88号1号楼101室', '家', 1, NOW(), NOW()),
(1, '张三', '13900139001', 1, '110000', '北京市', '110100', '北京市', '110101', '东城区', '建国路99号2号楼202室', '公司', 0, NOW(), NOW());

-- ============================================
-- 9. 插入店铺收藏数据
-- ============================================
INSERT INTO `store_favorite` (`user_id`, `store_id`, `create_time`) VALUES
(1, 1, NOW()),
(1, 3, NOW()),
(2, 2, NOW()),
(2, 5, NOW()),
(3, 1, NOW()),
(3, 4, NOW());

-- ============================================
-- 10. 插入用户签到数据
-- ============================================
INSERT INTO `user_checkin` (`user_id`, `checkin_date`, `checkin_time`, `points`) VALUES
(1, CURDATE(), NOW(), 1),
(2, CURDATE(), NOW(), 1),
(3, CURDATE(), NOW(), 1);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 数据插入完成
-- ============================================
SELECT '初始数据插入完成！' AS message;
