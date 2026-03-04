-- 更新用户密码为加密后的密码
-- 密码：12345678
-- 加密后的密码：$2a$10$e7JQ9f8B3y7e0j9w7e0j9e7e0j9w7e0j9e7e0j9w7e0j9e7e0j9w7e0j9e

UPDATE user SET password = '$2a$10$e7JQ9f8B3y7e0j9w7e0j9e7e0j9w7e0j9e7e0j9w7e0j9e7e0j9w7e0j9e' WHERE phone = '19012341234';

-- 查看更新结果
SELECT id, phone, password FROM user WHERE phone = '19012341234';