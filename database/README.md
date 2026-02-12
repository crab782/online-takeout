# 外卖系统数据库结构说明文档

## 1. 数据库概述

### 1.1 数据库信息
- **数据库名称**: `takeout_system`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`
- **创建日期**: 2026-02-13

### 1.2 系统架构
外卖系统采用三层用户角色架构：
- **前端用户**: 普通消费者，浏览店铺、下单、评论
- **商家管理员**: 店铺管理者，管理菜品、订单
- **平台管理员**: 系统管理者，管理店铺、用户、数据统计

## 2. 数据表结构

### 2.1 核心业务表

#### 2.1.1 店铺表 (store)
**表说明**: 存储所有店铺的基本信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| name | VARCHAR | 100 | NO | - | 店铺名称 |
| address | VARCHAR | 255 | YES | NULL | 店铺地址 |
| phone | VARCHAR | 20 | YES | NULL | 联系电话 |
| description | TEXT | - | YES | NULL | 店铺简介 |
| image | VARCHAR | 255 | YES | NULL | 店铺图片 |
| status | TINYINT | - | NO | 1 | 营业状态（0-打烊，1-营业中） |
| category_id | BIGINT | - | YES | NULL | 分类ID |
| open_time | VARCHAR | 10 | YES | NULL | 营业开始时间 |
| close_time | VARCHAR | 10 | YES | NULL | 营业结束时间 |
| delivery_fee | DECIMAL | 10,2 | NO | 0.00 | 配送费 |
| min_order_amount | DECIMAL | 10,2 | NO | 0.00 | 起送金额 |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_status`
- INDEX: `idx_category_id`

#### 2.1.2 用户表 (user)
**表说明**: 存储前端用户信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| username | VARCHAR | 50 | YES | NULL | 用户名 |
| password | VARCHAR | 255 | NO | - | 密码（BCrypt加密） |
| name | VARCHAR | 50 | YES | NULL | 姓名 |
| phone | VARCHAR | 20 | NO | - | 手机号 |
| email | VARCHAR | 100 | YES | NULL | 邮箱 |
| sex | TINYINT | - | YES | NULL | 性别（0-女，1-男） |
| id_number | VARCHAR | 18 | YES | NULL | 身份证号 |
| avatar | VARCHAR | 255 | YES | NULL | 头像 |
| status | TINYINT | - | NO | 1 | 状态（0-禁用，1-正常） |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_phone`
- INDEX: `idx_username`
- INDEX: `idx_status`

#### 2.1.3 员工表 (employee)
**表说明**: 存储商家管理员和平台管理员信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| username | VARCHAR | 50 | NO | - | 用户名 |
| password | VARCHAR | 255 | NO | - | 密码（BCrypt加密） |
| name | VARCHAR | 50 | NO | - | 姓名 |
| phone | VARCHAR | 20 | YES | NULL | 手机号 |
| email | VARCHAR | 100 | YES | NULL | 邮箱 |
| role | TINYINT | - | NO | 2 | 角色（1-平台管理员，2-商家管理员） |
| store_id | BIGINT | - | YES | NULL | 关联店铺ID（商家管理员必填） |
| status | TINYINT | - | NO | 1 | 状态（0-禁用，1-正常） |
| created_at | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_username`
- INDEX: `idx_role`
- INDEX: `idx_store_id`
- INDEX: `idx_status`
- FOREIGN KEY: `fk_employee_store` -> `store(id)`

#### 2.1.4 分类表 (category)
**表说明**: 存储菜品和套餐分类信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| name | VARCHAR | 50 | NO | - | 分类名称 |
| type | TINYINT | - | YES | NULL | 类型（1-菜品分类，2-套餐分类） |
| sort | INT | - | NO | 0 | 排序 |
| status | TINYINT | - | NO | 1 | 状态（0-禁用，1-启用） |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| create_user | BIGINT | - | YES | NULL | 创建人 |
| update_user | BIGINT | - | YES | NULL | 修改人 |
| store_id | BIGINT | - | YES | NULL | 所属店铺ID |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_type`
- INDEX: `idx_sort`
- INDEX: `idx_status`
- INDEX: `idx_store_id`
- FOREIGN KEY: `fk_category_store` -> `store(id)`

#### 2.1.5 菜品表 (dish)
**表说明**: 存储各店铺的菜品信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| name | VARCHAR | 50 | NO | - | 菜品名称 |
| category_id | BIGINT | - | YES | NULL | 分类ID |
| price | DECIMAL | 10,2 | NO | - | 价格 |
| description | VARCHAR | 255 | YES | NULL | 描述 |
| image | VARCHAR | 255 | YES | NULL | 图片 |
| status | TINYINT | - | NO | 1 | 状态（0-停售，1-起售） |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| create_user | BIGINT | - | YES | NULL | 创建人 |
| update_user | BIGINT | - | YES | NULL | 修改人 |
| store_id | BIGINT | - | YES | NULL | 所属店铺ID |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_category_id`
- INDEX: `idx_status`
- INDEX: `idx_store_id`
- FOREIGN KEY: `fk_dish_category` -> `category(id)`
- FOREIGN KEY: `fk_dish_store` -> `store(id)`

#### 2.1.6 套餐表 (setmeal)
**表说明**: 存储各店铺的套餐信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| category_id | VARCHAR | 50 | YES | NULL | 分类ID |
| name | VARCHAR | 50 | NO | - | 套餐名称 |
| price | DECIMAL | 10,2 | NO | - | 价格 |
| status | TINYINT | - | NO | 1 | 状态（0-停售，1-起售） |
| description | VARCHAR | 255 | YES | NULL | 描述 |
| image | VARCHAR | 255 | YES | NULL | 图片 |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| create_user | BIGINT | - | YES | NULL | 创建人 |
| update_user | BIGINT | - | YES | NULL | 修改人 |
| store_id | BIGINT | - | YES | NULL | 所属店铺ID |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_category_id`
- INDEX: `idx_status`
- INDEX: `idx_store_id`
- FOREIGN KEY: `fk_setmeal_store` -> `store(id)`

### 2.2 订单相关表

#### 2.2.1 订单表 (orders)
**表说明**: 存储订单基本信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| number | VARCHAR | 50 | NO | - | 订单编号 |
| status | TINYINT | - | NO | 0 | 订单状态（0-待处理，1-已完成，2-已取消） |
| user_id | BIGINT | - | NO | - | 用户ID |
| user_name | VARCHAR | 50 | YES | NULL | 用户名 |
| store_id | BIGINT | - | NO | - | 店铺ID |
| store_name | VARCHAR | 100 | YES | NULL | 店铺名称 |
| amount | DECIMAL | 10,2 | NO | - | 订单金额 |
| pay_method | TINYINT | - | YES | NULL | 支付方式（1-微信，2-支付宝） |
| pay_status | TINYINT | - | NO | 0 | 支付状态（0-未支付，1-已支付） |
| receiver | VARCHAR | 50 | YES | NULL | 收货人 |
| address | VARCHAR | 255 | YES | NULL | 收货地址 |
| phone | VARCHAR | 20 | YES | NULL | 联系电话 |
| remark | VARCHAR | 255 | YES | NULL | 备注 |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_number`
- INDEX: `idx_user_id`
- INDEX: `idx_store_id`
- INDEX: `idx_status`
- INDEX: `idx_create_time`
- FOREIGN KEY: `fk_orders_user` -> `user(id)`
- FOREIGN KEY: `fk_orders_store` -> `store(id)`

#### 2.2.2 订单详情表 (order_detail)
**表说明**: 存储订单中的菜品/套餐详情

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| name | VARCHAR | 50 | YES | NULL | 名称 |
| order_id | BIGINT | - | NO | - | 订单ID |
| dish_id | BIGINT | - | YES | NULL | 菜品ID |
| setmeal_id | BIGINT | - | YES | NULL | 套餐ID |
| number | INT | - | YES | NULL | 数量 |
| amount | DECIMAL | 10,2 | YES | NULL | 金额 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_order_id`
- INDEX: `idx_dish_id`
- INDEX: `idx_setmeal_id`
- FOREIGN KEY: `fk_order_detail_order` -> `orders(id)`
- FOREIGN KEY: `fk_order_detail_dish` -> `dish(id)`
- FOREIGN KEY: `fk_order_detail_setmeal` -> `setmeal(id)`

#### 2.2.3 购物车表 (shopping_cart)
**表说明**: 存储用户购物车信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| name | VARCHAR | 50 | YES | NULL | 名称 |
| image | VARCHAR | 255 | YES | NULL | 图片 |
| user_id | BIGINT | - | NO | - | 用户ID |
| dish_id | BIGINT | - | YES | NULL | 菜品ID |
| setmeal_id | BIGINT | - | YES | NULL | 套餐ID |
| dish_flavor | VARCHAR | 50 | YES | NULL | 口味 |
| number | INT | - | NO | - | 数量 |
| amount | DECIMAL | 10,2 | NO | - | 金额 |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_user_id`
- INDEX: `idx_dish_id`
- INDEX: `idx_setmeal_id`
- FOREIGN KEY: `fk_shopping_cart_user` -> `user(id)`
- FOREIGN KEY: `fk_shopping_cart_dish` -> `dish(id)`
- FOREIGN KEY: `fk_shopping_cart_setmeal` -> `setmeal(id)`

### 2.3 其他业务表

#### 2.3.1 套餐菜品关系表 (setmeal_dish)
**表说明**: 存储套餐与菜品的关联关系

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| setmeal_id | BIGINT | - | NO | - | 套餐ID |
| dish_id | BIGINT | - | NO | - | 菜品ID |
| name | VARCHAR | 50 | YES | NULL | 菜品名称 |
| price | DECIMAL | 10,2 | YES | NULL | 菜品价格 |
| copies | INT | - | NO | 1 | 份数 |
| sort | INT | - | NO | 0 | 排序 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_setmeal_id`
- INDEX: `idx_dish_id`
- FOREIGN KEY: `fk_setmeal_dish_setmeal` -> `setmeal(id)`
- FOREIGN KEY: `fk_setmeal_dish_dish` -> `dish(id)`

#### 2.3.2 地址簿表 (address_book)
**表说明**: 存储用户收货地址信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID |
| consignee | VARCHAR | 50 | NO | - | 收货人姓名 |
| phone | VARCHAR | 20 | NO | - | 手机号 |
| sex | TINYINT | - | YES | NULL | 性别（0-女，1-男） |
| province_code | VARCHAR | 12 | YES | NULL | 省级区划编号 |
| province_name | VARCHAR | 32 | YES | NULL | 省级名称 |
| city_code | VARCHAR | 12 | YES | NULL | 市级区划编号 |
| city_name | VARCHAR | 32 | YES | NULL | 市级名称 |
| district_code | VARCHAR | 12 | YES | NULL | 区级区划编号 |
| district_name | VARCHAR | 32 | YES | NULL | 区级名称 |
| detail | VARCHAR | 200 | YES | NULL | 详细地址 |
| label | VARCHAR | 32 | YES | NULL | 标签（公司、家、学校） |
| is_default | TINYINT | - | NO | 0 | 是否默认（0-否，1-是） |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_user_id`
- INDEX: `idx_is_default`
- FOREIGN KEY: `fk_address_book_user` -> `user(id)`

#### 2.3.3 评论表 (comment)
**表说明**: 存储用户订单评论信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| content | TEXT | - | NO | - | 评论内容 |
| user_id | BIGINT | - | NO | - | 用户ID |
| user_name | VARCHAR | 50 | YES | NULL | 用户名 |
| avatar | VARCHAR | 255 | YES | NULL | 用户头像 |
| order_id | BIGINT | - | NO | - | 订单ID |
| order_number | VARCHAR | 50 | YES | NULL | 订单编号 |
| store_id | BIGINT | - | NO | - | 店铺ID |
| rating | TINYINT | - | NO | 5 | 评分（1-5） |
| images | VARCHAR | 500 | YES | NULL | 评论图片（多个图片路径，用逗号分隔） |
| reply_content | TEXT | YES | NULL | 回复内容 |
| reply_time | DATETIME | YES | NULL | 回复时间 |
| status | TINYINT | - | NO | 0 | 状态（0-未回复，1-已回复） |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_user_id`
- INDEX: `idx_order_id`
- INDEX: `idx_store_id`
- INDEX: `idx_status`
- FOREIGN KEY: `fk_comment_user` -> `user(id)`
- FOREIGN KEY: `fk_comment_order` -> `orders(id)`
- FOREIGN KEY: `fk_comment_store` -> `store(id)`

#### 2.3.4 活动表 (activity)
**表说明**: 存储平台活动信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| title | VARCHAR | 100 | NO | - | 活动标题 |
| description | TEXT | YES | NULL | 活动描述 |
| image_url | VARCHAR | 255 | YES | NULL | 活动图片 |
| status | TINYINT | - | NO | 1 | 状态（0-禁用，1-启用） |
| start_time | DATETIME | YES | NULL | 开始时间 |
| end_time | DATETIME | YES | NULL | 结束时间 |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NO | CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**:
- PRIMARY KEY: `id`
- INDEX: `idx_status`

#### 2.3.5 店铺收藏表 (store_favorite)
**表说明**: 存储用户收藏店铺信息

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID |
| store_id | BIGINT | - | NO | - | 店铺ID |
| create_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 创建时间 |

**索引**:
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_user_store`
- INDEX: `idx_user_id`
- INDEX: `idx_store_id`
- FOREIGN KEY: `fk_store_favorite_user` -> `user(id)`
- FOREIGN KEY: `fk_store_favorite_store` -> `store(id)`

#### 2.3.6 用户签到表 (user_checkin)
**表说明**: 存储用户签到记录

| 字段名 | 类型 | 长度 | 允许NULL | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGINT | - | NO | AUTO_INCREMENT | 主键 |
| user_id | BIGINT | - | NO | - | 用户ID |
| checkin_date | DATE | - | NO | - | 签到日期 |
| checkin_time | DATETIME | - | NO | CURRENT_TIMESTAMP | 签到时间 |
| points | INT | - | NO | 1 | 获得积分 |

**索引**:
- PRIMARY KEY: `id`
- UNIQUE KEY: `uk_user_date`
- INDEX: `idx_user_id`
- INDEX: `idx_checkin_date`
- FOREIGN KEY: `fk_user_checkin_user` -> `user(id)`

## 3. 数据库关系图

### 3.1 核心关系

```
store (店铺)
  ├── employee (员工/管理员) [1:N]
  ├── category (分类) [1:N]
  ├── dish (菜品) [1:N]
  ├── setmeal (套餐) [1:N]
  ├── orders (订单) [1:N]
  └── comment (评论) [1:N]

user (前端用户)
  ├── orders (订单) [1:N]
  ├── shopping_cart (购物车) [1:N]
  ├── address_book (地址簿) [1:N]
  ├── comment (评论) [1:N]
  ├── store_favorite (店铺收藏) [1:N]
  └── user_checkin (签到记录) [1:N]

category (分类)
  ├── dish (菜品) [1:N]
  └── setmeal (套餐) [1:N]

orders (订单)
  └── order_detail (订单详情) [1:N]

setmeal (套餐)
  └── setmeal_dish (套餐菜品关系) [1:N]
```

### 3.2 业务关系说明

#### 3.2.1 平台与店铺关系
- **关系类型**: 一对多
- **说明**: 一个平台包含多个店铺
- **实现**: 通过 `store` 表的 `id` 字段关联

#### 3.2.2 店铺与店铺管理员关系
- **关系类型**: 一对一
- **说明**: 一个店铺对应一个店铺管理账号
- **实现**: 通过 `employee` 表的 `store_id` 字段关联，且 `role=2`

#### 3.2.3 店铺与菜品关系
- **关系类型**: 一对多
- **说明**: 一个店铺包含多个菜品
- **实现**: 通过 `dish` 表的 `store_id` 字段关联

#### 3.2.4 权限隔离
- **说明**: 各店铺管理员只能管理自己店铺的菜品
- **实现**: 在查询菜品时，通过 `dish.store_id` 与当前登录管理员的 `employee.store_id` 进行过滤

## 4. 初始数据

### 4.1 测试账号

#### 4.1.1 前端用户
| 用户名 | 密码 | 手机号 | 说明 |
|--------|------|--------|------|
| test1 | 123456 | 13900139001 | 测试用户1 |
| test2 | 123456 | 13900139002 | 测试用户2 |
| test3 | 123456 | 13900139003 | 测试用户3 |

#### 4.1.2 商家管理员
| 用户名 | 密码 | 店铺 | 说明 |
|--------|------|------|------|
| shop_admin1 | 123 | 肯德基 | 肯德基管理员 |
| shop_admin2 | 123 | 麦当劳 | 麦当劳管理员 |
| shop_admin3 | 123 | 星巴克 | 星巴克管理员 |
| shop_admin4 | 123 | 必胜客 | 必胜客管理员 |
| shop_admin5 | 123 | 喜茶 | 喜茶管理员 |

#### 4.1.3 平台管理员
| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 系统管理员 |

### 4.2 店铺数据

| 店铺ID | 店铺名称 | 地址 | 电话 | 营业时间 | 配送费 | 起送金额 |
|--------|----------|------|------|----------|--------|----------|
| 1 | 肯德基 | 北京市朝阳区建国路88号 | 400-823-8230 | 06:00-23:00 | 5.00 | 20.00 |
| 2 | 麦当劳 | 北京市朝阳区建国路99号 | 400-920-0205 | 06:00-23:00 | 5.00 | 20.00 |
| 3 | 星巴克 | 北京市朝阳区建国路100号 | 400-820-6988 | 07:00-22:00 | 3.00 | 15.00 |
| 4 | 必胜客 | 北京市朝阳区建国路101号 | 400-812-3123 | 10:00-22:00 | 6.00 | 30.00 |
| 5 | 喜茶 | 北京市朝阳区建国路102号 | 400-888-8888 | 09:00-22:00 | 4.00 | 10.00 |

### 4.3 菜品数据示例

#### 肯德基菜品
- 香辣鸡腿堡 (19.00元)
- 奥尔良烤鸡腿堡 (20.00元)
- 原味鸡 (12.00元)
- 香辣鸡翅 (13.00元)
- 薯条（大）(12.00元)
- 蛋挞 (8.00元)
- 可乐 (9.00元)
- 雪碧 (9.00元)

#### 麦当劳菜品
- 巨无霸 (24.00元)
- 麦辣鸡腿堡 (19.00元)
- 麦乐鸡 (18.00元)
- 薯条（中）(10.00元)
- 麦旋风 (15.00元)
- 可乐 (9.00元)

#### 星巴克菜品
- 拿铁 (32.00元)
- 美式咖啡 (25.00元)
- 卡布奇诺 (32.00元)
- 抹茶星冰乐 (35.00元)
- 芒果西番莲果茶 (30.00元)
- 芝士蛋糕 (28.00元)
- 可颂 (18.00元)

#### 必胜客菜品
- 超级至尊披萨 (89.00元)
- 夏威夷风情披萨 (79.00元)
- 意大利肉酱面 (45.00元)
- 奶油蘑菇汤 (18.00元)
- 炸鸡翅 (28.00元)
- 提拉米苏 (32.00元)

#### 喜茶菜品
- 芝芝莓莓 (28.00元)
- 多肉葡萄 (28.00元)
- 满杯水果茶 (32.00元)
- 满杯红柚茶 (28.00元)
- 纯绿茶 (18.00元)
- 珍珠奶茶 (22.00元)
- 蛋挞 (12.00元)
- 蛋糕 (18.00元)

## 5. 安全设计

### 5.1 密码加密
- **加密算法**: BCrypt
- **加密强度**: 10轮
- **存储方式**: 密码字段存储BCrypt加密后的哈希值
- **示例**:
  - 明文密码: `123456`
  - 加密后: `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`

### 5.2 权限隔离
- **前端用户**: 只能浏览店铺、下单、评论，无法管理店铺数据
- **商家管理员**: 只能管理自己店铺的菜品、订单、评论
- **平台管理员**: 可以管理所有店铺、用户、查看全局数据统计

### 5.3 外键约束
- 所有外键关系都设置了 `ON DELETE CASCADE` 或 `ON DELETE SET NULL`
- 确保数据一致性和完整性

## 6. 性能优化

### 6.1 索引设计
- 所有主键字段都建立了索引
- 所有外键字段都建立了索引
- 常用查询字段（如 `status`、`user_id`、`store_id`）都建立了索引

### 6.2 字符集选择
- 使用 `utf8mb4` 字符集，支持完整的Unicode字符集
- 包括emoji表情符号

### 6.3 时间字段
- 使用 `DATETIME` 类型存储时间
- 设置 `DEFAULT CURRENT_TIMESTAMP` 自动填充创建时间
- 设置 `ON UPDATE CURRENT_TIMESTAMP` 自动更新修改时间

## 7. 数据库初始化

### 7.1 初始化脚本
- **建表脚本**: `database/01_create_tables.sql`
- **数据插入脚本**: `database/02_insert_data.sql`
- **初始化脚本**: `database/init_database.ps1` (PowerShell版本)

### 7.2 执行步骤
1. 确保MySQL服务已启动
2. 确保root账号密码为 `123456`
3. 执行初始化脚本: `powershell -ExecutionPolicy Bypass -File database\init_database.ps1`

### 7.3 验证初始化
- 连接MySQL: `mysql -uroot -p123456`
- 选择数据库: `USE takeout_system;`
- 查看表: `SHOW TABLES;`
- 查看数据: `SELECT * FROM store;`

## 8. 附录

### 8.1 文件清单
```
database/
├── 01_create_tables.sql      # 建表脚本
├── 02_insert_data.sql        # 数据插入脚本
├── init_database.ps1         # PowerShell初始化脚本
├── init_database.bat         # Windows批处理脚本
└── README.md               # 数据库说明文档（本文件）
```

### 8.2 版本信息
- **数据库版本**: MySQL 8.0.45
- **创建日期**: 2026-02-13
- **文档版本**: v1.0

### 8.3 联系方式
如有问题，请联系开发团队。
