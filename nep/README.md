# NEP 环保公共监督系统

环保网格化监管平台，打通「公众举报 → 网格员核实 → 管理员调度 → 决策者分析」四个环节，实现环境问题从发现到处置的全流程闭环。

## 技术栈

- 后端：SpringBoot 3.5.14 + MyBatis-Plus + MySQL
- 前端：Vue 3（Composition API）+ Element Plus + ECharts（4 个独立前端）
- 测试：JUnit + Mockito

## 系统结构（1 后端 + 4 前端）

```
nep/
├── backend/       # SpringBoot 后端（nepBackend）
├── frontend/
│   ├── neps/      # 公众举报端
│   ├── nepg/      # 网格员端
│   ├── nepm/      # 管理员端
│   └── nepv/      # 决策者大屏端（ECharts）
├── database/      # 数据库脚本
└── docs/          # 运行手册 + 项目运行视频.mp4
```

## 体现的工程能力

- 前后端分离架构设计
- Controller → Service → Mapper 分层 + 面向接口编程
- `@Transactional` 多表事务、JUnit/Mockito 测试
