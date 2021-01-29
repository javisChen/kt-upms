# 介绍

KT-UPMS 通用用户权限管理系统

# 作用

基于RBAC（Role-Based Access Control ）模型下沉的通用权限体系系统，可满足98%的Web应用系统。 整体围绕着用户、角色、权限三个实体进行实现。控制粒度达到页面按钮级。

# 目录结构

```
├── asserts         -- 项目资源（sql等）
├── kt-upms-api     -- 负责系统内部api
├── kt-upms-dao     -- 数据访问层，负责和db做数据交互
├── kt-upms-manager -- 第三方服务聚合层，统一封装集成第三方服务的调用，比如微信、百度等
├── kt-upms-openapi -- 对外部系统提供能力
├── kt-upms-rpc     -- rpc封装（Thrift，Feign、ProtoBuf等） 
├── kt-upms-service -- 业务聚合层
├── kt-upms-start   -- 程序入口
└── pom.xml

```

# 系统设计简述

# 功能

- 用户管理
    - [x] 添加用户
    - [x] 编辑用户
    - [x] 查询用户列表
    - [x] 查看用户详情
    - [x] 修改用户状态

- 用户组管理
    - [x] 添加用户组
    - [x] 查看用户组列表
    - [x] 编辑用户组
    - [x] 查看用户组
    - [x] 禁用用户组
    - [x] 启用用户组
    - [x] 用户组添加或移除用户
    - [x] 查询用户组下的用户
    - [x] 用户组添加或移除角色
    - [x] 查询用户组下的角色

- 角色管理
    - [x] 添加角色
    - [x] 修改角色状态
    - [x] 编辑角色
    - [x] 查看角色
    - [x] 查询角色列表（加Redis）
    - [x] 分配权限

- 权限管理
    - [x] 修改权限状态
    - [x] 查看权限列表
    - [x] 查看单个权限

- 路由管理
    - [x] 新建路由
    - [x] 编辑路由
    - [x] 查看路由列表
    - [x] 修改路由状态
    - [x] 查看菜单
    - [ ] 路由层级修改
    - [ ] 获取树形菜单Json（加Redis）

- 页面管理
    - [x] 添加页面
    - [x] 删除页面
    - [x] 编辑页面
    - [x] 查看页面
    - [x] 页面元素管理

# 数据模型

## ER图

![](asserts/kt-upms-er.png)

## 表说明

| 表名  | 说明 |
| ----- | ---- |
| upms_user_group | 用户组表 |
| upms_user_group_user_rel | 用户组与用户关联表 |
| upms_user_group_role_rel | 用户组与角色关联表 |
| upms_user | 用户表 |
| upms_role | 角色表 |
| upms_user_role_rel | 用户角色关联表 |
| upms_permission | 权限表 |
| upms_permission_role_rel | 角色与权限联表 |
| upms_menu | 用户表 |
| upms_page | 页面表 |
| upms_page_element | 页面元素表 |
| upms_api | api表 |

# 主要技术选型

## 后端

| 表名  | 说明 |
| ----- | ---- |
| SpringBoot | web框架 |
| MybatisPlus | 数据层 |

## 前端