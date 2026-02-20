-- 为喜茶店铺（store_id=5）的订单添加评论

-- 1. 检查是否存在评论表，如果不存在则创建
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `store_id` bigint NOT NULL,
  `rating` tinyint NOT NULL COMMENT '评分：1-5星',
  `content` varchar(500) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. 为喜茶店铺的订单添加评论
INSERT INTO `comment` (
    `id`, `content`, `user_id`, `user_name`, `avatar`, 
    `order_id`, `order_number`, `store_id`, `rating`, `images`, 
    `status`, `create_time`, `update_time`
) VALUES
-- 订单101评论：芝芝芒芒
(401, '芝芝芒芒非常好喝，芒果味很浓郁，芝士奶盖也很正宗，下次还会再来！', 1, '测试用户1', 'https://example.com/ht/avatar1.jpg', 
 101, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '001'), 5, 5, 'https://example.com/ht/comment1.jpg', 
 0, NOW(), NOW()),
-- 订单102评论：芝芝桃桃
(402, '芝芝桃桃的桃子味很新鲜，甜度刚好，就是价格稍微有点贵。', 2, '测试用户2', 'https://example.com/ht/avatar2.jpg', 
 102, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '002'), 5, 4, 'https://example.com/ht/comment2.jpg', 
 0, NOW(), NOW()),
-- 订单103评论：黑糖珍珠奶茶
(403, '黑糖珍珠奶茶的珍珠很Q弹，黑糖味很浓，奶茶也很丝滑，推荐！', 3, '测试用户3', 'https://example.com/ht/avatar3.jpg', 
 103, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '003'), 5, 5, 'https://example.com/ht/comment3.jpg', 
 0, NOW(), NOW()),
-- 订单104评论：抹茶奶茶
(404, '抹茶奶茶的抹茶味很正宗，不会太苦，甜度适中，好评！', 1, '测试用户1', 'https://example.com/ht/avatar1.jpg', 
 104, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '004'), 5, 4, 'https://example.com/ht/comment4.jpg', 
 0, NOW(), NOW()),
-- 订单105评论：焦糖奶茶
(405, '焦糖奶茶的焦糖味很香，奶茶很浓郁，珍珠也很有嚼劲，非常满意！', 2, '测试用户2', 'https://example.com/ht/avatar2.jpg', 
 105, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '005'), 5, 5, 'https://example.com/ht/comment5.jpg', 
 0, NOW(), NOW()),
-- 订单106评论：燕麦奶茶
(406, '燕麦奶茶很健康，燕麦颗粒很多，奶茶也很顺滑，值得尝试。', 3, '测试用户3', 'https://example.com/ht/avatar3.jpg', 
 106, CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '006'), 5, 4, 'https://example.com/ht/comment6.jpg', 
 0, NOW(), NOW());

-- 3. 查看已添加的评论
SELECT * FROM `comment` WHERE `store_id` = 5 ORDER BY `create_time` DESC;