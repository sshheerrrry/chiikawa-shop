-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: chiikawadb
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Current Database: `chiikawadb`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `chiikawadb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `chiikawadb`;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1vhvont0fdtramle6nmghntj7` (`user_id`,`product_id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK709eickf3kc0dujx3ub9i7btf` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_favorite_user_product` (`user_id`,`product_id`),
  UNIQUE KEY `UKgh1s14hhb9qb8p2do933hscsf` (`user_id`,`product_id`),
  KEY `fk_favorite_product` (`product_id`),
  CONSTRAINT `fk_favorite_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
INSERT INTO `favorites` VALUES (3,2,2,'2026-09-14 16:12:08'),(4,3,6,'2026-09-14 16:13:08'),(5,3,8,'2026-09-14 16:13:13'),(6,2,13,'2026-09-14 16:17:55'),(7,2,6,'2026-09-15 11:23:55'),(8,4,40,'2026-09-17 10:00:28'),(9,4,31,'2026-09-17 10:00:38'),(10,1,19,'2026-09-17 17:54:45');
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` decimal(10,2) NOT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` int NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,450.00,1,'天使－吉伊',1,1),(2,550.00,3,'天使惡魔－烏薩奇',1,2),(3,550.00,9,'樂園－烏薩奇',1,2),(4,499.00,5,'平行世界－小八',1,3),(5,550.00,6,'平行世界－烏薩奇',1,3),(6,499.00,11,'壽司－小八',1,4),(7,499.00,5,'平行世界－小八',1,5),(8,449.00,13,'餐廳－吉伊',1,5),(9,450.00,17,'Baby－吉伊',1,5),(10,550.00,18,'Baby－烏薩奇',1,5),(11,430.00,20,'餐廳－獅薩',1,6),(12,420.00,25,'Baby－小桃',1,6),(13,420.00,28,'麵包店－古本',1,6),(14,430.00,32,'壽司－小桃',1,6),(15,550.00,18,'Baby－烏薩奇',1,7),(16,460.00,35,'天使惡魔－師傅',1,8),(17,430.00,26,'Baby－獅薩',1,9),(18,450.00,21,'幽靈－吉伊',1,10),(19,499.00,22,'幽靈－小八',1,10),(20,550.00,23,'幽靈－烏薩奇',1,10),(21,450.00,31,'壽司－師傅',1,11),(22,599.00,40,'魔法－其他',1,11);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `total_amount` decimal(12,2) NOT NULL,
  `user_id` bigint NOT NULL,
  `payment_method` enum('ATM_TRANSFER','CASH_ON_DELIVERY','CREDIT_CARD') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recipient_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2026-09-09 16:54:50.462598',450.00,2,NULL,NULL,NULL,NULL),(2,'2026-09-13 15:47:50.414216',1100.00,3,NULL,NULL,NULL,NULL),(3,'2026-09-14 09:26:28.428284',1049.00,2,NULL,NULL,NULL,NULL),(4,'2026-09-15 12:09:25.859110',499.00,2,NULL,NULL,NULL,NULL),(5,'2026-09-15 14:10:04.232928',1948.00,3,NULL,NULL,NULL,NULL),(6,'2026-09-16 14:10:23.021801',1700.00,4,NULL,NULL,NULL,NULL),(7,'2026-09-16 17:22:18.762021',550.00,2,'CREDIT_CARD','0912345678','Alice','104 台北市中山區南京東路一段100號'),(8,'2026-09-16 17:32:12.796014',460.00,2,'CREDIT_CARD','0912345678','Alice','104 台北市中山區南京東路一段100號'),(9,'2026-09-16 17:54:07.610847',430.00,2,'ATM_TRANSFER','0912345678','Alice','104 台北市中山區南京東路一段100號'),(10,'2026-09-17 09:47:25.148206',1499.00,4,'CREDIT_CARD','0987654321','Leo','103 台北市大同區承德路一段70-1號15樓'),(11,'2026-09-17 17:29:14.847095',1049.00,4,'ATM_TRANSFER','0987654321','Leo','103 台北市大同區承德路一段70-1號15樓');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_reviews`
--

DROP TABLE IF EXISTS `product_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_review_user_product` (`user_id`,`product_id`),
  UNIQUE KEY `UKi8pvlswx9d9ul91orv429gxf` (`user_id`,`product_id`),
  KEY `fk_review_product` (`product_id`),
  CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `chk_review_rating` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_reviews`
--

LOCK TABLES `product_reviews` WRITE;
/*!40000 ALTER TABLE `product_reviews` DISABLE KEYS */;
INSERT INTO `product_reviews` VALUES (1,2,1,5,'實品很可愛，品質很好！','2026-09-14 17:44:05',NULL),(2,2,5,5,'毛茸茸超可愛！','2026-09-15 09:52:05',NULL),(3,2,6,5,'收到實體與照片相符！','2026-09-15 11:55:57',NULL),(4,3,5,5,'小八超可愛！','2026-09-15 14:11:06',NULL),(5,4,32,5,'','2026-09-17 09:47:38',NULL),(6,4,28,5,'我愛古本！','2026-09-17 09:48:04',NULL),(7,4,31,5,'海膽師傅超可愛！','2026-09-17 17:29:41',NULL);
/*!40000 ALTER TABLE `product_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `character_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `series` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `stock` int NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'吉伊','2026-09-09 16:39:56.000554','天使惡魔系列－吉伊','/images/d2220383-507b-4f77-b721-263ee3b68a03.jpg',450.00,'天使惡魔',9,_binary ''),(2,'小八','2026-09-09 16:39:56.007588','天使惡魔系列－小八','/images/91448ec3-b0a2-498d-8460-8e351d577ccf.jpg',499.00,'天使惡魔',10,_binary ''),(3,'烏薩奇','2026-09-09 16:39:56.014874','天使惡魔系列－烏薩奇','/images/ddb3fe02-3340-4488-8c34-556805b49fad.jpg',550.00,'天使惡魔',9,_binary ''),(4,'吉伊','2026-09-09 16:39:56.022916','平行世界系列－吉伊','/images/9c7d7ac1-b451-4d04-a26e-b2e463cf2ded.jpg',450.00,'平行世界',10,_binary ''),(5,'小八','2026-09-09 16:39:56.028943','平行世界系列－小八','/images/34958361-a72c-4984-94db-7cc8d1bde2b3.jpg',499.00,'平行世界',8,_binary ''),(6,'烏薩奇','2026-09-09 16:39:56.043020','平行世界系列－烏薩奇','/images/00fce9c6-e13e-47c9-a19e-53a4fb702c11.jpg',550.00,'平行世界',9,_binary ''),(7,'吉伊','2026-09-09 16:39:56.049429','樂園系列－吉伊','/images/c61584bd-17e3-45cf-bc44-cf535e469942.jpg',450.00,'樂園',10,_binary ''),(8,'小八','2026-09-09 16:39:56.057129','樂園系列－小八','/images/527ad7b2-01b8-48c7-bebf-c2c748bd877c.jpg',499.00,'樂園',10,_binary ''),(9,'烏薩奇','2026-09-09 16:39:56.064327','樂園系列－烏薩奇','/images/9a0262fe-5110-4e28-b5cf-ee13f705d6c9.jpg',550.00,'樂園',9,_binary ''),(10,'吉伊','2026-09-09 16:39:56.071067','壽司系列－吉伊','/images/e21bddd7-7977-460f-bd95-5553639c5bc9.jpg',450.00,'壽司',10,_binary ''),(11,'小八','2026-09-09 16:39:56.079533','壽司系列－小八','/images/01203231-0b4e-4f12-a03a-c78e6d0750de.jpg',499.00,'壽司',9,_binary ''),(12,'烏薩奇','2026-09-09 16:39:56.086114','壽司系列－烏薩奇','/images/4d838080-1f22-4f37-ac6b-2a83da6fb83a.jpg',550.00,'壽司',10,_binary ''),(13,'吉伊','2026-09-10 17:37:14.232514','餐廳系列－吉伊','/images/1e23676d-b37b-4a7a-8e32-ac0a732ded75.jpg',449.00,'餐廳',9,_binary ''),(15,'小八','2026-09-10 17:39:02.761293','餐廳系列－小八','/images/eb236cfb-d2ca-4593-aa36-0647f25f2d6d.jpg',499.00,'餐廳',10,_binary ''),(16,'烏薩奇','2026-09-10 20:34:01.313425','餐廳系列－烏薩奇','/images/afcd2459-e44a-4f85-8161-56f301abd2cd.jpg',550.00,'餐廳',10,_binary ''),(17,'吉伊','2026-09-13 15:56:15.000000','Baby系列－吉伊','/images/cb65b47e-e907-47bc-898b-36d519b68273.jpg',450.00,'Baby',9,_binary ''),(18,'烏薩奇','2026-09-13 15:57:00.000000','Baby系列－烏薩奇','/images/f2b38e08-4f74-4042-8600-aa96502c30d7.webp',550.00,'Baby',8,_binary ''),(19,'小八','2026-09-13 15:57:19.000000','Baby系列－小八','/images/8a4ada52-6fee-423f-962d-e97229e28fa6.jpg',499.00,'Baby',10,_binary ''),(20,'獅薩','2026-09-16 11:07:47.000000','餐廳系列－獅薩','/images/1c9eb3c9-7a7e-46d6-b961-734883395133.jpg',430.00,'餐廳',9,_binary ''),(21,'吉伊','2026-09-16 11:10:38.000000','幽靈系列－吉伊','/images/732df66f-afe7-4891-a0e7-4462bd45728b.jpg',450.00,'幽靈',9,_binary ''),(22,'小八','2026-09-16 11:10:57.000000','幽靈系列－小八','/images/743ede7d-8514-453a-959a-eaae4b2d5d8d.jpg',499.00,'幽靈',9,_binary ''),(23,'烏薩奇','2026-09-16 11:11:14.000000','幽靈系列－烏薩奇','/images/b0ca29f5-c40d-4256-bcd4-22f0a21a0e00.jpg',550.00,'幽靈',9,_binary ''),(24,'師傅','2026-09-16 11:15:47.000000','Baby系列－師傅','/images/8a264e70-c84a-4a71-b6ba-48b7177b7458.jpg',470.00,'Baby',10,_binary ''),(25,'小桃','2026-09-16 11:16:15.000000','Baby系列－小桃','/images/fca8f7a2-7f5e-4a7a-b1b6-12f5e20f78bf.jpg',420.00,'Baby',9,_binary ''),(26,'獅薩','2026-09-16 11:16:43.000000','Baby系列－獅薩','/images/e2b8eb29-034d-4c14-85ab-65d3e8152878.jpg',430.00,'Baby',9,_binary ''),(27,'小桃','2026-09-16 11:20:31.000000','麵包店系列－小桃','/images/f534b551-6ba0-4318-993b-3d6a031b3968.jpg',430.00,'麵包店',10,_binary ''),(28,'古本','2026-09-16 11:20:52.000000','麵包店系列－古本','/images/4ab1e264-ba94-4d8b-8e42-dca91dd82cab.jpg',420.00,'麵包店',9,_binary ''),(29,'古本','2026-09-16 11:22:05.000000','Baby系列－古本','/images/2a7d66bb-27f3-4ed4-9081-9ee280a534b8.jpg',410.00,'Baby',10,_binary ''),(30,'栗子饅頭','2026-09-16 11:25:07.000000','壽司系列－栗子饅頭','/images/e5bd8c15-404b-4d97-b33f-3803a43b46b5.jpg',440.00,'壽司',10,_binary ''),(31,'師傅','2026-09-16 11:25:26.000000','壽司系列－師傅','/images/771db0ce-c1ec-49b3-a659-47ea7d0a7ed7.jpg',450.00,'壽司',9,_binary ''),(32,'小桃','2026-09-16 11:26:08.000000','壽司系列－小桃','/images/51badf36-8323-4be4-b949-6a4d26a8e044.jpg',430.00,'壽司',9,_binary ''),(33,'古本','2026-09-16 11:26:24.000000','壽司系列－古本','/images/93bdba33-de70-401b-ad3e-e896ebb06ff1.jpg',420.00,'壽司',10,_binary ''),(34,'獅薩','2026-09-16 11:27:09.000000','壽司系列－獅薩','/images/2f766e15-c5ca-4134-8c7d-29c39cdce6ea.jpg',420.00,'壽司',10,_binary ''),(35,'師傅','2026-09-16 11:52:13.000000','天使惡魔系列－師傅','/images/7c73ab7d-b9f1-457c-bba3-eb79e8beaea5.jpg',460.00,'天使惡魔',9,_binary ''),(36,'其他','2026-09-16 11:58:04.000000','睡衣派對系列－小綠','/images/ec868773-45f7-488f-90cb-eca5e33b4763.jpg',399.00,'睡衣派對',10,_binary ''),(37,'其他','2026-09-16 11:58:36.000000','睡衣派對系列－小白','/images/9061d99a-b394-4001-abc9-07bcc4b6ad55.jpg',399.00,'睡衣派對',10,_binary ''),(38,'其他','2026-09-16 11:59:04.000000','睡衣派對系列－小紫','/images/c6f62ddb-40c2-484d-b658-0963de6ae2cf.jpg',399.00,'睡衣派對',10,_binary ''),(39,'其他','2026-09-16 11:59:42.000000','睡衣派對系列－小粉','/images/9184cacc-ff72-41a1-8bc2-d51c24996272.jpg',399.00,'睡衣派對',10,_binary ''),(40,'其他','2026-09-16 12:01:51.000000','魔法系列－小背包鎧甲先生','/images/de35f563-aeb5-4013-8783-96f590c029e6.jpg',599.00,'魔法',9,_binary '');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_addresses`
--

DROP TABLE IF EXISTS `user_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_default` bit(1) NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `postal_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recipient_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn2fisxyyu3l9wlch3ve2nocgp` (`user_id`),
  CONSTRAINT `FKn2fisxyyu3l9wlch3ve2nocgp` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_addresses`
--

LOCK TABLES `user_addresses` WRITE;
/*!40000 ALTER TABLE `user_addresses` DISABLE KEYS */;
INSERT INTO `user_addresses` VALUES (1,'南京東路一段100號','台北市','中山區',_binary '','0912345678','104','Alice',2),(4,'承德路一段70-1號15樓','台北市','大同區',_binary '','0987654321','103','Leo',4);
/*!40000 ALTER TABLE `user_addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','USER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-09-09 16:39:55.673973','admin@chiikawa.shop','管理員','$2a$10$2IIcEXt/5Go6/dTP/0ukAu5jlMmhl8vtWp0eVY6t9kJszpOor4/Ve','ADMIN','admin'),(2,'2026-09-09 16:42:32.812085','alice@example.com','Alice','$2a$10$1KooOO72mBfKKbh/VhiHruvc8DSNVec/HAibDcUhjf757osPTV0oK','USER','alice'),(3,'2026-09-13 15:46:39.221479','amy@gmail.com','Amy','$2a$10$U8Nq6iqoZbz0IBQh5LNLleVo4pQEwsDZouM0wXyiifETt7WiO7N1S','USER','amy'),(4,'2026-09-16 14:09:42.531873','leo@example.com','Leo','$2a$10$XWSByn/oJoQeDu/aWZJhFOG5otZVOj4cVrJPNe9nsrV8FaGDc5kUm','USER','leo');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'chiikawadb'
--

--
-- Dumping routines for database 'chiikawadb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-18  9:07:21
