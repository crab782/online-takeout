-- ============================================
-- 清理并生成测试订单数据脚本
-- 创建日期：2026-02-21
-- 说明：清理现有订单数据，并生成连续7天的合理测试订单数据
-- ============================================

USE `takeout_system`;

-- ============================================
-- 第一步：清理现有订单数据
-- ============================================

-- 清理订单详情表数据
DELETE FROM `order_detail`;

-- 清理订单表数据
DELETE FROM `orders`;

-- 重置自增ID
ALTER TABLE `order_detail` AUTO_INCREMENT = 1;
ALTER TABLE `orders` AUTO_INCREMENT = 1;

-- ============================================
-- 第二步：生成测试订单数据
-- ============================================

-- 说明：
-- 1. 订单状态：0-待处理，1-商家已接单，2-准备中，3-骑手已接单，4-配送中，5-已完成，6-已取消
-- 2. 支付方式：1-微信支付，2-支付宝，3-银行卡
-- 3. 支付状态：0-未支付，1-已支付
-- 4. 生成连续7天的订单数据（2026-02-14 到 2026-02-20）

-- ============================================
-- 第1天：2026-02-14（周五）
-- ============================================

-- 订单1：用户1在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021400001', 5, 1, '测试用户1', 1, 1, '2026-02-14 11:30:00', '2026-02-14 11:35:00', '2026-02-14 11:32:00', '肯德基', 38.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '不要辣', '2026-02-14 11:30:00', '2026-02-14 12:15:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('香辣鸡腿堡', 1, 1, 1, 19.00),
('薯条（大）', 1, 5, 1, 12.00),
('可乐', 1, 7, 1, 9.00);

-- 订单2：用户2在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021400002', 5, 2, '测试用户2', 2, NULL, '2026-02-14 12:15:00', '2026-02-14 12:20:00', '2026-02-14 12:17:00', '麦当劳', 43.00, 2, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '多加冰', '2026-02-14 12:15:00', '2026-02-14 12:55:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('巨无霸', 2, 9, 1, 24.00),
('薯条（中）', 2, 11, 1, 10.00),
('可乐', 2, 14, 1, 9.00);

-- 订单3：用户3在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021400003', 5, 3, '测试用户3', 3, NULL, '2026-02-14 14:30:00', '2026-02-14 14:32:00', '2026-02-14 14:31:00', '星巴克', 32.00, 1, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '少糖', '2026-02-14 14:30:00', '2026-02-14 14:45:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 3, 15, 1, 32.00);

-- 订单4：用户1在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021400004', 5, 1, '测试用户1', 4, NULL, '2026-02-14 18:45:00', '2026-02-14 18:50:00', '2026-02-14 18:47:00', '必胜客', 89.00, 2, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '', '2026-02-14 18:45:00', '2026-02-14 19:30:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('超级至尊披萨', 4, 22, 1, 89.00);

-- 订单5：用户2在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021400005', 5, 2, '测试用户2', 5, NULL, '2026-02-14 20:00:00', '2026-02-14 20:02:00', '2026-02-14 20:01:00', '喜茶', 56.00, 1, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '常温', '2026-02-14 20:00:00', '2026-02-14 20:20:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('芝芝莓莓', 5, 29, 2, 56.00);

-- ============================================
-- 第2天：2026-02-15（周六）
-- ============================================

-- 订单6：用户1在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021500001', 5, 1, '测试用户1', 1, 1, '2026-02-15 10:30:00', '2026-02-15 10:35:00', '2026-02-15 10:32:00', '肯德基', 68.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '', '2026-02-15 10:30:00', '2026-02-15 11:15:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('香辣鸡腿堡', 6, 1, 2, 38.00),
('奥尔良烤鸡腿堡', 6, 2, 1, 20.00),
('可乐', 6, 7, 2, 18.00);

-- 订单7：用户3在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021500002', 5, 3, '测试用户3', 2, NULL, '2026-02-15 12:00:00', '2026-02-15 12:05:00', '2026-02-15 12:02:00', '麦当劳', 52.00, 2, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-15 12:00:00', '2026-02-15 12:40:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('麦辣鸡腿堡', 7, 10, 2, 38.00),
('薯条（中）', 7, 11, 1, 10.00),
('可乐', 7, 14, 1, 9.00);

-- 订单8：用户2在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021500003', 5, 2, '测试用户2', 3, NULL, '2026-02-15 15:30:00', '2026-02-15 15:32:00', '2026-02-15 15:31:00', '星巴克', 64.00, 1, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '两杯少糖', '2026-02-15 15:30:00', '2026-02-15 15:50:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 8, 15, 2, 64.00);

-- 订单9：用户1在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021500004', 5, 1, '测试用户1', 5, NULL, '2026-02-15 16:45:00', '2026-02-15 16:47:00', '2026-02-15 16:46:00', '喜茶', 44.00, 2, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '去冰', '2026-02-15 16:45:00', '2026-02-15 17:10:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('多肉葡萄', 9, 30, 1, 28.00),
('珍珠奶茶', 9, 34, 1, 22.00);

-- 订单10：用户3在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021500005', 5, 3, '测试用户3', 4, NULL, '2026-02-15 19:00:00', '2026-02-15 19:05:00', '2026-02-15 19:02:00', '必胜客', 73.00, 1, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-15 19:00:00', '2026-02-15 19:45:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('夏威夷风情披萨', 10, 23, 1, 79.00);

-- ============================================
-- 第3天：2026-02-16（周日）
-- ============================================

-- 订单11：用户2在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021600001', 5, 2, '测试用户2', 1, NULL, '2026-02-16 11:00:00', '2026-02-16 11:05:00', '2026-02-16 11:02:00', '肯德基', 44.00, 2, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '不要辣', '2026-02-16 11:00:00', '2026-02-16 11:40:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('香辣鸡腿堡', 11, 1, 1, 19.00),
('原味鸡', 11, 3, 1, 12.00),
('可乐', 11, 7, 1, 9.00);

-- 订单12：用户1在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021600002', 5, 1, '测试用户1', 3, 1, '2026-02-16 14:15:00', '2026-02-16 14:17:00', '2026-02-16 14:16:00', '星巴克', 50.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '少糖', '2026-02-16 14:15:00', '2026-02-16 14:35:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 12, 15, 1, 32.00),
('可颂', 12, 20, 1, 18.00);

-- 订单13：用户3在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021600003', 5, 3, '测试用户3', 5, NULL, '2026-02-16 15:30:00', '2026-02-16 15:32:00', '2026-02-16 15:31:00', '喜茶', 56.00, 2, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '常温', '2026-02-16 15:30:00', '2026-02-16 15:55:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('芝芝莓莓', 13, 29, 2, 56.00);

-- 订单14：用户2在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021600004', 5, 2, '测试用户2', 4, NULL, '2026-02-16 18:00:00', '2026-02-16 18:05:00', '2026-02-16 18:02:00', '必胜客', 128.00, 1, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '', '2026-02-16 18:00:00', '2026-02-16 18:50:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('超级至尊披萨', 14, 22, 1, 89.00),
('意大利肉酱面', 14, 24, 1, 45.00);

-- 订单15：用户1在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021600005', 5, 1, '测试用户1', 2, 1, '2026-02-16 20:30:00', '2026-02-16 20:35:00', '2026-02-16 20:32:00', '麦当劳', 52.00, 2, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '', '2026-02-16 20:30:00', '2026-02-16 21:10:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('巨无霸', 15, 9, 2, 48.00),
('可乐', 15, 14, 1, 9.00);

-- ============================================
-- 第4天：2026-02-17（周一）
-- ============================================

-- 订单16：用户3在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021700001', 5, 3, '测试用户3', 1, NULL, '2026-02-17 12:00:00', '2026-02-17 12:05:00', '2026-02-17 12:02:00', '肯德基', 33.00, 1, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-17 12:00:00', '2026-02-17 12:40:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('奥尔良烤鸡腿堡', 16, 2, 1, 20.00),
('香辣鸡翅', 16, 4, 1, 13.00);

-- 订单17：用户1在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021700002', 5, 1, '测试用户1', 5, 1, '2026-02-17 15:00:00', '2026-02-17 15:02:00', '2026-02-17 15:01:00', '喜茶', 28.00, 2, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '去冰', '2026-02-17 15:00:00', '2026-02-17 15:20:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('芝芝莓莓', 17, 29, 1, 28.00);

-- 订单18：用户2在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021700003', 5, 2, '测试用户2', 3, NULL, '2026-02-17 16:30:00', '2026-02-17 16:32:00', '2026-02-17 16:31:00', '星巴克', 50.00, 1, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '少糖', '2026-02-17 16:30:00', '2026-02-17 16:50:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 18, 15, 1, 32.00),
('可颂', 18, 20, 1, 18.00);

-- 订单19：用户3在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021700004', 5, 3, '测试用户3', 2, NULL, '2026-02-17 18:45:00', '2026-02-17 18:50:00', '2026-02-17 18:47:00', '麦当劳', 43.00, 2, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-17 18:45:00', '2026-02-17 19:25:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('巨无霸', 19, 9, 1, 24.00),
('薯条（中）', 19, 11, 1, 10.00),
('可乐', 19, 14, 1, 9.00);

-- 订单20：用户1在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021700005', 5, 1, '测试用户1', 4, 1, '2026-02-17 19:30:00', '2026-02-17 19:35:00', '2026-02-17 19:32:00', '必胜客', 79.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '', '2026-02-17 19:30:00', '2026-02-17 20:15:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('夏威夷风情披萨', 20, 23, 1, 79.00);

-- ============================================
-- 第5天：2026-02-18（周二）
-- ============================================

-- 订单21：用户1在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021800001', 5, 1, '测试用户1', 1, 1, '2026-02-18 11:30:00', '2026-02-18 11:35:00', '2026-02-18 11:32:00', '肯德基', 44.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '不要辣', '2026-02-18 11:30:00', '2026-02-18 12:15:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('香辣鸡腿堡', 21, 1, 1, 19.00),
('原味鸡', 21, 3, 1, 12.00),
('可乐', 21, 7, 1, 9.00);

-- 订单22：用户2在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021800002', 5, 2, '测试用户2', 5, NULL, '2026-02-18 14:00:00', '2026-02-18 14:02:00', '2026-02-18 14:01:00', '喜茶', 60.00, 2, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '去冰', '2026-02-18 14:00:00', '2026-02-18 14:25:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('满杯水果茶', 22, 31, 1, 32.00),
('珍珠奶茶', 22, 34, 1, 22.00);

-- 订单23：用户3在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021800003', 5, 3, '测试用户3', 3, NULL, '2026-02-18 15:45:00', '2026-02-18 15:47:00', '2026-02-18 15:46:00', '星巴克', 32.00, 1, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '少糖', '2026-02-18 15:45:00', '2026-02-18 16:05:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 23, 15, 1, 32.00);

-- 订单24：用户1在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021800004', 5, 1, '测试用户1', 2, 1, '2026-02-18 18:00:00', '2026-02-18 18:05:00', '2026-02-18 18:02:00', '麦当劳', 52.00, 2, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '', '2026-02-18 18:00:00', '2026-02-18 18:40:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('巨无霸', 24, 9, 2, 48.00),
('可乐', 24, 14, 1, 9.00);

-- 订单25：用户2在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021800005', 5, 2, '测试用户2', 4, NULL, '2026-02-18 19:30:00', '2026-02-18 19:35:00', '2026-02-18 19:32:00', '必胜客', 73.00, 1, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '', '2026-02-18 19:30:00', '2026-02-18 20:15:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('夏威夷风情披萨', 25, 23, 1, 79.00);

-- ============================================
-- 第6天：2026-02-19（周三）
-- ============================================

-- 订单26：用户3在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021900001', 5, 3, '测试用户3', 1, NULL, '2026-02-19 12:15:00', '2026-02-19 12:20:00', '2026-02-19 12:17:00', '肯德基', 38.00, 2, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-19 12:15:00', '2026-02-19 12:55:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('香辣鸡腿堡', 26, 1, 1, 19.00),
('薯条（大）', 26, 5, 1, 12.00),
('可乐', 26, 7, 1, 9.00);

-- 订单27：用户1在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021900002', 5, 1, '测试用户1', 3, 1, '2026-02-19 14:30:00', '2026-02-19 14:32:00', '2026-02-19 14:31:00', '星巴克', 50.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '少糖', '2026-02-19 14:30:00', '2026-02-19 14:50:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 27, 15, 1, 32.00),
('可颂', 27, 20, 1, 18.00);

-- 订单28：用户2在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021900003', 5, 2, '测试用户2', 5, NULL, '2026-02-19 15:15:00', '2026-02-19 15:17:00', '2026-02-19 15:16:00', '喜茶', 56.00, 2, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '常温', '2026-02-19 15:15:00', '2026-02-19 15:40:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('芝芝莓莓', 28, 29, 2, 56.00);

-- 订单29：用户3在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021900004', 5, 3, '测试用户3', 2, NULL, '2026-02-19 18:30:00', '2026-02-19 18:35:00', '2026-02-19 18:32:00', '麦当劳', 43.00, 1, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-19 18:30:00', '2026-02-19 19:10:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('巨无霸', 29, 9, 1, 24.00),
('薯条（中）', 29, 11, 1, 10.00),
('可乐', 29, 14, 1, 9.00);

-- 订单30：用户1在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026021900005', 5, 1, '测试用户1', 4, 1, '2026-02-19 20:00:00', '2026-02-19 20:05:00', '2026-02-19 20:02:00', '必胜客', 128.00, 2, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '', '2026-02-19 20:00:00', '2026-02-19 20:50:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('超级至尊披萨', 30, 22, 1, 89.00),
('意大利肉酱面', 30, 24, 1, 45.00);

-- ============================================
-- 第7天：2026-02-20（周四）
-- ============================================

-- 订单31：用户2在肯德基下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026022000001', 5, 2, '测试用户2', 1, NULL, '2026-02-20 11:00:00', '2026-02-20 11:05:00', '2026-02-20 11:02:00', '肯德基', 44.00, 1, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '不要辣', '2026-02-20 11:00:00', '2026-02-20 11:40:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('香辣鸡腿堡', 31, 1, 1, 19.00),
('原味鸡', 31, 3, 1, 12.00),
('可乐', 31, 7, 1, 9.00);

-- 订单32：用户3在喜茶下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026022000002', 5, 3, '测试用户3', 5, NULL, '2026-02-20 14:45:00', '2026-02-20 14:47:00', '2026-02-20 14:46:00', '喜茶', 44.00, 2, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '去冰', '2026-02-20 14:45:00', '2026-02-20 15:10:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('多肉葡萄', 32, 30, 1, 28.00),
('珍珠奶茶', 32, 34, 1, 22.00);

-- 订单33：用户1在星巴克下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026022000003', 5, 1, '测试用户1', 3, 1, '2026-02-20 16:00:00', '2026-02-20 16:02:00', '2026-02-20 16:01:00', '星巴克', 32.00, 1, 1, '张三', '北京市北京市东城区建国路88号1号楼101室', '13900139001', '少糖', '2026-02-20 16:00:00', '2026-02-20 16:20:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('拿铁', 33, 15, 1, 32.00);

-- 订单34：用户2在麦当劳下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026022000004', 5, 2, '测试用户2', 2, NULL, '2026-02-20 18:15:00', '2026-02-20 18:20:00', '2026-02-20 18:17:00', '麦当劳', 52.00, 2, 1, '测试用户2', '北京市北京市东城区建国路99号2号楼202室', '13900139002', '', '2026-02-20 18:15:00', '2026-02-20 18:55:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('巨无霸', 34, 9, 2, 48.00),
('可乐', 34, 14, 1, 9.00);

-- 订单35：用户3在必胜客下单（已完成）
INSERT INTO `orders` (`number`, `status`, `user_id`, `user_name`, `store_id`, `address_book_id`, `order_time`, `checkout_time`, `pay_time`, `store_name`, `amount`, `pay_method`, `pay_status`, `receiver`, `address`, `phone`, `remark`, `create_time`, `update_time`) VALUES
('2026022000005', 5, 3, '测试用户3', 4, NULL, '2026-02-20 19:45:00', '2026-02-20 19:50:00', '2026-02-20 19:47:00', '必胜客', 79.00, 1, 1, '测试用户3', '北京市北京市东城区建国路100号3号楼303室', '13900139003', '', '2026-02-20 19:45:00', '2026-02-20 20:30:00');

INSERT INTO `order_detail` (`name`, `order_id`, `dish_id`, `number`, `amount`) VALUES
('夏威夷风情披萨', 35, 23, 1, 79.00);

-- ============================================
-- 验证数据
-- ============================================

-- 查看订单总数
SELECT COUNT(*) as total_orders FROM orders;

-- 查看订单详情总数
SELECT COUNT(*) as total_order_details FROM order_detail;

-- 查看各店铺订单数量
SELECT store_id, store_name, COUNT(*) as order_count, SUM(amount) as total_amount 
FROM orders 
GROUP BY store_id, store_name 
ORDER BY store_id;

-- 查看各用户订单数量
SELECT user_id, user_name, COUNT(*) as order_count, SUM(amount) as total_amount 
FROM orders 
GROUP BY user_id, user_name 
ORDER BY user_id;

-- 查看订单状态分布
SELECT 
    CASE status 
        WHEN 0 THEN '待处理'
        WHEN 1 THEN '商家已接单'
        WHEN 2 THEN '准备中'
        WHEN 3 THEN '骑手已接单'
        WHEN 4 THEN '配送中'
        WHEN 5 THEN '已完成'
        WHEN 6 THEN '已取消'
        ELSE '未知状态'
    END as status_name,
    COUNT(*) as order_count
FROM orders 
GROUP BY status 
ORDER BY status;

-- 查看每日订单数量
SELECT DATE(order_time) as order_date, COUNT(*) as order_count, SUM(amount) as total_amount
FROM orders 
GROUP BY DATE(order_time)
ORDER BY order_date;

-- 查看订单时间分布
SELECT 
    CASE 
        WHEN HOUR(order_time) BETWEEN 6 AND 10 THEN '早餐时段(6-10)'
        WHEN HOUR(order_time) BETWEEN 11 AND 13 THEN '午餐时段(11-13)'
        WHEN HOUR(order_time) BETWEEN 14 AND 17 THEN '下午茶时段(14-17)'
        WHEN HOUR(order_time) BETWEEN 18 AND 20 THEN '晚餐时段(18-20)'
        ELSE '其他时段'
    END as time_period,
    COUNT(*) as order_count
FROM orders 
GROUP BY time_period
ORDER BY MIN(HOUR(order_time));

-- 完成
SELECT '测试订单数据生成完成！' as message;
