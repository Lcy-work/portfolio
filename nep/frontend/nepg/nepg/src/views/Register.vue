<!-- src/views/Register.vue — NEPG 网格员注册页 -->
<template>
  <div class="wrapper">
    <!-- 顶部 Banner 图片 -->
    <header>
      <img src="@/assets/header.png" alt="环保主题插图" />
    </header>

    <!-- 主体内容区域 -->
    <div class="main-content">
      <!-- 标题区域 -->
      <h1>用户注册</h1>
      <p class="subtitle">东软环保公众监督平台 - 网格员端</p>

      <!-- 注册表单卡片 -->
      <section class="register-card">
        <div class="input-box">
          <i class="fa fa-user"></i>
          <input
            type="text"
            v-model="registerForm.username"
            placeholder="请输入实名"
            @blur="validateUsername"
          />
        </div>
        <div v-if="errors.username" class="error-message">{{ errors.username }}</div>

        <div class="input-box">
          <i class="fa fa-phone"></i>
          <input
            type="tel"
            v-model="registerForm.phone"
            placeholder="请输入手机号"
            maxlength="13"
            @input="formatPhoneInput"
            @blur="validatePhone"
          />
        </div>
        <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>

        <div class="input-box password-box">
          <i class="fa fa-lock"></i>
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="registerForm.password"
            placeholder="请输入密码（至少6位）"
            @blur="validatePassword"
          />
          <component 
            :is="showPassword ? Hide : View"
            class="toggle-password"
            @click="togglePasswordVisibility"
          />
        </div>
        <div v-if="errors.password" class="error-message">{{ errors.password }}</div>

        <div class="input-box password-box">
          <i class="fa fa-lock"></i>
          <input
            :type="showConfirmPassword ? 'text' : 'password'"
            v-model="registerForm.confirmPassword"
            placeholder="请再次输入密码"
            @keyup.enter="handleRegister"
            @blur="validateConfirmPassword"
          />
          <component 
            :is="showConfirmPassword ? Hide : View"
            class="toggle-password"
            @click="toggleConfirmPasswordVisibility"
          />
        </div>
        <div v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</div>

        <div class="btn-primary" @click="handleRegister">注 册</div>

        <div class="login-link">
          <span>已有账号？</span>
          <a @click="goToLogin">立即登录</a>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, inject, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from 'element-plus';
import { View, Hide } from '@element-plus/icons-vue';

const router = useRouter();
const axios = inject("axios");

// 注册表单数据
const registerForm = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
});

// 密码可见性控制
const showPassword = ref(false);
const showConfirmPassword = ref(false);

// 错误信息
const errors = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
});

// 切换密码可见性
const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

// 切换确认密码可见性
const toggleConfirmPasswordVisibility = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

// 验证用户名（实名）
const validateUsername = () => {
  if (!registerForm.username) {
    errors.username = '实名不能为空';
    return false;
  }
  // 移除长度限制，只检查是否为空
  errors.username = '';
  return true;
};

// 手机号格式化输入
const formatPhoneInput = () => {
  const digits = registerForm.phone.replace(/\D/g, '');
  if (digits.length <= 3) {
    registerForm.phone = digits;
  } else if (digits.length <= 7) {
    registerForm.phone = digits.slice(0, 3) + ' ' + digits.slice(3);
  } else {
    registerForm.phone = digits.slice(0, 3) + ' ' + digits.slice(3, 7) + ' ' + digits.slice(7, 11);
  }
};

// 验证手机号
const validatePhone = () => {
  const digits = registerForm.phone.replace(/\D/g, '');
  if (!digits) {
    errors.phone = '手机号不能为空';
    return false;
  }
  if (digits.length !== 11) {
    errors.phone = '请输入正确的11位手机号';
    return false;
  }
  errors.phone = '';
  return true;
};

// 验证密码
const validatePassword = () => {
  if (!registerForm.password) {
    errors.password = '密码不能为空';
    return false;
  }
  if (registerForm.password.length < 6) {
    errors.password = '密码长度不能少于6位';
    return false;
  }
  if (registerForm.password.length > 20) {
    errors.password = '密码长度不能超过20位';
    return false;
  }
  if (!/(?=.*[a-zA-Z])(?=.*\d)/.test(registerForm.password)) {
    errors.password = '密码必须包含字母和数字';
    return false;
  }
  errors.password = '';
  // 如果确认密码已输入，同时验证一致性
  if (registerForm.confirmPassword) {
    validateConfirmPassword();
  }
  return true;
};

// 验证确认密码
const validateConfirmPassword = () => {
  if (!registerForm.confirmPassword) {
    errors.confirmPassword = '请确认密码';
    return false;
  }
  if (registerForm.password !== registerForm.confirmPassword) {
    errors.confirmPassword = '两次输入的密码不一致';
    return false;
  }
  errors.confirmPassword = '';
  return true;
};

// 注册函数
const handleRegister = async () => {
  // 校验所有字段
  const isUsernameValid = validateUsername();
  const isPhoneValid = validatePhone();
  const isPasswordValid = validatePassword();
  const isConfirmPasswordValid = validateConfirmPassword();
  
  if (!isUsernameValid || !isPhoneValid || !isPasswordValid || !isConfirmPasswordValid) {
    return;
  }

  try {
    // 调用后端注册接口
    const response = await axios.post('gridMember/register', {
      gmName: registerForm.username,
      gmCode: registerForm.username,
      tel: registerForm.phone.replace(/\D/g, ''),
      password: registerForm.password
    });

    if (response.data) {
      ElMessage.success('注册成功');
      // 跳转到登录页
      router.push('/login');
    } else {
      ElMessage.error('注册失败');
    }
  } catch (error) {
    console.error('注册错误:', error);
    ElMessage.error('注册失败，请检查网络连接');
  }
};

// 跳转到登录页
const goToLogin = () => {
  router.push('/login');
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

/* ========== 顶部区域 ========== */
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

/* ========== 注册卡片 ========== */
.register-card {
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

/* ========== 注册按钮 ========== */
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

/* ========== 登录链接 ========== */
.login-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #1565C0;
  cursor: pointer;
  margin-left: 5px;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.login-link a:hover {
  color: #42A5F5;
  text-decoration: underline;
}

/* ========== 响应式优化 ========== */
@media (max-width: 768px) {
  h1 {
    font-size: 28px;
  }
  
  .subtitle {
    font-size: 16px;
  }
  
  .register-card {
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
  
  .register-card {
    width: 95%;
    padding: 25px 20px 30px;
  }
  
  header img {
    max-width: 350px;
  }
}
</style>
