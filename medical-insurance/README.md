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
└── docs/                          # 数据库设计文档
```

## 体现的工程规范

- 统一返回体 `ResultVo`、全局异常处理 `GlobalExceptionHandler`、CORS 跨域配置
- Druid 连接池、MyBatis-Plus 分页
- Controller → Service → Mapper 分层 + 面向接口编程
