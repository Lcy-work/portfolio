from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import os
import requests

# 从环境变量读取密钥（不再硬编码，公开仓库前已处理）
api_key = os.getenv("DEEPSEEK_API_KEY", "")

app = FastAPI(title="医疗数字人后端接口")

# 跨域配置
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class ChatRequest(BaseModel):
    message: str

SYSTEM_PROMPT = """你是专业医疗科普助手，语言简洁易懂，禁止开具处方药；用户如需诊疗，提醒线下就医。回答简短适合语音播报。"""

@app.post("/chat")
async def chat(request: ChatRequest):
    if not api_key:
        raise HTTPException(status_code=500, detail="DeepSeek API密钥未填写")

    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json"
    }
    data = {
        "model": "deepseek-chat",
        "messages": [
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": request.message}
        ],
        "temperature": 0.7
    }
    try:
        res = requests.post(
            "https://api.deepseek.com/v1/chat/completions",
            headers=headers,
            json=data,
            timeout=30
        )
        res.raise_for_status()
        result = res.json()
        reply = result["choices"][0]["message"]["content"]
        return {"response": reply}
    except Exception as e:
        print("DeepSeek调用详细异常：", str(e))
        if 'res' in locals():
            print("接口返回状态码：", res.status_code)
            print("接口返回内容：", res.text)
        raise HTTPException(status_code=500, detail=f"调用DeepSeek接口失败：{str(e)}")

if __name__ == "__main__":
    import uvicorn
    print("密钥来源：环境变量 DEEPSEEK_API_KEY")
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)