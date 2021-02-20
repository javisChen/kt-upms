# 介绍

## 有可能是最用心的分布式权限中心

# 作用

基于RBAC（Role-Based Access Control）模型下沉的通用权限体系系统，可满足98%的Web应用系统。 整体围绕着用户、角色、权限三个实体进行实现。控制粒度达到页面按钮级。

# 关键字
分布式、无感知、代码无入侵

# 目录结构

```
├── asserts         -- 项目资源（sql等）
├── kt-iam-auth    -- 认证模块
├── kt-iam-web     -- 权限系统运行模块
└── pom.xml

```

# 系统设计简述

# 数据模型

## ER图

![](asserts/kt-iam-er.png)

## 表说明

| 表名  | 说明 |
| ----- | ---- |
| iam_user_group | 用户组表 |
| iam_user_group_user_rel | 用户组与用户关联表 |
| iam_user_group_role_rel | 用户组与角色关联表 |
| iam_user | 用户表 |
| iam_role | 角色表 |
| iam_user_role_rel | 用户角色关联表 |
| iam_permission | 权限表 |
| iam_permission_role_rel | 角色与权限联表 |
| iam_route | 前端路由表 |
| iam_page_element | 页面元素表 |
| iam_api | api表 |
| iam_application | 应用表 |

# 主要技术选型

## 后端

| 组件  | 说明 |
| ----- | ---- |
| SpringBoot | web框架 |
| MybatisPlus | 数据层 |
| Redis | 缓存中间件 |

## 前端

| 组件  | 说明 |
| ----- | ---- |
| Vue | 前端框架 |