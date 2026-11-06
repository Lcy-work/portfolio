<!-- src/views/Login.vue — NEPG 网格员登录页 -->
<template>
  <div class="wrapper">
    <!-- 顶部 Banner 图片 -->
    <header>
      <img src="@/assets/header.png" alt="环保主题插图" />
    </header>

    <!-- 主体内容区域 -->
    <div class="main-content">
      <!-- 标题区域 -->
      <h1>东软环保公众监督平台</h1>
      <p class="subtitle">网格员端</p>

      <!-- 登录表单卡片 -->
      <section class="login-card">
        <div class="input-box">
          <i class="fa fa-user"></i>
          <input
            type="text"
            v-model="loginForm.username"
            placeholder="请输入实名或手机号"
            @input="formatUsernameInput"
            @blur="validateUsername"
          />
        </div>
        <div v-if="errors.username" class="error-message">{{ errors.username }}</div>

        <div class="input-box password-box">
          <i class="fa fa-lock"></i>
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="loginForm.password"
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
            @blur="validatePassword"
          />
          <component 
            :is="showPassword ? Hide : View"
            class="toggle-password"
            @click="togglePasswordVisibility"
          />
        </div>
        <div v-if="errors.password" class="error-message">{{ errors.password }}</div>

        <div class="remember-me">
          <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
        </div>

        <div class="btn-primary" @click="handleLogin">登 录</div>

        <div class="links">
          <a @click="goToRegister">注册账号</a>
          <span class="divider">|</span>
          <a @click="goToForgotPassword">忘记密码？</a>
        </div>
      </section>
    </div>

  </div>
</template>

<script setup>
import { reactive, inject, ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { setSessionStorage } from "../common.js";
import { ElMessage } from 'element-plus';
import { View, Hide } from '@element-plus/icons-vue';

const router = useRouter();
const axios = inject("axios");

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
});

// 密码可见性控制
const showPassword = ref(false);

// 记住密码
const rememberMe = ref(false);

// 错误信息
const errors = reactive({
  username: '',
  password: ''
});

// 页面加载时读取保存的账号密码
onMounted(() => {
  const savedUsername = localStorage.getItem('remembered_username');
  const savedPassword = localStorage.getItem('remembered_password');
  const savedRememberMe = localStorage.getItem('remember_me');
  
  if (savedRememberMe === 'true' && savedUsername && savedPassword) {
    loginForm.username = savedUsername;
    loginForm.password = savedPassword;
    rememberMe.value = true;
  }
});

// 切换密码可见性
const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

// 验证用户名（支持实名或手机号）
const validateUsername = () => {
  if (!loginForm.username) {
    errors.username = '实名不能为空';
    return false;
  }
  
  // 去除空格后检查
  const usernameWithoutSpace = loginForm.username.replace(/\s/g, '');
  
  // 判断是否为手机号格式（11位数字）
  if (/^\d{11}$/.test(usernameWithoutSpace)) {
    // 是手机号，验证通过
    errors.username = '';
    return true;
  }
  
  // 不是手机号，按实名规则验证（只需非空即可）
  errors.username = '';
  return true;
};

// 用户名/手机号格式化输入
const formatUsernameInput = () => {
  const input = loginForm.username;
  // 提取所有数字
  const digits = input.replace(/\D/g, '');
  
  // 只处理1-11位数字的情况
  if (digits.length === 0 || digits.length > 11) {
    return; // 不处理空值或超过11位的数字
  }
  
  // 应用手机号格式化：XXX XXXX XXXX
  let formatted = '';
  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = digits.slice(0, 3) + ' ' + digits.slice(3);
  } else {
    formatted = digits.slice(0, 3) + ' ' + digits.slice(3, 7) + ' ' + digits.slice(7, 11);
  }
  
  // 只有当格式化后的结果与当前值不同时才更新，避免不必要的触发
  if (formatted !== input) {
    loginForm.username = formatted;
  }
};

// 验证密码
const validatePassword = () => {
  if (!loginForm.password) {
    errors.password = '密码不能为空';
    return false;
  }
  if (loginForm.password.length < 6) {
    errors.password = '密码长度不能少于6位';
    return false;
  }
  errors.password = '';
  return true;
};

// 登录函数
const handleLogin = async () => {
  // 校验所有字段
  const isUsernameValid = validateUsername();
  const isPasswordValid = validatePassword();
  
  if (!isUsernameValid || !isPasswordValid) {
    return;
  }

  try {
    // 调用后端登录接口，去除空格后发送纯数字或用户名
    const usernameToSend = loginForm.username.replace(/\s/g, '');
    const response = await axios.post('gridMember/getGridMemberByCodeByPass', {
      gmCode: usernameToSend,
      password: loginForm.password
    });

    if (response.data) {
      // 如果勾选了记住密码，保存到 localStorage
      if (rememberMe.value) {
        localStorage.setItem('remembered_username', loginForm.username);
        localStorage.setItem('remembered_password', loginForm.password);
        localStorage.setItem('remember_me', 'true');
      } else {
        // 如果未勾选，清除保存的信息
        localStorage.removeItem('remembered_username');
        localStorage.removeItem('remembered_password');
        localStorage.removeItem('remember_me');
      }

      // 登录成功，保存用户信息到 sessionStorage
      setSessionStorage('gridMember', response.data);
      ElMessage.success('登录成功');
      // 跳转到反馈任务列表页
      router.push('/feedbackList');
    } else {
      ElMessage.error('用户名或密码错误');
    }
  } catch (error) {
    console.error('登录错误:', error);
    ElMessage.error('登录失败，请检查网络连接');
  }
};

// 跳转到注册页
const goToRegister = () => {
  router.push('/register');
};

// 跳转到找回密码页
const goToForgotPassword = () => {
  router.push('/forgotPassword');
};

</script>

<style scoped>
.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #E3F2FD;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* ========== 顶部区域 - 紧贴顶部，大小合适 ========== */
header { 
  width: 100%;
  text-align: center;
  flex-shrink: 0;
  padding: 0;
  margin: 0;
}

header img { 
  display: block; 
  width: 100%;
  height: auto;
  max-width: 750px;
  margin: 0 auto;
}

/* ========== 主体内容区域 ========== */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 20px 20px 40px;
  background-color: #E3F2FD;
}

/* ========== 标题 ========== */
h1 {
  text-align: center;
  color: #1565C0;
  font-size: 36px;
  font-weight: 700;
  margin: 20px 0 8px;
  letter-spacing: 2px;
}

.subtitle {
  text-align: center;
  color: #42A5F5;
  font-size: 20px;
  margin: 0 0 30px;
  letter-spacing: 1px;
}

/* ========== 登录卡片 ========== */
.login-card {
  width: 90%;
  max-width: 480px;
  background-color: #fff;
  border-radius: 16px;
  box-sizing: border-box;
  padding: 40px 35px 45px;
  box-shadow: 0 8px 32px rgba(21, 101, 192, 0.15);
  backdrop-filter: blur(10px);
}

/* ========== 输入框 ========== */
.input-box {
  width: 100%;
  height: 52px;
  border: solid 2px #e0e0e0;
  border-radius: 10px;
  margin-top: 22px;
  display: flex;
  align-items: center;
  position: relative;
  background-color: #fafafa;
  transition: all 0.3s ease;
}

.input-box:hover {
  border-color: #42A5F5;
  background-color: #f5faff;
}

.input-box:focus-within {
  border-color: #1565C0;
  box-shadow: 0 0 0 3px rgba(21, 101, 192, 0.1);
}

.input-box .fa { 
  position: absolute;
  left: 18px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px; 
  color: #90caf9; 
  z-index: 1;
  transition: color 0.3s ease;
}

.input-box:focus-within .fa {
  color: #1565C0;
}

.input-box input { 
  width: 100%;
  height: 100%;
  border: none; 
  outline: none; 
  padding-left: 50px;
  padding-right: 45px;
  text-align: left;
  font-size: 16px;
  background: transparent;
  box-sizing: border-box;
  color: #333;
}

.input-box input::placeholder {
  color: #aaa;
  text-align: left;
}

/* 密码框特殊样式 */
.password-box {
  position: relative;
}

.toggle-password {
  position: absolute;
  right: 18px;
  left: auto !important;
  cursor: pointer;
  font-size: 18px;
  color: #90caf9;
  transition: color 0.3s ease;
  z-index: 2;
  width: 20px;
  height: 20px;
}

.toggle-password:hover {
  color: #1565C0;
}

/* ========== 错误提示 ========== */
.error-message {
  color: #f44336;
  font-size: 13px;
  margin-top: 6px;
  margin-left: 5px;
  text-align: left;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* ========== 记住密码复选框 ========== */
.remember-me {
  margin-top: 15px;
  display: flex;
  align-items: center;
}

.remember-me :deep(.el-checkbox) {
  font-size: 14px;
  color: #666;
}

.remember-me :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #1565C0;
  border-color: #1565C0;
}

.remember-me :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #1565C0;
}

/* ========== 登录按钮 ========== */
.btn-primary {
  margin-top: 28px;
  border-radius: 10px;
  height: 52px;
  line-height: 52px;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 2px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #42A5F5 0%, #1565C0 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(21, 101, 192, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(21, 101, 192, 0.4);
}

.btn-primary:active {
  transform: translateY(0);
}

/* ========== 链接区域 ========== */
.links {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
}

.links a {
  color: #1565C0;
  cursor: pointer;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.links a:hover {
  color: #42A5F5;
  text-decoration: underline;
}

.links .divider {
  margin: 0 12px;
  color: #ccc;
}

/* ========== 响应式优化 ========== */
@media (max-width: 768px) {
  h1 {
    font-size: 28px;
  }
  
  .subtitle {
    font-size: 16px;
  }
  
  .login-card {
    padding: 30px 25px 35px;
  }
  
  .input-box {
    height: 48px;
  }
  
  .btn-primary {
    height: 48px;
    line-height: 48px;
    font-size: 16px;
  }
  
  header img {
    max-width: 500px;
  }
}

@media (max-width: 480px) {
  .main-content {
    padding: 20px 15px;
  }
  
  h1 {
    font-size: 24px;
  }
  
  .login-card {
    width: 95%;
    padding: 25px 20px 30px;
  }
  
  header img {
    max-width: 350px;
  }
}
</style>
