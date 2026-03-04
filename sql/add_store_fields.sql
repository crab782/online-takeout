-- 添加销量和评分字段
ALTER TABLE store ADD COLUMN sales INT DEFAULT 0 COMMENT '月销量';
ALTER TABLE store ADD COLUMN rating DECIMAL(2,1) DEFAULT 5.0 COMMENT '店铺评分';

-- 更新店铺数据，添加随机销量和评分
UPDATE store SET sales = FLOOR(RAND() * 1000) + 100, rating = ROUND((RAND() * 1 + 4), 1);

-- 查看更新结果
SELECT id, name, sales, rating FROM store;