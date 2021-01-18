-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: kt
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `upms_api`
--

DROP TABLE IF EXISTS `upms_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_api` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `application_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '应用id，关联upms_application.id',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT 'API名称',
  `url` varchar(128) NOT NULL DEFAULT '' COMMENT '接口地址',
  `method` tinyint(1) DEFAULT '1' COMMENT 'Http Method 1-GET 2-POST 3-PUT 4-DELETE  5-PATCH',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态 1-已启用；2-已禁用；',
  `auth_type` tinyint(1) DEFAULT '1' COMMENT '认证授权类型 1-无需认证授权 2-只需认证无需授权 3-需要认证和授权',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`,`application_id`),
  UNIQUE KEY `uk_code` (`url`),
  KEY `uk_is_deleted` (`is_deleted`),
  KEY `fk_upms_api_upms_app1_idx` (`application_id`),
  KEY `uk_system_id` (`application_id`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='api表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_api`
--

LOCK TABLES `upms_api` WRITE;
/*!40000 ALTER TABLE `upms_api` DISABLE KEYS */;
INSERT INTO `upms_api` VALUES (1,2,'新建Api123','/api123',4,1,2,'2021-01-16 13:17:05','2021-01-16 13:17:05',0,0,1);
/*!40000 ALTER TABLE `upms_api` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_application`
--

DROP TABLE IF EXISTS `upms_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_application` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '应用名称',
  `code` varchar(50) NOT NULL DEFAULT '' COMMENT '应用编码',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态 1-已上线；2-已下线；',
  `type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '应用类型 1-业务系统（前后端）2-纯后台服务',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`,`is_deleted`),
  UNIQUE KEY `uk_name` (`name`,`is_deleted`),
  KEY `uk_is_deleted` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_application`
--

LOCK TABLES `upms_application` WRITE;
/*!40000 ALTER TABLE `upms_application` DISABLE KEYS */;
INSERT INTO `upms_application` VALUES (1,'权限中心','permission',1,1,'2021-01-16 12:59:41','2021-01-16 12:59:41',0,0,0),(2,'订单中心','order',1,1,'2021-01-16 13:24:51','2021-01-16 13:24:51',0,0,0),(3,'消息中心','message',1,1,'2021-01-16 13:25:10','2021-01-16 13:25:10',0,0,0);
/*!40000 ALTER TABLE `upms_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_page_element`
--

DROP TABLE IF EXISTS `upms_page_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_page_element` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` bigint unsigned NOT NULL COMMENT '所属菜单id，关联upms_menu.id',
  `name` varchar(45) NOT NULL COMMENT '元素名称',
  `type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '元素类型 1-按钮；2-层；',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`),
  KEY `idx_route_id` (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面元素表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_page_element`
--

LOCK TABLES `upms_page_element` WRITE;
/*!40000 ALTER TABLE `upms_page_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `upms_page_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_permission`
--

DROP TABLE IF EXISTS `upms_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(45) NOT NULL COMMENT '权限编码',
  `type` varchar(25) NOT NULL COMMENT '权限类型 FRONT_ROUTE-前端路由；PAGE_ELEMENT-页面元素；PAGE-页面；FILE-文件；SER_API-内部服务API；OPEN_API-开放API',
  `resource_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '资源id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '权限状态 1-已启用；2-已禁用；',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_resource_id` (`type`,`resource_id`,`gmt_create`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_permission`
--

LOCK TABLES `upms_permission` WRITE;
/*!40000 ALTER TABLE `upms_permission` DISABLE KEYS */;
INSERT INTO `upms_permission` VALUES (1,'FR00001','FRONT_ROUTE',1,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(2,'FR00002','FRONT_ROUTE',2,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(3,'FR00003','FRONT_ROUTE',3,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(4,'FR00004','FRONT_ROUTE',4,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(5,'FR00005','FRONT_ROUTE',5,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(6,'FR00006','FRONT_ROUTE',6,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(7,'FR00007','FRONT_ROUTE',7,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(8,'FR00008','FRONT_ROUTE',8,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(9,'FR00009','FRONT_ROUTE',9,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(10,'FR00010','FRONT_ROUTE',10,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(11,'FR00011','FRONT_ROUTE',11,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(12,'FR00012','FRONT_ROUTE',12,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(13,'FR00013','FRONT_ROUTE',13,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(14,'FR00014','FRONT_ROUTE',14,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(15,'FR00015','FRONT_ROUTE',15,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(16,'FR00016','FRONT_ROUTE',16,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(17,'FR00017','FRONT_ROUTE',17,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(18,'FR00018','FRONT_ROUTE',18,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(19,'FR00019','FRONT_ROUTE',19,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(20,'FR00020','FRONT_ROUTE',20,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(21,'FR00021','FRONT_ROUTE',21,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(22,'FR00022','FRONT_ROUTE',22,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(23,'FR00023','FRONT_ROUTE',23,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(24,'FR00024','FRONT_ROUTE',24,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(25,'FR00025','FRONT_ROUTE',25,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(26,'FR00026','FRONT_ROUTE',26,1,'2020-12-24 16:03:23','2020-12-24 16:03:23',0,0,0),(27,'FR00027','FRONT_ROUTE',27,1,'2020-12-24 16:03:23','2020-12-24 16:03:23',0,0,0),(28,'FR00028','FRONT_ROUTE',28,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(29,'FR00029','FRONT_ROUTE',29,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(30,'FR00030','FRONT_ROUTE',30,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(31,'FR00031','FRONT_ROUTE',31,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(32,'FR00032','FRONT_ROUTE',32,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(33,'FR00033','FRONT_ROUTE',33,1,'2020-12-24 18:28:38','2020-12-24 18:28:38',0,0,0),(34,'FR00035','FRONT_ROUTE',35,1,'2020-12-25 17:23:07','2020-12-25 17:23:07',0,0,0),(35,'FR00036','FRONT_ROUTE',36,1,'2020-12-25 17:23:15','2020-12-25 17:23:15',0,0,0),(36,'FR00037','FRONT_ROUTE',37,1,'2020-12-25 17:23:20','2020-12-25 17:23:20',0,0,0),(37,'FR00038','FRONT_ROUTE',38,1,'2020-12-25 17:23:24','2020-12-25 17:23:24',0,0,0),(38,'FR00039','FRONT_ROUTE',39,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(39,'FR00040','FRONT_ROUTE',40,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(40,'FR00041','FRONT_ROUTE',41,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(41,'FR00042','FRONT_ROUTE',42,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(42,'FR00043','FRONT_ROUTE',43,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(43,'FR00044','FRONT_ROUTE',44,1,'2020-12-25 17:41:40','2020-12-25 17:41:40',0,0,0),(44,'FR00045','FRONT_ROUTE',45,1,'2020-12-25 17:41:45','2020-12-25 17:41:45',0,0,0),(45,'FR00046','FRONT_ROUTE',46,1,'2020-12-29 14:52:49','2020-12-29 14:52:49',0,0,0),(46,'FR00047','FRONT_ROUTE',47,1,'2020-12-29 15:51:28','2020-12-29 15:51:28',0,0,0),(47,'FR00048','FRONT_ROUTE',48,1,'2020-12-29 15:52:58','2020-12-29 15:52:58',0,0,0),(48,'FR00049','FRONT_ROUTE',49,1,'2020-12-29 16:34:36','2020-12-29 16:34:36',0,0,0),(49,'FR00050','FRONT_ROUTE',50,1,'2020-12-29 16:58:00','2020-12-29 16:58:00',0,0,0),(50,'FR00051','FRONT_ROUTE',51,1,'2020-12-29 16:58:45','2020-12-29 16:58:45',0,0,0),(51,'FR00052','FRONT_ROUTE',52,1,'2020-12-29 17:01:27','2020-12-29 17:01:27',0,0,0),(52,'FR00053','FRONT_ROUTE',53,1,'2020-12-29 17:01:31','2020-12-29 17:01:31',0,0,0),(53,'FR00054','FRONT_ROUTE',54,1,'2020-12-29 17:05:29','2020-12-29 17:05:29',0,0,0),(54,'FR00055','FRONT_ROUTE',55,1,'2020-12-29 17:05:41','2020-12-29 17:05:41',0,0,0),(55,'FR00056','FRONT_ROUTE',56,1,'2020-12-29 17:05:50','2020-12-29 17:05:50',0,0,0),(56,'FR00057','FRONT_ROUTE',57,1,'2020-12-29 17:17:56','2020-12-29 17:17:56',0,0,0),(57,'FR00058','FRONT_ROUTE',58,1,'2020-12-29 17:19:17','2020-12-29 17:19:17',0,0,0),(58,'FR00059','FRONT_ROUTE',59,1,'2020-12-29 17:20:20','2020-12-29 17:20:20',0,0,0),(59,'FR00060','FRONT_ROUTE',60,1,'2020-12-29 17:21:08','2020-12-29 17:21:08',0,0,0),(60,'FR00061','FRONT_ROUTE',61,1,'2020-12-29 17:36:25','2020-12-29 17:36:25',0,0,0),(61,'FR00062','FRONT_ROUTE',62,1,'2020-12-29 17:36:40','2020-12-29 17:36:40',0,0,0),(62,'FR00063','FRONT_ROUTE',63,1,'2020-12-29 17:36:49','2020-12-29 17:36:49',0,0,0),(63,'FR00064','FRONT_ROUTE',64,1,'2020-12-29 18:11:17','2020-12-29 18:11:17',0,0,0),(64,'FR00065','FRONT_ROUTE',65,1,'2020-12-29 18:11:57','2020-12-29 18:11:57',0,0,0),(65,'FR00066','FRONT_ROUTE',66,1,'2020-12-29 18:30:10','2020-12-29 18:30:10',0,0,0),(66,'FR00067','FRONT_ROUTE',67,1,'2020-12-30 16:37:49','2020-12-30 16:37:49',0,0,0),(67,'FR00068','FRONT_ROUTE',68,1,'2020-12-30 16:40:51','2020-12-30 16:40:51',0,0,0),(68,'FR00069','FRONT_ROUTE',69,1,'2020-12-30 17:00:20','2020-12-30 17:00:20',0,0,0),(69,'FR00070','FRONT_ROUTE',70,1,'2020-12-30 17:00:33','2020-12-30 17:00:33',0,0,0),(70,'FR00071','FRONT_ROUTE',71,1,'2021-01-02 22:57:49','2021-01-02 22:57:49',0,0,0),(71,'FR00072','FRONT_ROUTE',72,1,'2021-01-04 22:44:21','2021-01-04 22:44:21',0,0,0),(72,'FR00073','FRONT_ROUTE',73,1,'2021-01-04 22:57:11','2021-01-04 22:57:11',0,0,0),(73,'FR00074','FRONT_ROUTE',74,1,'2021-01-07 10:33:37','2021-01-07 10:33:37',0,0,0),(74,'FR00034','FRONT_ROUTE',34,1,'2021-01-07 10:33:37','2021-01-07 10:33:37',0,0,0),(75,'FR00075','FRONT_ROUTE',75,1,'2021-01-16 12:59:00','2021-01-16 12:59:00',0,0,0),(76,'FR00076','FRONT_ROUTE',76,1,'2021-01-16 12:59:23','2021-01-16 12:59:23',0,0,0);
/*!40000 ALTER TABLE `upms_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_permission_role_rel`
--

DROP TABLE IF EXISTS `upms_permission_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_permission_role_rel` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `permission_id` bigint unsigned NOT NULL COMMENT '权限id，关联upms_permission.id',
  `role_id` bigint unsigned NOT NULL COMMENT '角色id，关联upms_role.id',
  `type` varchar(25) NOT NULL DEFAULT '' COMMENT '权限类型 FRONT_ROUTE-前端路由；PAGE_ELEMENT-页面元素；PAGE-页面；FILE-文件；SER_API-内部服务API；OPEN_API-开放API',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_id_role_id` (`permission_id`,`role_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色与权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_permission_role_rel`
--

LOCK TABLES `upms_permission_role_rel` WRITE;
/*!40000 ALTER TABLE `upms_permission_role_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `upms_permission_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_role`
--

DROP TABLE IF EXISTS `upms_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `name` varchar(45) NOT NULL COMMENT '用户名称',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '角色状态 1-已启用；2-已禁用；',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 1-表示删除；0-表示未删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`,`is_deleted`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_role`
--

LOCK TABLES `upms_role` WRITE;
/*!40000 ALTER TABLE `upms_role` DISABLE KEYS */;
INSERT INTO `upms_role` VALUES (1,'CTO',1,'2021-01-16 13:34:27','2021-01-16 13:34:27',0,0,0);
/*!40000 ALTER TABLE `upms_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_route`
--

DROP TABLE IF EXISTS `upms_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_route` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '应用id，关联upms_application.id',
  `name` varchar(45) NOT NULL COMMENT '路由标题',
  `code` varchar(64) NOT NULL DEFAULT '' COMMENT '路由编码',
  `component` varchar(64) NOT NULL DEFAULT '' COMMENT '组件名',
  `pid` int NOT NULL COMMENT '父级路由id',
  `level_path` varchar(45) NOT NULL DEFAULT '' COMMENT '路由层级路径，例如：0.1.2 代表该菜单是三级路由，上级路由的id是1,再上级的路由id是0',
  `level` int NOT NULL COMMENT '路由层级',
  `path` varchar(64) NOT NULL DEFAULT '' COMMENT '路由path',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '图标',
  `hide_children` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏子路由 0-否 1-是',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '路由类型 1：菜单路由 2：页面路由',
  `sequence` int NOT NULL DEFAULT '0' COMMENT '排序序号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '权限状态 1-已启用；2-已禁用；',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`,`application_id`),
  UNIQUE KEY `uk_name` (`name`,`is_deleted`),
  UNIQUE KEY `uk_code` (`code`,`is_deleted`),
  KEY `idx_is_deleted` (`is_deleted`),
  KEY `idx_pid` (`pid`),
  KEY `fk_idx_system_id` (`application_id`),
  KEY `idx_system_id` (`is_deleted`,`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='路由表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_route`
--

LOCK TABLES `upms_route` WRITE;
/*!40000 ALTER TABLE `upms_route` DISABLE KEYS */;
INSERT INTO `upms_route` VALUES (2,1,'个人页','account','RouteView',0,'2.',1,'','user',0,1,700,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(3,1,'结果页','result','PageView',0,'3.',1,'','check-circle-o',0,1,500,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(4,1,'详情页','profile','RouteView',0,'4.',1,'','profile',0,1,400,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(5,1,'列表页','list','RouteView',0,'5.',1,'','table',0,1,300,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(6,1,'仪表盘','dashboard','RouteView',0,'6.',1,'','dashboard',0,1,100,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(7,1,'异常页','exception','RouteView',0,'7.',1,'','warning',0,1,600,1,'2020-12-24 15:49:19','2020-12-24 15:49:19',0,0,0),(8,1,'工作台','workplace','Workplace',6,'6.8.',2,'/dashboard/workplace','',0,1,102,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(9,1,'分析页','Analysis','Analysis',6,'6.9.',2,'/dashboard/analysis/:pageNo([1-9]\\\\d*)?','',0,1,101,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(13,1,'查询表格','table-list','TableList',5,'5.13.',2,'/list/table-list/:pageNo([1-9]\\\\d*)?','',0,1,0,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(14,1,'标准列表','basic-list','StandardList',5,'5.14.',2,'/list/basic-list','',0,1,0,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(15,1,'卡片列表','card','CardList',5,'5.15.',2,'/list/card','',0,1,0,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(16,1,'搜索列表','search','SearchLayout',5,'5.16.',2,'/list/search','',0,1,0,1,'2020-12-24 15:58:43','2020-12-24 15:58:43',0,0,0),(17,1,'基础详情页','basic','ProfileBasic',4,'4.17.',2,'/profile/basic','',0,1,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(18,1,'高级详情页','advanced','ProfileAdvanced',4,'4.18.',2,'/profile/advanced','',0,1,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(19,1,'成功','success','ResultSuccess',3,'3.19.',2,'/result/success','',0,1,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(20,1,'失败','fail','ResultFail',3,'3.20.',2,'/result/fail','',0,1,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(22,1,'404','404','Exception404',7,'7.22.',2,'/exception/404','',0,1,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(23,1,'500','500','Exception500',7,'7.23.',2,'/exception/500','',0,1,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(24,1,'个人中心','center','AccountCenter',2,'2.24.',2,'/account/center','',0,2,0,1,'2020-12-24 16:02:53','2020-12-24 16:02:53',0,0,0),(25,1,'个人设置','settings','AccountSettings',2,'2.25.',2,'/account/settings','',1,1,0,1,'2020-12-24 16:02:53','2021-01-04 22:56:19',0,0,0),(26,1,'搜索列表（项目）','project','SearchProjects',16,'5.16.26.',2,'/list/search/project','',0,1,0,1,'2020-12-24 16:03:23','2020-12-24 16:03:23',0,0,0),(27,1,'搜索列表（应用）','application','SearchApplications',16,'5.16.27.',2,'/list/search/application','',0,1,0,1,'2020-12-24 16:03:23','2020-12-24 16:03:23',0,0,0),(28,1,'基本设置','BasicSettings','BasicSettings',25,'2.25.28.',3,'/account/settings/basic','',0,2,0,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(29,1,'安全设置','SecuritySettings','SecuritySettings',25,'2.25.29.',3,'/account/settings/security','',0,2,0,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(30,1,'个性化设置','CustomSettings','CustomSettings',25,'2.25.30.',3,'/account/settings/custom','',0,2,0,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(32,1,'新消息通知','NotificationSettings','NotificationSettings',25,'2.25.32.',3,'/account/settings/notification','',0,2,0,1,'2020-12-24 16:13:50','2020-12-24 16:13:50',0,0,0),(34,1,'搜索列表（文章）','article','SearchArticles',16,'5.16.27.',3,'/list/search/article','',0,1,0,1,'2020-12-24 23:55:30','2020-12-24 23:55:30',0,0,0),(35,1,'其他组件','other','PageView',0,'35.',1,'','slack',0,1,800,1,'2020-12-25 17:23:07','2020-12-25 17:23:07',0,0,0),(36,1,'IconSelector','TestIconSelect','EditList',35,'35.36.',2,'/other/icon-selector','tool',0,1,810,1,'2020-12-25 17:23:15','2020-12-25 17:23:15',0,0,0),(37,1,'业务布局','bizLayout','RouteView',35,'35.37.',2,'','layout',0,1,820,1,'2020-12-25 17:23:20','2020-12-25 17:23:20',0,0,0),(38,1,'树目录表格','TreeList','TreeList',37,'35.37.38.',3,'/other/list/tree-list','',0,1,821,1,'2020-12-25 17:23:24','2020-12-25 17:23:24',0,0,0),(39,1,'内联编辑表格','EditList','EditList',37,'35.37.39.',3,'/other/list/edit-table','',0,1,822,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(40,1,'权限列表','PermissionList','PermissionList',37,'35.37.40.',3,'/other/list/system-permission','',0,1,823,1,'2020-12-25 17:23:25','2021-01-04 22:54:33',0,0,0),(42,1,'角色列表','RoleList','RoleList',37,'35.37.42.',3,'/other/list/role-list','',0,1,825,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(43,1,'角色列表2','SystemRole','SystemRole',37,'35.37.43.',3,'/other/list/system-role','',0,1,826,1,'2020-12-25 17:23:25','2020-12-25 17:23:25',0,0,0),(44,1,'权限管理','permission','RouteView',0,'44.',1,'/permission','slack',0,1,1,1,'2020-12-25 17:41:40','2021-01-02 22:57:31',0,0,0),(45,1,'路由管理','permission:route','PermissionRoute',44,'44.45.',2,'/permission/route','',0,2,910,1,'2020-12-25 17:41:45','2021-01-03 16:18:16',0,0,0),(57,1,'表单页','form','RouteView',0,'57.',1,'','form',0,1,2,1,'2020-12-29 17:17:56','2021-01-03 16:29:31',0,0,0),(58,1,'高级表单','advanced-form','AdvanceForm',57,'57.58.',2,'/form/advanced-form','',0,1,0,1,'2020-12-29 17:19:17','2020-12-29 17:19:17',0,0,0),(59,1,'分步表单','step-form','StepForm',57,'57.59.',2,'/form/step-form','',0,1,0,1,'2020-12-29 17:20:20','2020-12-29 17:20:20',0,0,0),(60,1,'基础表单','basic-form','BasicForm',57,'57.60.',2,'/form/base-form','',0,1,0,1,'2020-12-29 17:21:08','2020-12-29 17:21:08',0,0,0),(70,1,'测试子路由','3213','',69,'6.70.',2,'','',0,1,0,1,'2020-12-30 17:00:33','2020-12-30 17:00:44',0,0,0),(71,1,'用户管理','permission:user','PermissionUser',44,'44.71.',2,'/permission/user','',0,2,0,1,'2021-01-02 22:57:49','2021-01-02 22:57:49',0,0,0),(72,1,'用户组管理','permission:usergroup','PermissionUserGroup',44,'44.72.',2,'/permission/usergroup','',0,2,0,1,'2021-01-04 22:44:21','2021-01-04 22:44:21',0,0,0),(73,1,'账号绑定','BindingSettings','BindingSettings',25,'2.25.73.',3,'/account/settings/binding','',0,1,0,1,'2021-01-04 22:57:11','2021-01-04 23:13:44',0,0,0),(74,1,'角色管理','permission:role','PermissionRole',44,'44.74.',2,'/permission/role','',0,2,0,1,'2021-01-07 10:33:37','2021-01-07 10:33:37',0,0,0),(75,1,'应用管理','permission:application','PermissionApplication',44,'44.75.',2,'/permission/application','',0,2,0,1,'2021-01-16 12:59:00','2021-01-16 12:59:00',0,0,0),(76,1,'接口管理','permission:interface','PermissionApi',44,'44.76.',2,'/permission/interface','',0,2,0,1,'2021-01-16 12:59:23','2021-01-16 12:59:23',0,0,0);
/*!40000 ALTER TABLE `upms_route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_user`
--

DROP TABLE IF EXISTS `upms_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '用户名称',
  `phone` char(11) NOT NULL COMMENT '手机号码',
  `password` varchar(64) NOT NULL COMMENT '用户密码',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '用户状态：1-已启用；2-已禁用；',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_user`
--

LOCK TABLES `upms_user` WRITE;
/*!40000 ALTER TABLE `upms_user` DISABLE KEYS */;
INSERT INTO `upms_user` VALUES (1,'Admin','18588686716','$2a$10$T2AIk/bsyNa3N5HbU0zgruqVLDbWZo02fGb7qbOrxA4hz0DWKI4kK',1,'2021-01-16 13:31:55','2021-01-16 13:31:55',0,0,0);
/*!40000 ALTER TABLE `upms_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_user_group`
--

DROP TABLE IF EXISTS `upms_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_user_group` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `pid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '父用户组id，关联id',
  `name` varchar(45) NOT NULL COMMENT '用户组名称',
  `level` int unsigned NOT NULL DEFAULT '1' COMMENT '用户组层级',
  `level_path` varchar(45) NOT NULL DEFAULT '' COMMENT '部门层级',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '用户组状态：1-已启用；2-已禁用；',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  `is_deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0-表示未删除 大于0-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_is_deleted` (`is_deleted`),
  KEY `idx_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_user_group`
--

LOCK TABLES `upms_user_group` WRITE;
/*!40000 ALTER TABLE `upms_user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `upms_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_user_group_role_rel`
--

DROP TABLE IF EXISTS `upms_user_group_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_user_group_role_rel` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_group_id` bigint unsigned NOT NULL COMMENT '用户组id，关联upms_user_group.id',
  `role_id` bigint unsigned NOT NULL DEFAULT '1' COMMENT '角色id，关联upms_role.id',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_group_id_role_id` (`user_group_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户组与角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_user_group_role_rel`
--

LOCK TABLES `upms_user_group_role_rel` WRITE;
/*!40000 ALTER TABLE `upms_user_group_role_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `upms_user_group_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_user_group_user_rel`
--

DROP TABLE IF EXISTS `upms_user_group_user_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_user_group_user_rel` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_group_id` bigint unsigned NOT NULL COMMENT '用户组名称，关联upms_user_group.id',
  `user_id` bigint unsigned NOT NULL DEFAULT '1' COMMENT '用户id，关联upms_user_id',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_group_id_user_id` (`user_group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户组与用户关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_user_group_user_rel`
--

LOCK TABLES `upms_user_group_user_rel` WRITE;
/*!40000 ALTER TABLE `upms_user_group_user_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `upms_user_group_user_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upms_user_role_rel`
--

DROP TABLE IF EXISTS `upms_user_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upms_user_role_rel` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_id` bigint unsigned NOT NULL COMMENT '用户id，关联upms_role.id',
  `role_id` bigint unsigned NOT NULL COMMENT '角色id，关联upms_role.id',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `modifier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id_role_id` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_user_role_rel`
--

LOCK TABLES `upms_user_role_rel` WRITE;
/*!40000 ALTER TABLE `upms_user_role_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `upms_user_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'kt'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-16 21:59:28
