<!-- src/views/ForgotPassword.vue — NEPG 网格员找回密码页 -->
<template>
  <div class="wrapper">
    <!-- 顶部 Banner 图片 -->
    <header>
      <img src="@/assets/header.png" alt="环保主题插图" />
    </header>

    <!-- 主体内容区域 -->
    <div class="main-content">
      <!-- 标题区域 -->
      <h1>找回密码</h1>
      <p class="subtitle">东软环保公众监督平台 - 网格员端</p>

      <!-- 找回密码表单卡片 -->
      <section class="forgot-card">
        <div class="input-box">
          <i class="fa fa-user"></i>
          <input
            type="text"
            v-model="forgotForm.username"
            placeholder="请输入实名"
            @blur="validateUsername"
          />
        </div>
        <div v-if="errors.username" class="error-message">{{ errors.username }}</div>

        <div class="input-box">
          <i class="fa fa-phone"></i>
          <input
            type="tel"
            v-model="forgotForm.phone"
            placeholder="请输入手机号"
            maxlength="13"
            @input="formatPhoneInput"
            @keyup.enter="handleForgotPassword"
            @blur="validatePhone"
          />
        </div>
        <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>

        <div class="btn-primary" @click="handleForgotPassword">提 交</div>

        <div class="login-link">
          <span>想起密码了？</span>
          <a @click="goToLogin">返回登录</a>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, inject } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from 'element-plus';

const router = useRouter();
const axios = inject("axios");

// 找回密码表单数据
const forgotForm = reactive({
  username: '',
  phone: ''
});

// 错误信息
const errors = reactive({
  username: '',
  phone: ''
});

// 验证用户名（实名）
const validateUsername = () => {
  if (!forgotForm.username) {
    errors.username = '实名不能为空';
    return false;
  }
  // 移除长度限制，只检查是否为空
  errors.username = '';
  return true;
};

// 手机号格式化输入
const formatPhoneInput = () => {
  const digits = forgotForm.phone.replace(/\D/g, '');
  if (digits.length <= 3) {
    forgotForm.phone = digits;
  } else if (digits.length <= 7) {
    forgotForm.phone = digits.slice(0, 3) + ' ' + digits.slice(3);
  } else {
    forgotForm.phone = digits.slice(0, 3) + ' ' + digits.slice(3, 7) + ' ' + digits.slice(7, 11);
  }
};

// 验证手机号
const validatePhone = () => {
  const digits = forgotForm.phone.replace(/\D/g, '');
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

// 找回密码函数
const handleForgotPassword = async () => {
  // 校验所有字段
  const isUsernameValid = validateUsername();
  const isPhoneValid = validatePhone();

  if (!isUsernameValid || !isPhoneValid) {
    return;
  }

  try {
    // 调用后端找回密码接口
    const response = await axios.post('gridMember/forgotPassword', {
      gmName: forgotForm.username,
      tel: forgotForm.phone.replace(/\D/g, '')
    });

    if (response.data) {
      ElMessage.success('您的密码是：' + response.data);
      // 跳转到登录页
      setTimeout(() => {
        router.push('/login');
      }, 3000);
    } else {
      ElMessage.error('未找到该用户');
    }
  } catch (error) {
    console.error('找回密码错误:', error);
    ElMessage.error('操作失败，请检查网络连接');
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

/* ========== 找回密码卡片 ========== */
.forgot-card {
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

/* ========== 提交按钮 ========== */
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
  
  .forgot-card {
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
  
  .forgot-card {
    width: 95%;
    padding: 25px 20px 30px;
  }
  
  header img {
    max-width: 350px;
  }
}
</style>
