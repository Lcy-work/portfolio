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

## 运行

### 环境要求
- JDK 17、Maven 3.6+
- MySQL 8.0
- Node.js 20.19+ / 22.12+（前端，见各 `package.json` 的 `engines`）

### 1. 初始化数据库
```bash
# 1) 建库（后端 URL 已带 createDatabaseIfNotExist=true，会自动建 nep_db，也可手动建）
# 2) 导入表结构与数据（转储文件无 USE 语句，需显式指定目标库 nep_db）
mysql -uroot -p nep_db < database/nep_1780486289_1.sql
```

> 库名以 `nep_db` 为准（`application.yml`、`src/main/resources/schema.sql`、`data.sql` 三处一致）。转储文件头部 `Source Database: nep` 是旧库名，表结构相同，导入 `nep_db` 即可。

### 2. 启动后端（端口 8080，context-path `/nepm`）
```bash
cd backend
# 按需修改 src/main/resources/application.yml 中的数据库账号密码
#   spring.datasource.username=root / password=改成你自己的密码
mvn spring-boot:run        # Windows 也可用 mvnw.cmd spring-boot:run
```
启动类 `com.cqupt.NepBackendApplication`。接口文档（Knife4j）：`http://localhost:8080/nepm/doc.html`

### 3. 启动前端（四个独立模块，端口已定）
四个前端已在 `vite.config.js` / `vue.config.js` 里配好代理（`/nepm` → `http://localhost:8080`），分别启动：

| 模块 | 目录 | 命令 | 端口 |
|------|------|------|------|
| 公众举报端 | `frontend/neps` | `npm install && npm run serve` | 8081 |
| 管理员端 | `frontend/nepm` | `npm install && npm run dev` | 8082 |
| 网格员端 | `frontend/nepg/nepg` | `npm install && npm run dev` | 8083 |
| 决策者大屏端 | `frontend/nepv` | `npm install && npm run dev` | 8084 |

> 完整图文步骤见 `docs/项目运行手册 .docx`、演示见 `docs/项目运行视频.mp4`。

## 体现的工程能力

- 前后端分离架构设计
- Controller → Service → Mapper 分层 + 面向接口编程
- `@Transactional` 多表事务、JUnit/Mockito 测试
