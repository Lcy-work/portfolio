# 基于 AIGC 的轻量化医疗问答数字人 Web 系统

医疗科普问答系统，前端 Vue 单页 + 后端 FastAPI，接入 DeepSeek 大模型提供自然语言医疗问答。

## 技术栈

- 后端：FastAPI + DeepSeek API
- 前端：Vue 3 + Vite
- 大模型：DeepSeek（deepseek-chat）

## 运行

```bash
# 后端（先配置密钥）
cd backend
pip install -r requirements.txt
# 复制 .env.example 为 .env，填入 DEEPSEEK_API_KEY
uvicorn main:app --reload --port 8000

# 前端
cd frontend
npm install
npm run dev
```

## 安全说明

- API Key 通过环境变量 `DEEPSEEK_API_KEY` 读取，**不硬编码**（见 `.env.example`）
- `SYSTEM_PROMPT` 限制为医疗科普，禁止开具处方药，非诊疗场景提醒线下就医

## 文档

- 项目报告见 `docs/`（PDF）
- 完整演示视频见原目录（未上传仓库，约 10MB）
