<template>
  <div class="wrapper">
    <!-- 顶部蓝色区域 -->
    <div class="top-ban"></div>

    <!-- 主体内容区域 -->
    <div class="main-content">
      <!-- 标题区域 -->
      <h1>东软环保公众监督平台</h1>
      <p class="subtitle">系统管理员端</p>

      <!-- 登录表单卡片 -->
      <section class="login-card">
        <!-- 账号 -->
        <div class="field" :class="{ 'field-error': errors.account }">
          <div class="input-box">
            <svg class="input-icon" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <input
              type="text"
              v-model="adminsForm.adminCode"
              placeholder="请输入实名 / 手机号 / 账号"
              @blur="validateAccount"
            />
          </div>
          <p class="field-err" v-if="errors.account">{{ errors.account }}</p>
        </div>

        <!-- 密码 -->
        <div class="field">
          <div class="input-box">
            <svg class="input-icon" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
            </svg>
            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="adminsForm.password"
              placeholder="请输入登录密码"
              @keyup.enter="login"
              @blur="validatePassword"
            />
            <el-icon 
              class="toggle-password" 
              style="font-size: 18px; cursor: pointer;"
              @click="showPassword = !showPassword"
            >
              <View v-if="!showPassword" />
              <Hide v-else />
            </el-icon>
          </div>
          <p class="field-err" v-if="errors.password">{{ errors.password }}</p>
        </div>

        <!-- 记住密码 -->
        <div class="remember-password">
          <el-checkbox v-model="rememberPassword" size="large">记住密码</el-checkbox>
        </div>

        <!-- 登录按钮 -->
        <div class="btn-primary" @click="login">登 录</div>

        <!-- 注册和忘记密码链接 -->
        <div class="bottom-links">
          <a @click="goToRegister">注册账号</a>
          <span class="divider">|</span>
          <a @click="handleForgotPassword">忘记密码？</a>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { inject, reactive, ref, onMounted } from "vue";
import { setSessionStorage } from "../common.js";
import { useRouter } from 'vue-router';
import { View, Hide } from '@element-plus/icons-vue';
import { ElMessageBox } from 'element-plus';

const myElMessage = inject('myElMessage');
const axios = inject('axios');
const router = useRouter();

const adminsForm = reactive({
  adminCode: '',
  password: ''
});

const showPassword = ref(false);
const rememberPassword = ref(false); // 记住密码状态

const errors = reactive({
  account: '',
  password: ''
});

// 页面加载时，从 localStorage 读取保存的账号密码
onMounted(() => {
  const savedCredentials = localStorage.getItem('nepm_login_credentials');
  if (savedCredentials) {
    try {
      const credentials = JSON.parse(savedCredentials);
      adminsForm.adminCode = credentials.adminCode || '';
      adminsForm.password = credentials.password || '';
      rememberPassword.value = true;
    } catch (e) {
      console.error('读取保存的登录信息失败:', e);
    }
  }
});

const validateAccount = () => {
  if (!adminsForm.adminCode || adminsForm.adminCode.trim() === '') {
    errors.account = '请输入实名 / 手机号 / 账号';
    return false;
  }
  errors.account = '';
  return true;
};

const validatePassword = () => {
  if (!adminsForm.password || adminsForm.password.trim() === '') {
    errors.password = '请输入登录密码';
    return false;
  }
  errors.password = '';
  return true;
};

const login = () => {
  const validCode = validateAccount();
  const validPwd = validatePassword();
  if (!validCode || !validPwd) return;

  axios.post('admins/getAdminsByCodeByPass', {
    adminCode: adminsForm.adminCode.trim(),
    password: adminsForm.password
  }).then(function(response) {
    if (response.data != null && response.data !== '') {
      setSessionStorage('admins', response.data);
      
      // 根据"记住密码"选项决定是否保存账号密码
      if (rememberPassword.value) {
        localStorage.setItem('nepm_login_credentials', JSON.stringify({
          adminCode: adminsForm.adminCode.trim(),
          password: adminsForm.password
        }));
      } else {
        // 如果不记住密码，清除之前保存的账号密码
        localStorage.removeItem('nepm_login_credentials');
      }
      
      myElMessage.success('登录成功');
      router.push('/index');
    } else {
      myElMessage.error('账号或密码错误');
    }
  }).catch(function(error) {
    myElMessage.error('登录失败：' + (error.response?.data?.message || error.message));
  });
};

const goToRegister = () => {
  router.push('/register');
};

// 处理忘记密码
const handleForgotPassword = () => {
  ElMessageBox.alert(
    '请联系系统管理员重置密码。\n\n联系电话：XXX-XXXX-XXXX',
    '忘记密码',
    {
      confirmButtonText: '确定',
      type: 'info',
      customClass: 'nepm-message-box'
    }
  );
};
</script>

<style scoped>
.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #EEF2FF;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* ========== 顶部蓝紫色装饰条 ========== */
.top-ban {
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #3949AB, #5C6BC0, #3949AB);
  flex-shrink: 0;
}

/* ========== 主体内容区域 ========== */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 20px 40px;
  background-color: #EEF2FF;
}

/* ========== 标题 ========== */
h1 {
  text-align: center;
  color: #3949AB;
  font-size: 36px;
  font-weight: 700;
  margin: 0 0 8px;
  letter-spacing: 2px;
}

.subtitle {
  text-align: center;
  color: #5C6BC0;
  font-size: 20px;
  margin: 0 0 30px;
  letter-spacing: 1px;
}

/* ========== 登录卡片 ========== */
.login-card {
  width: 400px;
  background-color: #fff;
  border-radius: 16px;
  padding: 40px 35px 45px;
  box-shadow: 0 8px 32px rgba(57, 73, 171, 0.15);
}

/* ========== 输入域 ========== */
.field {
  margin-top: 22px;
}
.field:first-child {
  margin-top: 0;
}

.input-box {
  width: 100%;
  height: 52px;
  border: solid 2px #e0e0e0;
  border-radius: 10px;
  display: flex;
  align-items: center;
  position: relative;
  background-color: #fafafa;
  transition: all 0.3s ease;
}

.input-box:hover {
  border-color: #5C6BC0;
  background-color: #f5f6ff;
}

.input-box:focus-within {
  border-color: #3949AB;
  box-shadow: 0 0 0 3px rgba(57, 73, 171, 0.1);
}

.field-error .input-box {
  border-color: #f56c6c;
}

.input-icon {
  position: absolute;
  left: 18px;
  top: 50%;
  transform: translateY(-50%);
  color: #9FA8DA;
  z-index: 1;
  transition: color 0.3s ease;
}

.input-box:focus-within .input-icon {
  color: #3949AB;
}

.input-box input {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  padding-left: 50px;
  padding-right: 45px;
  font-size: 16px;
  background: transparent;
  box-sizing: border-box;
  color: #333;
}

.input-box input::placeholder {
  color: #aaa;
}

/* 密码眼睛 */
.toggle-password {
  position: absolute;
  right: 18px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  color: #9FA8DA;
  transition: color 0.3s ease;
  z-index: 2;
}

.toggle-password:hover {
  color: #3949AB;
}

/* 错误提示 */
.field-err {
  color: #f56c6c;
  font-size: 13px;
  margin: 6px 0 0 5px;
  text-align: left;
}

/* ========== 记住密码复选框 ========== */
.remember-password {
  margin-top: 18px;
  display: flex;
  align-items: center;
}

.remember-password :deep(.el-checkbox__label) {
  font-size: 14px;
  color: #666;
  user-select: none;
}

.remember-password :deep(.el-checkbox.is-checked .el-checkbox__inner) {
  background-color: #3949AB;
  border-color: #3949AB;
}

.remember-password :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #3949AB;
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
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #5C6BC0 0%, #3949AB 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(57, 73, 171, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(57, 73, 171, 0.4);
}

.btn-primary:active {
  transform: translateY(0);
}

/* ========== 底部链接（注册账号 | 忘记密码） ========== */
.bottom-links {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.bottom-links a {
  color: #3949AB;
  cursor: pointer;
  font-weight: 500;
  transition: color 0.3s ease;
  text-decoration: none;
}

.bottom-links a:hover {
  color: #5C6BC0;
  text-decoration: underline;
}

.divider {
  margin: 0 10px;
  color: #ccc;
}
</style>
