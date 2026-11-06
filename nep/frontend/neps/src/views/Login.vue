<!-- src/views/Login.vue -->
<template>
  <div class="wrapper">
    <header>
      <img src="@/assets/header.png" alt="环保主题插图">
    </header>

    <h1>东软环保公众监督平台</h1>
    <p class="subtitle">网格员端</p>

    <section>
      <!-- 实名/手机号 -->
      <div class="field" :class="{ 'field-error': inputError }">
        <div class="input-box">
          <el-icon class="input-icon"><User /></el-icon>
          <input
            type="text"
            v-model="displayInput"
            @input="onInput"
            @blur="onBlur"
            placeholder="请输入实名或手机号"
            maxlength="20"
          >
        </div>

        <p class="field-err" v-if="inputError">{{ inputError }}</p>
      </div>

      <!-- 密码 -->
      <div class="field">
        <div class="input-box">
          <el-icon class="input-icon"><Lock /></el-icon>
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="loginForm.password"
            placeholder="请输入登录密码（6-20位）"
          >
          <el-icon class="eye-icon" @click="showPassword = !showPassword">
            <component :is="showPassword ? View : Hide" />
          </el-icon>
        </div>
      </div>

      <!-- 记住密码 -->
      <div class="remember-box">
        <label class="checkbox-label">
          <input 
            type="checkbox" 
            v-model="rememberMe" 
            @change="handleRememberChange"
          >
          <span class="checkbox-text">记住密码</span>
        </label>
      </div>

      <!-- 登录按钮 -->
      <div class="button-box" @click="handleLogin">登 录</div>

      <!-- 底部链接 -->
      <div class="bottom-links">
        <span @click="goToRegister">注册账号</span>
        <span class="divider">|</span>
        <span @click="forgotPassword">忘记密码？</span>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref, inject, onMounted } from "vue";
import { useRouter } from "vue-router";
import { setSessionStorage, getSessionStorage } from "../common.js";
import { ElMessage } from 'element-plus';
import { User, Lock, View, Hide, UserFilled } from '@element-plus/icons-vue';

const router = useRouter();
const axios = inject("axios");

const displayInput = ref('');
const inputTouched = ref(false);
const inputError = ref('');
const showPassword = ref(false);
const rememberMe = ref(false);

const loginForm = reactive({ password: '' });

// 从 localStorage 加载记住的账号信息
onMounted(() => {
  const savedInfo = localStorage.getItem('savedLoginInfo');
  if (savedInfo) {
    try {
      const info = JSON.parse(savedInfo);
      // 优先使用手机号，如果没有则使用实名
      if (info.telId && info.password) {
        displayInput.value = formatPhone(info.telId);
        loginForm.telId = info.telId;
        loginForm.realName = info.realName || '';
        loginForm.password = info.password;
        rememberMe.value = true;
      } else if (info.realName && info.password) {
        displayInput.value = info.realName;
        loginForm.realName = info.realName;
        loginForm.telId = '';
        loginForm.password = info.password;
        rememberMe.value = true;
      }
    } catch (e) {
      console.error('加载保存的登录信息失败', e);
    }
  }
});

// 手机号格式化：XXX XXXX XXXX
const formatPhone = (value) => {
  const digits = value.replace(/\D/g, '').slice(0, 11);
  if (digits.length <= 3) return digits;
  if (digits.length <= 7) return digits.slice(0, 3) + ' ' + digits.slice(3);
  return digits.slice(0, 3) + ' ' + digits.slice(3, 7) + ' ' + digits.slice(7);
};

// 处理统一输入框的输入（自动识别手机号或实名）
const onInput = (e) => {
  const raw = e.target.value.trim();
  
  // 提取纯数字用于判断和存储
  const digits = raw.replace(/\s/g, '');
  
  // 自动判断是手机号还是实名
  if (/^1[3-9]\d{0,10}$/.test(digits)) {
    // 看起来像手机号（以1开头，后面跟数字）
    displayInput.value = formatPhone(digits);
    loginForm.telId = digits;
    loginForm.realName = '';
  } else {
    // 其他情况当作实名
    displayInput.value = raw;
    loginForm.realName = raw;
    loginForm.telId = '';
  }
  
  inputTouched.value = true;
  if (inputError.value) inputError.value = '';
};

// 处理输入框失去焦点
const onBlur = () => {
  if (!displayInput.value) { 
    inputError.value = ''; 
    return; 
  }
  
  // 验证：如果是手机号格式则验证手机号，否则验证实名
  if (/^1[3-9]\d{10}$/.test(displayInput.value.replace(/\s/g, ''))) {
    // 完整的11位手机号
    inputError.value = '';
  } else if (displayInput.value.length > 0) {
    // 有内容但不是完整手机号，当作实名
    inputError.value = '';
  }
};

// 处理记住密码复选框变化
const handleRememberChange = () => {
  if (!rememberMe.value) {
    // 取消记住密码时清除保存的信息
    localStorage.removeItem('savedLoginInfo');
  }
};

const goToRegister = () => router.push('/register');
const forgotPassword = () => ElMessage.info('请联系管理员重置密码');

const handleLogin = async () => {
  if (!displayInput.value) return ElMessage.warning('请输入实名或手机号');

  // 去掉空格后判断是手机号还是实名（displayInput 里手机号是带空格的 135  2745  6015）
  const rawInput = displayInput.value.replace(/\s/g, '');
  
  if (!loginForm.password) return ElMessage.warning('请输入密码');
  
  try {
    console.log('========== 开始登录 ==========');
    console.log('输入内容:', displayInput.value);
    console.log('处理后账号:', rawInput);
    console.log('密码长度:', loginForm.password.length);
    
    // 参考 nepg 项目的做法，使用统一的字段名传递
    // 无论是手机号还是实名，都作为 telId 传递
    const res = await axios.post('supervisor/getSupervisorByIdByPass', {
      telId: rawInput,  // 统一使用 telId 字段
      password: loginForm.password
    });
    
    console.log('后端响应状态:', res.status);
    console.log('后端响应头:', res.headers);
    console.log('后端完整响应:', JSON.stringify(res, null, 2));
    console.log('res.data 类型:', typeof res.data);
    console.log('res.data 内容:', res.data);
    
    // 兼容多种返回格式
    let successData = null;
    let errorMessage = '登录失败';
    
    if (res.data && typeof res.data === 'object') {
      // 标准格式：{ code: 200, message: '', data: {...} }
      if (res.data.code !== undefined) {
        console.log('检测到标准格式响应，code:', res.data.code);
        if (res.data.code === 200 || res.data.code === 1 || res.data.code === 0) {
          successData = res.data.data || res.data;
          console.log('提取到的用户数据:', successData);
        } else {
          errorMessage = res.data.message || `登录失败 (code: ${res.data.code})`;
        }
      } 
      // 直接返回用户对象
      else if (res.data.telId || res.data.realName || res.data.supervisorId) {
        console.log('检测到直接返回用户对象');
        successData = res.data;
      }
      // 检查是否有 success 字段
      else if (res.data.success !== undefined) {
        console.log('检测到 success 字段:', res.data.success);
        if (res.data.success) {
          successData = res.data.data || res.data;
        } else {
          errorMessage = res.data.message || '登录失败';
        }
      }
    } 
    // 返回影响行数（数字）
    else if (typeof res.data === 'number' && res.data > 0) {
      console.log('检测到返回影响行数:', res.data);
      successData = { affected: res.data };
    }
    
    console.log('最终 successData:', successData);
    console.log('最终 errorMessage:', errorMessage);
    console.log('========== 登录结束 ==========');
    
    if (successData) {
      setSessionStorage('supervisor', successData);
      
      // 如果勾选了记住密码，保存到 localStorage
      if (rememberMe.value) {
        localStorage.setItem('savedLoginInfo', JSON.stringify({
          telId: rawInput.match(/^1[3-9]\d{10}$/) ? rawInput : '',
          realName: rawInput.match(/^1[3-9]\d{10}$/) ? '' : rawInput,
          password: loginForm.password
        }));
      } else {
        localStorage.removeItem('savedLoginInfo');
      }
      
      ElMessage.success('登录成功');
      router.push('/selectGrid');
    } else {
      ElMessage.error(errorMessage);
    }
  } catch (e) {
    console.error('========== 登录异常 ==========');
    console.error('错误对象:', e);
    console.error('错误消息:', e.message);
    
    let errorMsg = '网络错误，请稍后重试';
    if (e.response) {
      console.error('服务器返回错误响应:');
      console.error('状态码:', e.response.status);
      console.error('响应数据:', e.response.data);
      errorMsg = e.response.data?.message || `服务器错误 (${e.response.status})`;
    } else if (e.request) {
      console.error('请求已发送但未收到响应');
      errorMsg = '无法连接到服务器，请确认：\n1. 后端服务已启动在 http://localhost:8080\n2. 网络连接正常';
    } else {
      console.error('请求配置错误:', e.config);
    }
    console.error('========== 异常结束 ==========');
    ElMessage.error(errorMsg);
  }
};
</script>

<style scoped>
.wrapper { 
  width: 100%; 
  min-height: 100vh; 
  background: #d0e8e3;
  padding-bottom: 40px;
  position: relative;
}

/* 确保背景覆盖整个视口 */
.wrapper::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #d0e8e3;
  z-index: -1;
}

header { 
  width: 100%; 
  max-width: 750px; 
  margin: 0 auto; 
  text-align: center; 
  position: relative;
  z-index: 1;
}
header img { 
  display: block; 
  width: 100%; 
  height: auto; 
  max-width: 600px; 
  margin: 0 auto; 
}
h1 { 
  text-align: center; 
  color: #46a34f; 
  font-size: 32px; 
  font-weight: 700; 
  margin: 20px 0 5px; 
  position: relative;
  z-index: 1;
}
.subtitle { 
  text-align: center; 
  color: #7bbd81; 
  font-size: 18px; 
  margin: 0 0 30px; 
  position: relative;
  z-index: 1;
}

section {
  width: 86%; 
  max-width: 500px; 
  margin: 0 auto;
  background: #fff; 
  border-radius: 12px; 
  padding: 30px 30px 40px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15); 
  box-sizing: border-box;
  position: relative;
  z-index: 1;
}

/* 输入域 = 输入框 + 提示文字，紧密包裹 */
.field { margin-top: 20px; }
.field.field-error .input-box { border-color: #f56c6c; }

.input-box {
  width: 100%; 
  height: 50px;
  border: solid 1px #ddd; 
  border-radius: 8px;
  background: #fafafa; 
  position: relative;
  display: flex; 
  align-items: center;
  transition: all 0.3s ease;
}
.input-box:focus-within { 
  border-color: #46a34f; 
  box-shadow: 0 0 0 2px rgba(70, 163, 79, 0.1);
}

/* Element Plus 图标样式 */
.input-icon {
  position: absolute; 
  left: 15px; 
  top: 50%;
  transform: translateY(-50%); 
  font-size: 20px; 
  color: #46a34f; 
  z-index: 1;
}

.input-box input {
  width: 100%; 
  height: 100%; 
  border: none; 
  outline: none;
  padding-left: 45px; 
  padding-right: 45px;
  font-size: 16px; 
  background: transparent; 
  box-sizing: border-box;
  text-align: left;
}
.input-box input::placeholder { 
  color: #999; 
  text-align: left;
}

/* 切换图标样式 */
.switch-icon {
  position: absolute; 
  right: 15px; 
  top: 50%;
  transform: translateY(-50%); 
  font-size: 20px; 
  color: #46a34f; 
  cursor: pointer; 
  z-index: 2;
  transition: all 0.3s ease;
  padding: 5px;
  border-radius: 4px;
}
.switch-icon:hover { 
  color: #3d8f44; 
  background: rgba(70, 163, 79, 0.1);
}

/* 密码眼睛图标 - Element Plus 风格 */
.eye-icon {
  position: absolute; 
  right: 15px; 
  top: 50%;
  transform: translateY(-50%); 
  font-size: 20px; 
  color: #46a34f; 
  cursor: pointer; 
  z-index: 2;
  transition: all 0.3s ease;
  padding: 5px;
  border-radius: 4px;
}
.eye-icon:hover { 
  color: #3d8f44; 
  background: rgba(70, 163, 79, 0.1);
}

/* 记住密码复选框样式 */
.remember-box {
  margin-top: 15px;
  margin-bottom: 15px;
}
.checkbox-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
}
.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  margin-right: 8px;
  cursor: pointer;
  accent-color: #46a34f;
}
.checkbox-text {
  font-size: 14px;
  color: #666;
  transition: color 0.3s;
}
.checkbox-label:hover .checkbox-text {
  color: #46a34f;
}

/* 提示 / 错误 — 紧贴输入框下沿 */
.field-hint, .field-err {
  font-size: 12px; 
  margin: 3px 0 0 8px; 
  text-align: left; 
  line-height: 1.3;
}
.field-hint { color: #999; }
.field-err  { color: #f56c6c; }

.button-box {
  width: 100%; 
  height: 50px; 
  border-radius: 8px;
  background: linear-gradient(135deg, #46a34f 0%, #3d8f44 100%); 
  color: #fff; 
  text-align: center; 
  line-height: 50px;
  font-size: 18px; 
  font-weight: bold;
  letter-spacing: 2px; 
  cursor: pointer; 
  user-select: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(70, 163, 79, 0.3);
}
.button-box:hover { 
  background: linear-gradient(135deg, #3d8f44 0%, #357a3b 100%);
  box-shadow: 0 6px 16px rgba(70, 163, 79, 0.4);
  transform: translateY(-2px);
}
.button-box:active {
  transform: translateY(0);
}

/* 底部链接样式 */
.bottom-links {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}
.bottom-links span {
  color: #46a34f;
  cursor: pointer;
  transition: color 0.3s;
}
.bottom-links span:hover {
  color: #3d8f44;
}
.bottom-links .divider {
  color: #ccc;
  margin: 0 8px;
  cursor: default;
}
</style>