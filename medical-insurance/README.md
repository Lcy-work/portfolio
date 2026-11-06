# 东软医疗保险报销系统（复现）

面向医院与医保中心的报销业务系统，覆盖医保药品/诊疗项目/报销比例维护、医嘱处理、参保人员费用报销等核心业务。

> 说明：本项目为**复现项目**，原始参考来自另一团队（2025/07/17），用于学习企业级 Java 后端工程规范与分层架构。

## 技术栈

- 后端：SpringBoot 2.6.13 + MyBatis-Plus + MySQL 8.0 + Druid
- 前端：Vue（3 个独立模块）

## 系统结构

```
medical-insurance/
├── backend/                            # SpringBoot 后端（health_insurance）
│   └── src/main/java/com/cqupt/health_insurance/
│       ├── controller/  ├── service/  ├── mapper/  ├── dto/  ├── config/  └── utils/
├── frontend/
│   ├── health-manager/          # 医保管理端
│   ├── hospital-manager/        # 医院端
│   └── reimbursement-manager/   # 报销端
├── sql/                          # 建库建表脚本（12 张表）
└── docs/                          # 数据库设计文档
```

## 运行

### 环境要求
- JDK 8、Maven 3.6+
- MySQL 8.0
- Node.js 16+（前端）

### 1. 初始化数据库
导入 `sql/schema.sql`（已含建库 + 12 张表）：
```bash
mysql -uroot -p < sql/schema.sql
```

### 2. 启动后端（端口 8081）
```bash
cd backend
# 按需修改 src/main/resources/application.properties 中的数据库账号密码
#   spring.datasource.username=root
#   spring.datasource.password=改成你自己的密码
#   spring.datasource.url=jdbc:mysql://localhost:3306/health_insurance
mvn spring-boot:run
```
启动类 `com.cqupt.health_insurance.HealthInsuranceApplication`。

> 三个前端已在源码里把 `baseURL` 写死为 `http://localhost:8081`，所以后端端口固定 8081，不要改。

### 3. 启动前端（三个独立模块）
前端 baseURL 已指向后端 8081，无需配置代理，分别启动即可：
```bash
cd frontend/health-manager          && npm install && npm run serve
cd frontend/hospital-manager        && npm install && npm run serve
cd frontend/reimbursement-manager   && npm install && npm run serve
```
默认端口 8080；同时启动多个时 vue-cli 会自动顺延（8081 已被后端占用，可手动指定：`npm run serve -- --port 8082`）。

## 体现的工程规范

- 统一返回体 `ResultVo`、全局异常处理 `GlobalExceptionHandler`、CORS 跨域配置
- Druid 连接池、MyBatis-Plus 分页
- Controller → Service → Mapper 分层 + 面向接口编程
