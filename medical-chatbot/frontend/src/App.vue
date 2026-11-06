<template>
  <div class="chat-wrapper">
    <div class="chat-title">医疗健康问答助手</div>
    <div class="chat-content" ref="chatRef">
      <div v-for="(item, index) in chatList" :key="index" class="chat-item" :class="item.type">
        <div class="chat-msg">{{ item.content }}</div>
      </div>
    </div>
    <div class="chat-input">
      <el-input
        v-model="inputText"
        placeholder="请输入您的健康问题..."
        style="flex:1;margin-right:10px"
        @keyup.enter="sendMsg"
      >
        <template #prefix>
          <el-button
            :type="isListening ? 'primary' : 'default'"
            icon="Microphone"
            @click="toggleListening"
          >{{ isListening ? '正在录音...' : '语音输入' }}</el-button>
        </template>
      </el-input>
      <el-button type="primary" @click="sendMsg">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 聊天记录、输入框内容
const chatList = ref([])
const inputText = ref('')
const chatRef = ref(null)

// 语音识别状态锁：防止重复启动报错
const isListening = ref(false)

// 全局语音合成实例，避免多次播报重叠
let speechSynthesis = window.speechSynthesis
let recognition = null

// 初始化浏览器语音识别
if (window.webkitSpeechRecognition) {
  recognition = new window.webkitSpeechRecognition()
  recognition.lang = 'zh-CN' // 中文识别
  recognition.continuous = false
  recognition.interimResults = false

  // 识别成功赋值
  recognition.onresult = (event) => {
    const text = event.results[0][0].transcript
    inputText.value = text
  }

  // 识别结束重置状态
  recognition.onend = () => {
    isListening.value = false
  }

  // 识别异常捕获
  recognition.onerror = (err) => {
    console.error('语音识别异常：', err)
    isListening.value = false
    ElMessage.error('语音识别失败，请检查麦克风或切换网络重试')
  }
} else {
  ElMessage.warning('当前浏览器不支持语音识别，请使用Chrome/Edge浏览器')
}

// 麦克风启停函数（防重复点击）
const toggleListening = () => {
  if (!recognition) return
  if (isListening.value) {
    recognition.stop()
    return
  }
  try {
    recognition.start()
    isListening.value = true
  } catch (err) {
    console.error('启动录音失败：', err)
    isListening.value = false
    ElMessage.error('录音启动失败，请刷新页面重试')
  }
}

// 文字转语音播报AI回复
const textToSpeak = (text) => {
  // 先停止正在播放的语音，防止多条语音重叠
  speechSynthesis.cancel()
  const utterance = new SpeechSynthesisUtterance(text)
  utterance.lang = 'zh-CN'
  utterance.rate = 1
  utterance.pitch = 1
  speechSynthesis.speak(utterance)
}

// 发送消息请求后端DeepSeek接口
const sendMsg = async () => {
  if (!inputText.value.trim()) {
    ElMessage.warning('请输入问题内容')
    return
  }
  const userText = inputText.value.trim()
  chatList.value.push({
    type: 'user',
    content: userText
  })
  inputText.value = ''

  try {
    const res = await axios.post('http://127.0.0.1:8000/chat', {
      message: userText
    })
    const replyText = res.data.response
    chatList.value.push({
      type: 'ai',
      content: replyText
    })
    // AI回复自动语音朗读
    textToSpeak(replyText)
  } catch (err) {
    console.error('请求接口失败：', err)
    const errorMsg = '接口异常，暂时无法回答，请稍后重试'
    chatList.value.push({
      type: 'ai',
      content: errorMsg
    })
    textToSpeak(errorMsg)
  }
}

// 聊天框自动滚动到底部
watch(chatList, () => {
  nextTick(() => {
    chatRef.value.scrollTop = chatRef.value.scrollHeight
  })
}, { deep: true })
</script>

<style scoped>
.chat-wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-sizing: border-box;
}
.chat-title {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}
.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 6px;
  margin-bottom: 20px;
}
.chat-item {
  margin: 12px 0;
  display: flex;
}
.chat-item.user {
  justify-content: flex-end;
}
.chat-item.ai {
  justify-content: flex-start;
}
.chat-msg {
  max-width: 60%;
  padding: 10px 14px;
  border-radius: 12px;
}
.user .chat-msg {
  background: #409eff;
  color: #fff;
}
.ai .chat-msg {
  background: #f5f7fa;
  color: #333;
}
.chat-input {
  display: flex;
  align-items: center;
}
</style>