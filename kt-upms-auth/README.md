# 介绍

基于SpringSecurity的一套权限校验脚手架，目的在于定义校验的基本接口以及规范


# 目录结构

```
├── kt-upms-auth-config   -- 配置管理
├── kt-upms-auth-core     -- 包含了权限校验的传输实体、接口以及登录用户上下文等
├── kt-upms-auth-security -- 供业务系统直接引用的安全依赖，系统引用后所有操作将会交由权限中心进行限制
```