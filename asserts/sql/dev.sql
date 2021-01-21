-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: kt
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
  UNIQUE KEY `uk_application_id_url_method` (`url`,`application_id`,`method`),
  KEY `idx_is_deleted` (`is_deleted`),
  KEY `idx_application_id` (`application_id`,`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='api表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_api`
--

LOCK TABLES `upms_api` WRITE;
/*!40000 ALTER TABLE `upms_api` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_application`
--

LOCK TABLES `upms_application` WRITE;
/*!40000 ALTER TABLE `upms_application` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_permission`
--

LOCK TABLES `upms_permission` WRITE;
/*!40000 ALTER TABLE `upms_permission` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_role`
--

LOCK TABLES `upms_role` WRITE;
/*!40000 ALTER TABLE `upms_role` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='路由表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_route`
--

LOCK TABLES `upms_route` WRITE;
/*!40000 ALTER TABLE `upms_route` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upms_user`
--

LOCK TABLES `upms_user` WRITE;
/*!40000 ALTER TABLE `upms_user` DISABLE KEYS */;
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

-- Dump completed on 2021-01-20 22:24:36
