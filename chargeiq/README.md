# ChargeIQ 新能源充电桩大数据分析平台

端到端的新能源充电桩大数据分析平台：原始 CSV 数据 → Hadoop MapReduce 分布式计算 → MySQL 入库 → 机器学习预测 → Flask + ECharts 可视化大屏。

## 技术栈

- 大数据：Hadoop + MapReduce（7 个分布式任务）
- 后端：Flask
- 数据库：MySQL
- 机器学习：scikit-learn（随机森林 + GridSearchCV 调参）
- 可视化：ECharts（暗色科技风大屏）

## 功能

- 7 类分析：电压电流、单体电压、小时 SOC、充电频率、充电时长分布、充电速率、性能指标
- 四个页面：Index / Dashboard / Monitor / Predict
- 三种机器学习预测：充电费用、剩余电量(SOC)、充电时长

## 目录结构

```
chargeiq/
├── hadoop-mapreduce/   # 7 个 MapReduce 任务（Java + Maven）
├── ml-models/          # scikit-learn 训练/预测脚本
├── flask-webapp/       # Flask 后端 + ECharts 前端
├── sql/                # MySQL 建库脚本（7 张表）
├── screenshots/        # 运行截图
└── docs/               # 打开方法、截图清单
```

## 运行

见 `docs/打开方法.md`。需要 Hadoop + MySQL + Python 环境（虚拟机部署，Ubuntu 24.04）。
