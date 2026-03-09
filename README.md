# 外卖系统项目

## 项目简介

这是一个基于前后端分离架构的外卖系统，包含前端用户端、后端商家端和平台管理端三个部分。

### 主要功能

- **前端用户端**：浏览店铺、查看商品、购物车管理、订单提交、支付等
- **后端商家端**：订单管理、商品管理、店铺管理、提现管理等
- **平台管理端**：店铺审核、用户管理、财务管理、数据统计等

## 技术栈

### 前端
- Vue 2.6.14
- Vue Router 3.5.1
- Element UI 2.15.14
- Vant 2.12.40
- Axios 1.13.2
- ECharts 6.0.0
- Jest 30.2.0（测试）

### 后端
- Spring Boot 3.1.2
- MyBatis Plus 3.5.3.1
- MySQL 数据库
- Spring Security
- JWT 身份认证
- WebSocket 实时通信

## 项目结构

```
├── src/                # 后端源代码
│   ├── main/java/      # Java 代码
│   └── main/resources/ # 配置文件
├── vue/                # 前端项目
│   ├── src/            # 前端源代码
│   ├── public/         # 静态资源
│   └── tests/          # 前端测试
├── database/           # 数据库相关文件
└── sql/                # SQL 脚本
```

## 环境要求

### 前端
- Node.js 16.0+
- npm 7.0+

### 后端
- JDK 17+
- Maven 3.6+
- MySQL 5.7+

## 安装与运行

### 1. 数据库配置

1. 创建数据库：
   ```sql
   CREATE DATABASE takeout_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 导入数据库脚本：
   - 执行 `database/01_create_tables.sql` 创建表结构
   - 执行 `database/02_insert_data.sql` 插入初始数据

### 2. 后端运行

1. 配置数据库连接：
   修改 `src/main/resources/application.yml` 中的数据库连接信息

2. 启动后端服务：
   ```bash
   mvn spring-boot:run
   ```

   或使用 IDE 运行 `ReggieApplication.java`

### 3. 前端运行

1. 安装依赖：
   ```bash
   cd vue
   npm install
   ```

2. 启动开发服务器：
   ```bash
   npm run serve
   ```

3. 构建生产版本：
   ```bash
   npm run build
   ```

## 访问地址

- 前端：http://localhost:81
- 后端 API：http://localhost:8080

## 测试账号

### 前端用户
- 手机号：13800138000
- 密码：123456

### 商家账号
- 用户名：admin
- 密码：123456

### 平台账号
- 用户名：platform
- 密码：123456

## 项目特点

1. **前后端分离**：前端使用 Vue 框架，后端使用 Spring Boot，通过 API 进行通信
2. **多端适配**：前端支持移动端和 PC 端
3. **权限管理**：基于 JWT 和 Spring Security 的权限控制
4. **实时通信**：使用 WebSocket 实现订单状态实时更新
5. **数据可视化**：使用 ECharts 实现数据统计和分析

## 项目维护

### 代码风格
- 前端：遵循 ESLint 规范
- 后端：遵循 Java 编码规范

### 测试
- 前端：使用 Jest 进行单元测试
- 后端：使用 JUnit 5 进行单元测试

### 部署
- 前端：构建后部署到 Nginx 或其他静态文件服务器
- 后端：打包为 jar 包部署到服务器

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有问题或建议，欢迎联系项目维护者。