-- 更新用户密码为正确的BCrypt加密密码
-- 密码：123456
-- 使用Spring Security的BCryptPasswordEncoder生成的新加密密码

UPDATE `user` SET `password` = '$2a$10$Q/jwHtEiAMm62ktf/h6BV.GEBcjEEppbvIJ79QrmNqtB8mm/n7yhG' WHERE `phone` = '13900139001';
UPDATE `user` SET `password` = '$2a$10$Q/jwHtEiAMm62ktf/h6BV.GEBcjEEppbvIJ79QrmNqtB8mm/n7yhG' WHERE `phone` = '13900139002';
UPDATE `user` SET `password` = '$2a$10$Q/jwHtEiAMm62ktf/h6BV.GEBcjEEppbvIJ79QrmNqtB8mm/n7yhG' WHERE `phone` = '13900139003';

-- 验证更新结果
SELECT id, phone, password FROM user WHERE phone IN ('13900139001', '13900139002', '13900139003');
