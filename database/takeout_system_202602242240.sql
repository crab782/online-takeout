-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: takeout_system
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '活动描述',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动图片',
  `status` tinyint DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `store_id` bigint DEFAULT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'新用户专享优惠','新用户注册即送50元优惠券','https://example.com/activity1.jpg',1,1,'2026-01-01 00:00:00','2026-12-31 23:59:59','2026-02-13 00:55:47','2026-02-20 21:05:07'),(2,'限时秒杀','每日10点限时秒杀，低至5折','https://example.com/activity2.jpg',1,1,'2026-01-01 00:00:00','2026-12-31 23:59:59','2026-02-13 00:55:47','2026-02-20 21:05:07'),(3,'满减活动','满100减20，满200减50','https://example.com/activity3.jpg',1,1,'2026-01-01 00:00:00','2026-12-31 23:59:59','2026-02-13 00:55:47','2026-02-20 21:05:07');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address_book`
--

DROP TABLE IF EXISTS `address_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `store_id` bigint DEFAULT NULL,
  `consignee` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `sex` tinyint DEFAULT NULL COMMENT '性别（0-女，1-男）',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签（公司、家、学校）',
  `is_default` tinyint DEFAULT '0' COMMENT '是否默认（0-否，1-是）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_default` (`is_default`),
  KEY `idx_store_id` (`store_id`),
  CONSTRAINT `fk_address_book_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地址簿表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address_book`
--

LOCK TABLES `address_book` WRITE;
/*!40000 ALTER TABLE `address_book` DISABLE KEYS */;
INSERT INTO `address_book` VALUES (1,1,1,'张test1','13900139001',1,'110000','北京市','110100','北京市','110101','东城区','建国路88号1号楼test1室','家',1,'2026-02-13 00:55:47','2026-02-24 00:58:04'),(2,1,1,'张三','13900139001',1,'110000','北京市','110100','北京市','110101','东城区','建国路99号2号楼202室','公司',0,'2026-02-13 00:55:47','2026-02-20 21:05:07'),(3,2,NULL,'李四','13900139002',NULL,'310000',NULL,NULL,'上海市','310101',NULL,'test2路',NULL,1,'2026-02-24 11:44:29','2026-02-24 11:44:29'),(4,3,NULL,'王五','13900139003',NULL,'440000',NULL,NULL,'广州市','440118',NULL,'test3大道test3街test3号test3户',NULL,1,'2026-02-24 14:30:03','2026-02-24 14:30:03');
/*!40000 ALTER TABLE `address_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `type` tinyint DEFAULT NULL COMMENT '类型（1-菜品分类，2-套餐分类）',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `store_id` bigint DEFAULT NULL COMMENT '所属店铺ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_name_store` (`name`,`store_id`),
  KEY `idx_type` (`type`),
  KEY `idx_sort` (`sort`),
  KEY `idx_status` (`status`),
  KEY `idx_store_id` (`store_id`),
  CONSTRAINT `fk_category_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'汉堡',1,1,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1),(2,'炸鸡',1,2,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1),(3,'小食',1,3,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1),(4,'饮料',1,4,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1),(5,'套餐',2,5,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1),(6,'汉堡',1,1,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2),(7,'小食',1,2,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2),(8,'甜品',1,3,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2),(9,'饮料',1,4,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2),(10,'套餐',2,5,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2),(11,'咖啡',1,1,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3),(12,'茶饮',1,2,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3),(13,'轻食',1,3,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3),(14,'甜点',1,4,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3),(15,'披萨',1,1,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4),(16,'意面',1,2,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4),(17,'小食',1,3,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4),(18,'甜品',1,4,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4),(19,'套餐',2,5,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4),(20,'奶茶',1,1,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5),(21,'果茶',1,2,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5),(22,'纯茶',1,3,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5),(23,'小食',1,4,1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5),(24,'测试分类',1,1,1,'2026-02-20 21:07:04','2026-02-20 21:07:04',NULL,NULL,1);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `store_id` bigint NOT NULL COMMENT '店铺ID',
  `rating` tinyint DEFAULT '5' COMMENT '评分（1-5）',
  `images` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论图片（多个图片路径，用逗号分隔）',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `status` tinyint DEFAULT '0' COMMENT '状态（0-未回复，1-已回复）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_order` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_comment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=409 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品名称',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
  `status` tinyint DEFAULT '1' COMMENT '状态（0-停售，1-起售）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `store_id` bigint DEFAULT NULL COMMENT '所属店铺ID',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dish_name_store` (`name`,`store_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_store_id` (`store_id`),
  CONSTRAINT `fk_dish_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_dish_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=660 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

LOCK TABLES `dish` WRITE;
/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish` VALUES (1,'香辣鸡腿堡',1,19.00,'香辣鸡腿堡，外酥里嫩，香辣可口','https://example.com/kfc/burger1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(2,'奥尔良烤鸡腿堡',1,20.00,'奥尔良烤鸡腿堡，鲜嫩多汁','https://example.com/kfc/burger2.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(3,'原味鸡',2,12.00,'原味鸡，经典美味','https://example.com/kfc/chicken1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(4,'香辣鸡翅',2,13.00,'香辣鸡翅，外酥里嫩','https://example.com/kfc/wing1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(5,'薯条（大）',3,12.00,'薯条（大），金黄酥脆','https://example.com/kfc/fries1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(6,'蛋挞',3,8.00,'葡式蛋挞，香甜可口','https://example.com/kfc/tart1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(7,'可乐',4,9.00,'可口可乐，冰爽解渴','https://example.com/kfc/coke1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(8,'雪碧',4,9.00,'雪碧，清爽怡人','https://example.com/kfc/sprite1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,1,NULL),(9,'巨无霸',6,24.00,'巨无霸，双层牛肉，满足感十足','https://example.com/mcd/burger1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2,NULL),(10,'麦辣鸡腿堡',6,19.00,'麦辣鸡腿堡，香辣过瘾','https://example.com/mcd/burger2.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2,NULL),(11,'麦乐鸡',7,18.00,'麦乐鸡，外酥里嫩','https://example.com/mcd/nuggets1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2,NULL),(12,'薯条（中）',7,10.00,'薯条（中），金黄酥脆','https://example.com/mcd/fries1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2,NULL),(13,'麦旋风',8,15.00,'麦旋风，香浓美味','https://example.com/mcd/shake1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2,NULL),(14,'可乐',9,9.00,'可口可乐，冰爽解渴','https://example.com/mcd/coke1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,2,NULL),(15,'拿铁',11,32.00,'拿铁，香浓咖啡与丝滑牛奶的完美融合','https://example.com/sb/latte1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(16,'美式咖啡',11,25.00,'美式咖啡，纯正咖啡香','https://example.com/sb/americano1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(17,'卡布奇诺',11,32.00,'卡布奇诺，经典意式咖啡','https://example.com/sb/cappuccino1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(18,'抹茶星冰乐',12,35.00,'抹茶星冰乐，清爽冰爽','https://example.com/sb/matcha1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(19,'芒果西番莲果茶',12,30.00,'芒果西番莲果茶，果香浓郁','https://example.com/sb/fruittea1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(20,'芝士蛋糕',14,28.00,'芝士蛋糕，香甜可口','https://example.com/sb/cake1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(21,'可颂',13,18.00,'可颂，酥脆可口','https://example.com/sb/croissant1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,3,NULL),(22,'超级至尊披萨',16,89.00,'超级至尊披萨，多种配料，丰富美味','https://example.com/ph/pizza1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4,NULL),(23,'夏威夷风情披萨',16,79.00,'夏威夷风情披萨，清爽果香','https://example.com/ph/pizza2.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4,NULL),(24,'意大利肉酱面',17,45.00,'意大利肉酱面，经典意式风味','https://example.com/ph/pasta1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4,NULL),(25,'奶油蘑菇汤',18,18.00,'奶油蘑菇汤，香浓美味','https://example.com/ph/soup1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4,NULL),(26,'炸鸡翅',18,28.00,'炸鸡翅，外酥里嫩','https://example.com/ph/wing1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4,NULL),(27,'提拉米苏',19,32.00,'提拉米苏，经典意式甜点','https://example.com/ph/tiramisu1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,4,NULL),(28,'芝芝莓莓',21,28.00,'芝芝莓莓，新鲜草莓与芝士的完美结合','https://example.com/ht/milktea1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(29,'多肉葡萄',21,28.00,'多肉葡萄，果肉饱满，口感丰富','https://example.com/ht/milktea2.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(30,'满杯水果茶',22,32.00,'满杯水果茶，多种水果，清爽解渴','https://example.com/ht/fruittea1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(31,'满杯红柚茶',22,28.00,'满杯红柚茶，清新果香','https://example.com/ht/fruittea2.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(32,'纯绿茶',23,18.00,'纯绿茶，清香淡雅','https://example.com/ht/greentea1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(33,'珍珠奶茶',21,22.00,'珍珠奶茶，经典港式风味','https://example.com/ht/bobatea1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(34,'蛋挞',24,12.00,'葡式蛋挞，香甜可口','https://example.com/ht/tart1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(35,'蛋糕',24,18.00,'蛋糕，松软香甜','https://example.com/ht/cake1.jpg',1,'2026-02-13 00:55:47','2026-02-13 00:55:47',NULL,NULL,5,NULL),(36,'芝芝芒芒',21,29.00,'芝芝芒芒，新鲜芒果与芝士的完美结合','https://example.com/ht/milktea3.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(37,'芝芝桃桃',21,28.00,'芝芝桃桃，新鲜桃子与芝士的完美结合','https://example.com/ht/milktea4.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(38,'黑糖珍珠奶茶',21,24.00,'黑糖珍珠奶茶，经典台式风味','https://example.com/ht/milktea5.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(39,'抹茶奶茶',21,26.00,'抹茶奶茶，日式抹茶风味','https://example.com/ht/milktea6.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(40,'焦糖奶茶',21,24.00,'焦糖奶茶，香甜可口','https://example.com/ht/milktea7.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(41,'燕麦奶茶',21,25.00,'燕麦奶茶，健康营养','https://example.com/ht/milktea8.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(42,'红豆奶茶',21,23.00,'红豆奶茶，软糯香甜','https://example.com/ht/milktea9.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(43,'芋泥奶茶',21,25.00,'芋泥奶茶，细腻绵密','https://example.com/ht/milktea10.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(44,'满杯橙子茶',22,26.00,'满杯橙子茶，清新果香','https://example.com/ht/fruittea3.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(45,'满杯蓝莓茶',22,28.00,'满杯蓝莓茶，抗氧化','https://example.com/ht/fruittea4.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(46,'满杯猕猴桃茶',22,27.00,'满杯猕猴桃茶，维C丰富','https://example.com/ht/fruittea5.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(47,'满杯菠萝茶',22,25.00,'满杯菠萝茶，热带风情','https://example.com/ht/fruittea6.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(48,'满杯草莓茶',22,27.00,'满杯草莓茶，香甜可口','https://example.com/ht/fruittea7.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(49,'满杯西瓜茶',22,24.00,'满杯西瓜茶，清爽解渴','https://example.com/ht/fruittea8.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(50,'茉莉花茶',23,16.00,'茉莉花茶，清香淡雅','https://example.com/ht/greentea2.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(51,'乌龙茶',23,18.00,'乌龙茶，香气四溢','https://example.com/ht/greentea3.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(52,'红茶',23,16.00,'红茶，醇厚香甜','https://example.com/ht/greentea4.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(53,'普洱茶',23,20.00,'普洱茶，醇厚回甘','https://example.com/ht/greentea5.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(54,'鸡米花',24,15.00,'鸡米花，外酥里嫩','https://example.com/ht/snack1.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(55,'薯条',24,12.00,'薯条，金黄酥脆','https://example.com/ht/snack2.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(56,'鸡翅',24,18.00,'鸡翅，香辣可口','https://example.com/ht/snack3.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(57,'鸡柳',24,15.00,'鸡柳，鲜嫩多汁','https://example.com/ht/snack4.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(58,'洋葱圈',24,10.00,'洋葱圈，酥脆可口','https://example.com/ht/snack5.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL),(59,'上校鸡块',24,12.00,'上校鸡块，经典美味','https://example.com/ht/snack6.jpg',1,'2026-02-20 16:59:38','2026-02-20 16:59:38',NULL,NULL,5,NULL);
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt加密）',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `role` tinyint NOT NULL DEFAULT '2' COMMENT '角色（1-平台管理员，2-商家管理员）',
  `store_id` bigint DEFAULT NULL COMMENT '关联店铺ID（商家管理员必填）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0-禁用，1-正常）',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_employee_username_store` (`username`,`store_id`),
  KEY `idx_role` (`role`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_employee_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表（商家管理员和平台管理员）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'admin','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','系统管理员','13800138000','admin@takeout.com',1,NULL,1,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(2,'shop_admin1','$2a$10$vOntSFCJwS/ObxLQ375rmu7WFj6XlLCW8n8iTK.oW0Yqex/Re1v7S','肯德基管理员','13800138001','admin1@kfc.com',2,1,1,'2026-02-13 00:55:47','2026-02-20 10:29:21'),(3,'shop_admin2','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','麦当劳管理员','13800138002','admin2@mcdonalds.com',2,2,1,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(4,'shop_admin3','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','星巴克管理员','13800138003','admin3@starbucks.com',2,3,1,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(5,'shop_admin4','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','必胜客管理员','13800138004','admin4@pizzahut.com',2,4,1,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(6,'shop_admin5','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','喜茶管理员','13800138005','admin5@heytea.com',2,5,1,'2026-02-13 00:55:47','2026-02-13 00:55:47');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `dish_id` bigint DEFAULT NULL COMMENT '菜品ID',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐ID',
  `number` int DEFAULT NULL COMMENT '数量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜品图片',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_dish_id` (`dish_id`),
  KEY `idx_setmeal_id` (`setmeal_id`),
  CONSTRAINT `fk_order_detail_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_order_detail_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_detail_setmeal` FOREIGN KEY (`setmeal_id`) REFERENCES `setmeal` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1,'香辣鸡腿堡',1,1,NULL,1,19.00,NULL),(2,'薯条（大）',1,5,NULL,1,12.00,NULL),(3,'可乐',1,7,NULL,1,9.00,NULL),(4,'巨无霸',2,9,NULL,1,24.00,NULL),(5,'薯条（中）',2,11,NULL,1,10.00,NULL),(6,'可乐',2,14,NULL,1,9.00,NULL),(7,'拿铁',3,15,NULL,1,32.00,NULL),(8,'超级至尊披萨',4,22,NULL,1,89.00,NULL),(9,'芝芝莓莓',5,29,NULL,2,56.00,NULL),(10,'香辣鸡腿堡',6,1,NULL,2,38.00,NULL),(11,'奥尔良烤鸡腿堡',6,2,NULL,1,20.00,NULL),(12,'可乐',6,7,NULL,2,18.00,NULL),(13,'麦辣鸡腿堡',7,10,NULL,2,38.00,NULL),(14,'薯条（中）',7,11,NULL,1,10.00,NULL),(15,'可乐',7,14,NULL,1,9.00,NULL),(16,'拿铁',8,15,NULL,2,64.00,NULL),(17,'多肉葡萄',9,30,NULL,1,28.00,NULL),(18,'珍珠奶茶',9,34,NULL,1,22.00,NULL),(19,'夏威夷风情披萨',10,23,NULL,1,79.00,NULL),(20,'香辣鸡腿堡',11,1,NULL,1,19.00,NULL),(21,'原味鸡',11,3,NULL,1,12.00,NULL),(22,'可乐',11,7,NULL,1,9.00,NULL),(23,'拿铁',12,15,NULL,1,32.00,NULL),(24,'可颂',12,20,NULL,1,18.00,NULL),(25,'芝芝莓莓',13,29,NULL,2,56.00,NULL),(26,'超级至尊披萨',14,22,NULL,1,89.00,NULL),(27,'意大利肉酱面',14,24,NULL,1,45.00,NULL),(28,'巨无霸',15,9,NULL,2,48.00,NULL),(29,'可乐',15,14,NULL,1,9.00,NULL),(30,'奥尔良烤鸡腿堡',16,2,NULL,1,20.00,NULL),(31,'香辣鸡翅',16,4,NULL,1,13.00,NULL),(32,'芝芝莓莓',17,29,NULL,1,28.00,NULL),(33,'拿铁',18,15,NULL,1,32.00,NULL),(34,'可颂',18,20,NULL,1,18.00,NULL),(35,'巨无霸',19,9,NULL,1,24.00,NULL),(36,'薯条（中）',19,11,NULL,1,10.00,NULL),(37,'可乐',19,14,NULL,1,9.00,NULL),(38,'夏威夷风情披萨',20,23,NULL,1,79.00,NULL),(39,'香辣鸡腿堡',21,1,NULL,1,19.00,NULL),(40,'原味鸡',21,3,NULL,1,12.00,NULL),(41,'可乐',21,7,NULL,1,9.00,NULL),(42,'满杯水果茶',22,31,NULL,1,32.00,NULL),(43,'珍珠奶茶',22,34,NULL,1,22.00,NULL),(44,'拿铁',23,15,NULL,1,32.00,NULL),(45,'巨无霸',24,9,NULL,2,48.00,NULL),(46,'可乐',24,14,NULL,1,9.00,NULL),(47,'夏威夷风情披萨',25,23,NULL,1,79.00,NULL),(48,'香辣鸡腿堡',26,1,NULL,1,19.00,NULL),(49,'薯条（大）',26,5,NULL,1,12.00,NULL),(50,'可乐',26,7,NULL,1,9.00,NULL),(51,'拿铁',27,15,NULL,1,32.00,NULL),(52,'可颂',27,20,NULL,1,18.00,NULL),(53,'芝芝莓莓',28,29,NULL,2,56.00,NULL),(54,'巨无霸',29,9,NULL,1,24.00,NULL),(55,'薯条（中）',29,11,NULL,1,10.00,NULL),(56,'可乐',29,14,NULL,1,9.00,NULL),(57,'超级至尊披萨',30,22,NULL,1,89.00,NULL),(58,'意大利肉酱面',30,24,NULL,1,45.00,NULL),(59,'香辣鸡腿堡',31,1,NULL,1,19.00,NULL),(60,'原味鸡',31,3,NULL,1,12.00,NULL),(61,'可乐',31,7,NULL,1,9.00,NULL),(62,'多肉葡萄',32,30,NULL,1,28.00,NULL),(63,'珍珠奶茶',32,34,NULL,1,22.00,NULL),(64,'拿铁',33,15,NULL,1,32.00,NULL),(65,'巨无霸',34,9,NULL,2,48.00,NULL),(66,'可乐',34,14,NULL,1,9.00,NULL),(67,'夏威夷风情披萨',35,23,NULL,1,79.00,NULL),(68,'茉莉花茶',36,NULL,NULL,1,NULL,'https://example.com/ht/greentea2.jpg'),(69,'巨无霸',37,NULL,NULL,1,NULL,'https://example.com/mcd/burger1.jpg'),(70,'麦辣鸡腿堡',37,NULL,NULL,1,NULL,'https://example.com/mcd/burger2.jpg'),(71,'麦乐鸡',37,NULL,NULL,1,NULL,'https://example.com/mcd/nuggets1.jpg'),(72,'薯条（中）',37,NULL,NULL,1,NULL,'https://example.com/mcd/fries1.jpg'),(73,'麦旋风',37,NULL,NULL,1,NULL,'https://example.com/mcd/shake1.jpg'),(74,'可乐',37,NULL,NULL,1,NULL,'https://example.com/mcd/coke1.jpg'),(75,'超级至尊披萨',38,22,NULL,1,89.00,'https://example.com/ph/pizza1.jpg'),(76,'可颂',39,21,NULL,1,18.00,'https://example.com/sb/croissant1.jpg'),(77,'美式咖啡',39,16,NULL,1,25.00,'https://example.com/sb/americano1.jpg'),(78,'芝士蛋糕',39,20,NULL,1,28.00,'https://example.com/sb/cake1.jpg'),(79,'芒果西番莲果茶',39,19,NULL,1,30.00,'https://example.com/sb/fruittea1.jpg'),(80,'拿铁',39,15,NULL,1,32.00,'https://example.com/sb/latte1.jpg'),(81,'卡布奇诺',39,17,NULL,1,32.00,'https://example.com/sb/cappuccino1.jpg'),(82,'抹茶星冰乐',39,18,NULL,1,35.00,'https://example.com/sb/matcha1.jpg'),(83,'芝士蛋糕',40,20,NULL,1,28.00,'https://example.com/sb/cake1.jpg'),(84,'可颂',41,21,NULL,2,18.00,'https://example.com/sb/croissant1.jpg'),(85,'美式咖啡',41,16,NULL,1,25.00,'https://example.com/sb/americano1.jpg'),(86,'芝士蛋糕',41,20,NULL,1,28.00,'https://example.com/sb/cake1.jpg'),(87,'芒果西番莲果茶',41,19,NULL,1,30.00,'https://example.com/sb/fruittea1.jpg'),(88,'拿铁',41,15,NULL,1,32.00,'https://example.com/sb/latte1.jpg'),(89,'卡布奇诺',41,17,NULL,1,32.00,'https://example.com/sb/cappuccino1.jpg'),(90,'抹茶星冰乐',41,18,NULL,1,35.00,'https://example.com/sb/matcha1.jpg'),(91,'拿铁',42,15,NULL,2,32.00,'https://example.com/sb/latte1.jpg'),(92,'芒果西番莲果茶',42,19,NULL,5,30.00,'https://example.com/sb/fruittea1.jpg'),(93,'美式咖啡',43,16,NULL,1,25.00,'https://example.com/sb/americano1.jpg'),(94,'可颂',44,21,NULL,1,18.00,'https://example.com/sb/croissant1.jpg'),(95,'美式咖啡',44,16,NULL,1,25.00,'https://example.com/sb/americano1.jpg'),(96,'卡布奇诺',44,17,NULL,1,32.00,'https://example.com/sb/cappuccino1.jpg'),(97,'抹茶星冰乐',44,18,NULL,1,35.00,'https://example.com/sb/matcha1.jpg'),(98,'可颂',45,21,NULL,1,18.00,'https://example.com/sb/croissant1.jpg'),(99,'美式咖啡',45,16,NULL,1,25.00,'https://example.com/sb/americano1.jpg'),(100,'芝士蛋糕',45,20,NULL,1,28.00,'https://example.com/sb/cake1.jpg'),(101,'芒果西番莲果茶',45,19,NULL,1,30.00,'https://example.com/sb/fruittea1.jpg'),(102,'卡布奇诺',45,17,NULL,1,32.00,'https://example.com/sb/cappuccino1.jpg'),(103,'蛋挞',46,6,NULL,1,8.00,'https://example.com/kfc/tart1.jpg'),(104,'巨无霸',47,9,NULL,1,24.00,'https://example.com/mcd/burger1.jpg'),(105,'麦辣鸡腿堡',47,10,NULL,1,19.00,'https://example.com/mcd/burger2.jpg'),(106,'麦乐鸡',48,11,NULL,1,18.00,'https://example.com/mcd/nuggets1.jpg'),(107,'巨无霸',48,9,NULL,1,24.00,'https://example.com/mcd/burger1.jpg'),(108,'麦旋风',49,13,NULL,1,15.00,'https://example.com/mcd/shake1.jpg'),(109,'可乐',49,14,NULL,1,9.00,'https://example.com/mcd/coke1.jpg'),(110,'可乐',50,14,NULL,1,9.00,'https://example.com/mcd/coke1.jpg'),(111,'薯条（中）',50,12,NULL,1,10.00,'https://example.com/mcd/fries1.jpg'),(112,'可乐',51,14,NULL,3,9.00,'https://example.com/mcd/coke1.jpg'),(113,'薯条（中）',51,12,NULL,4,10.00,'https://example.com/mcd/fries1.jpg'),(114,'麦旋风',51,13,NULL,3,15.00,'https://example.com/mcd/shake1.jpg'),(115,'麦乐鸡',51,11,NULL,2,18.00,'https://example.com/mcd/nuggets1.jpg'),(116,'麦辣鸡腿堡',51,10,NULL,1,19.00,'https://example.com/mcd/burger2.jpg'),(117,'巨无霸',51,9,NULL,11,24.00,'https://example.com/mcd/burger1.jpg'),(118,'可颂',52,21,NULL,1,18.00,'https://example.com/sb/croissant1.jpg'),(119,'芒果西番莲果茶',53,19,NULL,1,30.00,'https://example.com/sb/fruittea1.jpg'),(120,'拿铁',53,15,NULL,1,32.00,'https://example.com/sb/latte1.jpg');
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `status` tinyint DEFAULT '0' COMMENT '订单状态（0-待处理，1-已完成，2-已取消）',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `store_id` bigint NOT NULL COMMENT '店铺ID',
  `address_book_id` bigint DEFAULT NULL COMMENT '地址簿ID',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `checkout_time` datetime DEFAULT NULL COMMENT '结账时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `pay_method` tinyint DEFAULT NULL COMMENT '支付方式（1-微信，2-支付宝）',
  `pay_status` tinyint DEFAULT '0' COMMENT '支付状态（0-未支付，1-已支付）',
  `receiver` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货地址',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_orders_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2026021400001',5,1,'测试用户1',1,1,'2026-02-14 11:30:00','2026-02-14 11:35:00','2026-02-14 11:32:00','肯德基',38.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','不要辣','2026-02-14 11:30:00','2026-02-14 12:15:00'),(2,'2026021400002',5,2,'测试用户2',2,NULL,'2026-02-14 12:15:00','2026-02-14 12:20:00','2026-02-14 12:17:00','麦当劳',43.00,2,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','多加冰','2026-02-14 12:15:00','2026-02-14 12:55:00'),(3,'2026021400003',5,3,'测试用户3',3,NULL,'2026-02-14 14:30:00','2026-02-14 14:32:00','2026-02-14 14:31:00','星巴克',32.00,1,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','少糖','2026-02-14 14:30:00','2026-02-14 14:45:00'),(4,'2026021400004',5,1,'测试用户1',4,NULL,'2026-02-14 18:45:00','2026-02-14 18:50:00','2026-02-14 18:47:00','必胜客',89.00,2,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','','2026-02-14 18:45:00','2026-02-14 19:30:00'),(5,'2026021400005',5,2,'测试用户2',5,NULL,'2026-02-14 20:00:00','2026-02-14 20:02:00','2026-02-14 20:01:00','喜茶',56.00,1,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','常温','2026-02-14 20:00:00','2026-02-14 20:20:00'),(6,'2026021500001',5,1,'测试用户1',1,1,'2026-02-15 10:30:00','2026-02-15 10:35:00','2026-02-15 10:32:00','肯德基',68.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','','2026-02-15 10:30:00','2026-02-15 11:15:00'),(7,'2026021500002',5,3,'测试用户3',2,NULL,'2026-02-15 12:00:00','2026-02-15 12:05:00','2026-02-15 12:02:00','麦当劳',52.00,2,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-15 12:00:00','2026-02-15 12:40:00'),(8,'2026021500003',5,2,'测试用户2',3,NULL,'2026-02-15 15:30:00','2026-02-15 15:32:00','2026-02-15 15:31:00','星巴克',64.00,1,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','两杯少糖','2026-02-15 15:30:00','2026-02-15 15:50:00'),(9,'2026021500004',5,1,'测试用户1',5,NULL,'2026-02-15 16:45:00','2026-02-15 16:47:00','2026-02-15 16:46:00','喜茶',44.00,2,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','去冰','2026-02-15 16:45:00','2026-02-15 17:10:00'),(10,'2026021500005',5,3,'测试用户3',4,NULL,'2026-02-15 19:00:00','2026-02-15 19:05:00','2026-02-15 19:02:00','必胜客',73.00,1,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-15 19:00:00','2026-02-15 19:45:00'),(11,'2026021600001',5,2,'测试用户2',1,NULL,'2026-02-16 11:00:00','2026-02-16 11:05:00','2026-02-16 11:02:00','肯德基',44.00,2,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','不要辣','2026-02-16 11:00:00','2026-02-16 11:40:00'),(12,'2026021600002',5,1,'测试用户1',3,1,'2026-02-16 14:15:00','2026-02-16 14:17:00','2026-02-16 14:16:00','星巴克',50.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','少糖','2026-02-16 14:15:00','2026-02-16 14:35:00'),(13,'2026021600003',5,3,'测试用户3',5,NULL,'2026-02-16 15:30:00','2026-02-16 15:32:00','2026-02-16 15:31:00','喜茶',56.00,2,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','常温','2026-02-16 15:30:00','2026-02-16 15:55:00'),(14,'2026021600004',5,2,'测试用户2',4,NULL,'2026-02-16 18:00:00','2026-02-16 18:05:00','2026-02-16 18:02:00','必胜客',128.00,1,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','','2026-02-16 18:00:00','2026-02-16 18:50:00'),(15,'2026021600005',5,1,'测试用户1',2,1,'2026-02-16 20:30:00','2026-02-16 20:35:00','2026-02-16 20:32:00','麦当劳',52.00,2,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','','2026-02-16 20:30:00','2026-02-16 21:10:00'),(16,'2026021700001',5,3,'测试用户3',1,NULL,'2026-02-17 12:00:00','2026-02-17 12:05:00','2026-02-17 12:02:00','肯德基',33.00,1,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-17 12:00:00','2026-02-17 12:40:00'),(17,'2026021700002',5,1,'测试用户1',5,1,'2026-02-17 15:00:00','2026-02-17 15:02:00','2026-02-17 15:01:00','喜茶',28.00,2,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','去冰','2026-02-17 15:00:00','2026-02-17 15:20:00'),(18,'2026021700003',5,2,'测试用户2',3,NULL,'2026-02-17 16:30:00','2026-02-17 16:32:00','2026-02-17 16:31:00','星巴克',50.00,1,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','少糖','2026-02-17 16:30:00','2026-02-17 16:50:00'),(19,'2026021700004',5,3,'测试用户3',2,NULL,'2026-02-17 18:45:00','2026-02-17 18:50:00','2026-02-17 18:47:00','麦当劳',43.00,2,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-17 18:45:00','2026-02-17 19:25:00'),(20,'2026021700005',5,1,'测试用户1',4,1,'2026-02-17 19:30:00','2026-02-17 19:35:00','2026-02-17 19:32:00','必胜客',79.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','','2026-02-17 19:30:00','2026-02-17 20:15:00'),(21,'2026021800001',5,1,'测试用户1',1,1,'2026-02-18 11:30:00','2026-02-18 11:35:00','2026-02-18 11:32:00','肯德基',44.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','不要辣','2026-02-18 11:30:00','2026-02-18 12:15:00'),(22,'2026021800002',5,2,'测试用户2',5,NULL,'2026-02-18 14:00:00','2026-02-18 14:02:00','2026-02-18 14:01:00','喜茶',60.00,2,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','去冰','2026-02-18 14:00:00','2026-02-18 14:25:00'),(23,'2026021800003',5,3,'测试用户3',3,NULL,'2026-02-18 15:45:00','2026-02-18 15:47:00','2026-02-18 15:46:00','星巴克',32.00,1,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','少糖','2026-02-18 15:45:00','2026-02-18 16:05:00'),(24,'2026021800004',5,1,'测试用户1',2,1,'2026-02-18 18:00:00','2026-02-18 18:05:00','2026-02-18 18:02:00','麦当劳',52.00,2,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','','2026-02-18 18:00:00','2026-02-18 18:40:00'),(25,'2026021800005',5,2,'测试用户2',4,NULL,'2026-02-18 19:30:00','2026-02-18 19:35:00','2026-02-18 19:32:00','必胜客',73.00,1,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','','2026-02-18 19:30:00','2026-02-18 20:15:00'),(26,'2026021900001',5,3,'测试用户3',1,NULL,'2026-02-19 12:15:00','2026-02-19 12:20:00','2026-02-19 12:17:00','肯德基',38.00,2,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-19 12:15:00','2026-02-19 12:55:00'),(27,'2026021900002',5,1,'测试用户1',3,1,'2026-02-19 14:30:00','2026-02-19 14:32:00','2026-02-19 14:31:00','星巴克',50.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','少糖','2026-02-19 14:30:00','2026-02-19 14:50:00'),(28,'2026021900003',5,2,'测试用户2',5,NULL,'2026-02-19 15:15:00','2026-02-19 15:17:00','2026-02-19 15:16:00','喜茶',56.00,2,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','常温','2026-02-19 15:15:00','2026-02-19 15:40:00'),(29,'2026021900004',5,3,'测试用户3',2,NULL,'2026-02-19 18:30:00','2026-02-19 18:35:00','2026-02-19 18:32:00','麦当劳',43.00,1,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-19 18:30:00','2026-02-19 19:10:00'),(30,'2026021900005',5,1,'测试用户1',4,1,'2026-02-19 20:00:00','2026-02-19 20:05:00','2026-02-19 20:02:00','必胜客',128.00,2,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','','2026-02-19 20:00:00','2026-02-19 20:50:00'),(31,'2026022000001',5,2,'测试用户2',1,NULL,'2026-02-20 11:00:00','2026-02-20 11:05:00','2026-02-20 11:02:00','肯德基',44.00,1,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','不要辣','2026-02-20 11:00:00','2026-02-20 11:40:00'),(32,'2026022000002',5,3,'测试用户3',5,NULL,'2026-02-20 14:45:00','2026-02-20 14:47:00','2026-02-20 14:46:00','喜茶',44.00,2,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','去冰','2026-02-20 14:45:00','2026-02-20 15:10:00'),(33,'2026022000003',5,1,'测试用户1',3,1,'2026-02-20 16:00:00','2026-02-20 16:02:00','2026-02-20 16:01:00','星巴克',32.00,1,1,'张三','北京市北京市东城区建国路88号1号楼101室','13900139001','少糖','2026-02-20 16:00:00','2026-02-20 16:20:00'),(34,'2026022000004',5,2,'测试用户2',2,NULL,'2026-02-20 18:15:00','2026-02-20 18:20:00','2026-02-20 18:17:00','麦当劳',52.00,2,1,'测试用户2','北京市北京市东城区建国路99号2号楼202室','13900139002','','2026-02-20 18:15:00','2026-02-20 18:55:00'),(35,'2026022000005',5,3,'测试用户3',4,NULL,'2026-02-20 19:45:00','2026-02-20 19:50:00','2026-02-20 19:47:00','必胜客',79.00,1,1,'测试用户3','北京市北京市东城区建国路100号3号楼303室','13900139003','','2026-02-20 19:45:00','2026-02-20 20:30:00'),(36,'1771865939989',5,1,NULL,5,NULL,NULL,NULL,NULL,'喜茶',16.00,NULL,0,'张test1','建国路88号1号楼test1室','13900139001',NULL,'2026-02-24 00:59:00','2026-02-24 14:02:45'),(37,'1771905034019',5,2,NULL,2,NULL,NULL,NULL,NULL,'麦当劳',95.00,NULL,0,'李四','test2路','13900139002',NULL,'2026-02-24 11:50:34','2026-02-24 14:02:46'),(38,'1771913280353',5,1,NULL,4,NULL,NULL,NULL,NULL,'必胜客',89.00,NULL,0,'张test1','建国路88号1号楼test1室','13900139001',NULL,'2026-02-24 14:08:00','2026-02-24 15:21:44'),(39,'1771919288672',6,3,NULL,3,NULL,NULL,NULL,NULL,'星巴克',200.00,NULL,0,'王五','test3大道test3街test3号test3户','13900139003',NULL,'2026-02-24 15:48:09','2026-02-24 15:53:59'),(40,'1771920379062',5,3,NULL,3,NULL,NULL,NULL,NULL,'星巴克',28.00,2,1,'王五','test3大道test3街test3号test3户','13900139003',NULL,'2026-02-24 16:06:19','2026-02-24 16:30:41'),(41,'1771921884365',5,3,NULL,3,NULL,NULL,NULL,NULL,'星巴克',218.00,1,1,'王五','test3大道test3街test3号test3户','13900139003',NULL,'2026-02-24 16:31:24','2026-02-24 16:37:45'),(42,'1771922323232',5,1,NULL,3,NULL,NULL,NULL,NULL,'星巴克',214.00,2,1,'张test1','建国路88号1号楼test1室','13900139001',NULL,'2026-02-24 16:38:43','2026-02-24 16:39:24'),(43,'1771922345266',5,3,NULL,3,NULL,NULL,NULL,NULL,'星巴克',25.00,2,1,'王五','test3大道test3街test3号test3户','13900139003',NULL,'2026-02-24 16:39:05','2026-02-24 16:39:21'),(44,'1771922403248',5,1,NULL,3,NULL,NULL,NULL,NULL,'星巴克',110.00,1,1,'张test1','建国路88号1号楼test1室','13900139001',NULL,'2026-02-24 16:40:03','2026-02-24 16:40:19'),(45,'1771922738872',5,1,NULL,3,NULL,NULL,NULL,NULL,'星巴克',133.00,2,1,'张test1','建国路88号1号楼test1室','13900139001',NULL,'2026-02-24 16:45:39','2026-02-24 16:46:11'),(46,'1771923561931',2,2,NULL,1,NULL,NULL,NULL,NULL,'肯德基',8.00,2,1,'李四','test2路','13900139002',NULL,'2026-02-24 16:59:22','2026-02-24 16:59:23'),(47,'1771923620119',5,2,NULL,2,NULL,NULL,NULL,NULL,'麦当劳',43.00,2,1,'李四','test2路','13900139002',NULL,'2026-02-24 17:00:20','2026-02-24 17:07:11'),(48,'1771924061584',5,2,NULL,2,NULL,NULL,NULL,NULL,'麦当劳',42.00,2,1,'李四','test2路','13900139002',NULL,'2026-02-24 17:07:42','2026-02-24 17:08:30'),(49,'1771924173768',5,2,NULL,2,NULL,NULL,NULL,NULL,'麦当劳',24.00,2,1,'李四','test2路','13900139002',NULL,'2026-02-24 17:09:34','2026-02-24 17:09:42'),(50,'1771924201985',0,2,NULL,2,NULL,NULL,NULL,NULL,'麦当劳',19.00,NULL,0,'李四','test2路','13900139002',NULL,'2026-02-24 17:10:02','2026-02-24 17:10:02'),(51,'1771924289370',5,3,NULL,2,NULL,NULL,NULL,NULL,'麦当劳',431.00,2,1,'王五','test3大道test3街test3号test3户','13900139003',NULL,'2026-02-24 17:11:29','2026-02-24 17:11:59'),(52,'1771932517252',5,2,NULL,3,NULL,NULL,NULL,NULL,'星巴克',18.00,2,1,'李四','test2路','13900139002',NULL,'2026-02-24 19:28:37','2026-02-24 19:57:01'),(53,'1771934337329',5,2,NULL,3,NULL,NULL,NULL,NULL,'星巴克',62.00,2,1,'李四','test2路','13900139002',NULL,'2026-02-24 19:58:57','2026-02-24 19:59:11');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setmeal`
--

DROP TABLE IF EXISTS `setmeal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setmeal` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `status` tinyint DEFAULT '1' COMMENT '状态（0-停售，1-起售）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `store_id` bigint DEFAULT NULL COMMENT '所属店铺ID',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_setmeal_name_store` (`name`,`store_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_store_id` (`store_id`),
  CONSTRAINT `fk_setmeal_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setmeal`
--

LOCK TABLES `setmeal` WRITE;
/*!40000 ALTER TABLE `setmeal` DISABLE KEYS */;
INSERT INTO `setmeal` VALUES (1,'5','单人套餐A',35.00,1,'香辣鸡腿堡+薯条（中）+可乐','https://example.com/kfc/setmeal1.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,1,'肯德基'),(2,'5','双人套餐A',68.00,1,'2个香辣鸡腿堡+2份薯条（中）+2杯可乐','https://example.com/kfc/setmeal2.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,1,'肯德基'),(3,'5','全家桶',128.00,1,'多种炸鸡组合，适合全家分享','https://example.com/kfc/setmeal3.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,1,'肯德基'),(4,'10','1+1随心配',13.90,1,'任选2款小食或饮料','https://example.com/mcd/setmeal1.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,2,'肯德基'),(5,'10','超值套餐',39.00,1,'巨无霸+薯条（中）+可乐','https://example.com/mcd/setmeal2.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,2,'肯德基'),(6,NULL,'下午茶套餐',50.00,1,'拿铁+可颂','https://example.com/sb/setmeal1.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,3,'肯德基'),(7,NULL,'咖啡套餐',60.00,1,'2杯拿铁+芝士蛋糕','https://example.com/sb/setmeal2.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,3,'肯德基'),(8,'20','超级至尊套餐',128.00,1,'超级至尊披萨+意大利肉酱面+炸鸡翅','https://example.com/ph/setmeal1.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,4,'肯德基'),(9,'20','情侣套餐',158.00,1,'2个披萨+2份意面+2份甜点','https://example.com/ph/setmeal2.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,4,'肯德基'),(10,NULL,'闺蜜套餐',50.00,1,'2杯芝芝莓莓+蛋挞','https://example.com/ht/setmeal1.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(11,NULL,'下午茶套餐',45.00,1,'满杯水果茶+蛋糕','https://example.com/ht/setmeal2.jpg','2026-02-13 00:55:47','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(12,NULL,'青春活力套餐',48.00,1,'芝芝莓莓+满杯水果茶+鸡米花','https://example.com/ht/setmeal3.jpg','2026-02-20 16:59:38','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(13,NULL,'闺蜜聚会套餐',88.00,1,'2杯芝芝芒芒+2杯多肉葡萄+2份鸡翅','https://example.com/ht/setmeal4.jpg','2026-02-20 16:59:38','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(14,NULL,'家庭分享套餐',128.00,1,'2杯芝芝桃桃+2杯满杯橙子茶+鸡米花+薯条+上校鸡块','https://example.com/ht/setmeal5.jpg','2026-02-20 16:59:38','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(15,NULL,'单人下午茶套餐',38.00,1,'珍珠奶茶+蛋挞+洋葱圈','https://example.com/ht/setmeal6.jpg','2026-02-20 16:59:38','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(16,NULL,'双人下午茶套餐',68.00,1,'2杯抹茶奶茶+2份蛋糕+鸡柳','https://example.com/ht/setmeal7.jpg','2026-02-20 16:59:38','2026-02-23 15:47:23',NULL,NULL,5,'肯德基'),(17,NULL,'夏日清爽套餐',58.00,1,'满杯西瓜茶+满杯菠萝茶+薯条+洋葱圈','https://example.com/ht/setmeal8.jpg','2026-02-20 16:59:38','2026-02-23 15:47:23',NULL,NULL,5,'肯德基');
/*!40000 ALTER TABLE `setmeal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setmeal_dish`
--

DROP TABLE IF EXISTS `setmeal_dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setmeal_dish` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `setmeal_id` bigint NOT NULL COMMENT '套餐ID',
  `dish_id` bigint NOT NULL COMMENT '菜品ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜品名称',
  `price` decimal(10,2) DEFAULT NULL COMMENT '菜品价格',
  `copies` int DEFAULT '1' COMMENT '份数',
  `sort` int DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_setmeal_id` (`setmeal_id`),
  KEY `idx_dish_id` (`dish_id`),
  CONSTRAINT `fk_setmeal_dish_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_setmeal_dish_setmeal` FOREIGN KEY (`setmeal_id`) REFERENCES `setmeal` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐菜品关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setmeal_dish`
--

LOCK TABLES `setmeal_dish` WRITE;
/*!40000 ALTER TABLE `setmeal_dish` DISABLE KEYS */;
/*!40000 ALTER TABLE `setmeal_dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_cart`
--

DROP TABLE IF EXISTS `shopping_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shopping_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `store_id` bigint DEFAULT NULL,
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  `dish_id` bigint DEFAULT NULL COMMENT '菜品ID',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐ID',
  `dish_flavor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '口味',
  `number` int NOT NULL COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shopping_cart_user_dish` (`user_id`,`dish_id`,`setmeal_id`,`dish_flavor`,`store_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_dish_id` (`dish_id`),
  KEY `idx_setmeal_id` (`setmeal_id`),
  KEY `idx_store_id` (`store_id`),
  CONSTRAINT `fk_shopping_cart_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_shopping_cart_setmeal` FOREIGN KEY (`setmeal_id`) REFERENCES `setmeal` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_shopping_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=378 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_cart`
--

LOCK TABLES `shopping_cart` WRITE;
/*!40000 ALTER TABLE `shopping_cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `shopping_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺地址',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '店铺简介',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺图片',
  `status` tinyint DEFAULT '1' COMMENT '营业状态（0-打烊，1-营业中）',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `open_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '营业开始时间',
  `close_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '营业结束时间',
  `delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '配送费',
  `min_order_amount` decimal(10,2) DEFAULT '0.00' COMMENT '起送金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,'肯德基','北京市朝阳区建国路88号','400-823-8230','肯德基（KFC），全球知名快餐连锁品牌，主营炸鸡、汉堡等西式快餐','https://example.com/images/kfc.jpg',1,1,'06:00','23:00',5.00,20.00,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(2,'麦当劳','北京市朝阳区建国路99号','400-920-0205','麦当劳（McDonald\'s），全球最大快餐连锁企业，主营汉堡、薯条等','https://example.com/images/mcdonalds.jpg',1,1,'06:00','23:00',5.00,20.00,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(3,'星巴克','北京市朝阳区建国路100号','400-820-6988','星巴克（Starbucks），全球知名咖啡连锁品牌，主营咖啡、茶饮等','https://example.com/images/starbucks.jpg',1,2,'07:00','22:00',3.00,15.00,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(4,'必胜客','北京市朝阳区建国路101号','400-812-3123','必胜客（Pizza Hut），全球知名披萨连锁品牌，主营披萨、意面等','https://example.com/images/pizzahut.jpg',1,1,'10:00','22:00',6.00,30.00,'2026-02-13 00:55:47','2026-02-13 00:55:47'),(5,'喜茶','北京市朝阳区建国路102号','400-888-8888','喜茶（HEYTEA），中国知名新式茶饮品牌，主营奶茶、果茶等','https://example.com/images/heytea.jpg',1,2,'09:00','22:00',4.00,10.00,'2026-02-13 00:55:47','2026-02-13 00:55:47');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_favorite`
--

DROP TABLE IF EXISTS `store_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `store_id` bigint NOT NULL COMMENT '店铺ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_store_favorite_user_store` (`user_id`,`store_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_store_id` (`store_id`),
  CONSTRAINT `fk_store_favorite_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_store_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_favorite`
--

LOCK TABLES `store_favorite` WRITE;
/*!40000 ALTER TABLE `store_favorite` DISABLE KEYS */;
INSERT INTO `store_favorite` VALUES (1,1,1,'2026-02-13 00:55:47'),(2,1,3,'2026-02-13 00:55:47'),(3,2,2,'2026-02-13 00:55:47'),(4,2,5,'2026-02-13 00:55:47'),(5,3,1,'2026-02-13 00:55:47'),(6,3,4,'2026-02-13 00:55:47');
/*!40000 ALTER TABLE `store_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt加密）',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `sex` tinyint DEFAULT NULL COMMENT '性别（0-女，1-男）',
  `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `status` tinyint DEFAULT '1' COMMENT '状态（0-禁用，1-正常）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_username` (`username`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='前端用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test1','$2a$10$mCypajnG86Xm/4/ZWDLGt.IXdu.2qo/LB457.UozU.A2Um1MNXozy','测试用户1','13900139001','test1@example.com',1,'110101199001011234','https://example.com/avatar1.jpg',1,'2026-02-13 00:55:47','2026-02-19 14:30:41'),(2,'test2','$2a$10$mCypajnG86Xm/4/ZWDLGt.IXdu.2qo/LB457.UozU.A2Um1MNXozy','测试用户2','13900139002','test2@example.com',0,'110101199002022345','https://example.com/avatar2.jpg',1,'2026-02-13 00:55:47','2026-02-19 15:39:59'),(3,'test3','$2a$10$mCypajnG86Xm/4/ZWDLGt.IXdu.2qo/LB457.UozU.A2Um1MNXozy','测试用户3','13900139003','test3@example.com',1,'110101199003033456','https://example.com/avatar3.jpg',1,'2026-02-13 00:55:47','2026-02-19 15:40:14');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_checkin`
--

DROP TABLE IF EXISTS `user_checkin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_checkin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `checkin_date` date NOT NULL COMMENT '签到日期',
  `checkin_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `points` int DEFAULT '1' COMMENT '获得积分',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_checkin_user_date` (`user_id`,`checkin_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_checkin_date` (`checkin_date`),
  CONSTRAINT `fk_user_checkin_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户签到表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_checkin`
--

LOCK TABLES `user_checkin` WRITE;
/*!40000 ALTER TABLE `user_checkin` DISABLE KEYS */;
INSERT INTO `user_checkin` VALUES (1,1,'2026-02-13','2026-02-13 00:55:47',1),(2,2,'2026-02-13','2026-02-13 00:55:47',1),(3,3,'2026-02-13','2026-02-13 00:55:47',1),(4,1,'2026-02-19','2026-02-19 15:30:02',1),(7,1,'2026-02-20','2026-02-20 21:14:41',1),(9,1,'2026-02-21','2026-02-21 00:01:06',1),(10,1,'2026-02-23','2026-02-23 16:55:03',1),(11,1,'2026-02-24','2026-02-24 14:27:12',1),(12,3,'2026-02-24','2026-02-24 16:18:28',1);
/*!40000 ALTER TABLE `user_checkin` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-24 22:40:18
