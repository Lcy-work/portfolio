<template>
  <div class="wrapper">
    <!-- 顶部靛紫色装饰条 -->
    <div class="top-ban"></div>

    <!-- 主体内容区域 -->
    <div class="main-content">
      <!-- 标题区域 -->
      <h1>东软环保公众监督平台</h1>
      <p class="subtitle">管理员注册</p>

      <!-- 注册表单卡片 -->
      <section class="register-card">
        <!-- 账号 -->
        <div class="field" :class="{ 'field-error': errors.adminCode }">
          <div class="input-box">
            <svg class="input-icon" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <input
              type="text"
              v-model="registerForm.adminCode"
              placeholder="请输入管理员账号"
              @blur="validateAdminCode"
            />
          </div>
          <p class="field-err" v-if="errors.adminCode">{{ errors.adminCode }}</p>
        </div>

        <!-- 实名 -->
        <div class="field" :class="{ 'field-error': errors.adminName }">
          <div class="input-box">
            <svg class="input-icon" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <input
              type="text"
              v-model="registerForm.adminName"
              placeholder="请输入管理员姓名"
              @blur="validateAdminName"
            />
          </div>
          <p class="field-err" v-if="errors.adminName">{{ errors.adminName }}</p>
        </div>

        <!-- 手机号 -->
        <div class="field" :class="{ 'field-error': errors.phone }">
          <div class="input-box">
            <svg class="input-icon" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="5" y="2" width="14" height="20" rx="2" ry="2"/>
              <line x1="12" y1="18" x2="12.01" y2="18"/>
            </svg>
            <input
              type="tel"
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              maxlength="13"
              @input="formatPhone"
              @blur="validatePhone"
            />
          </div>
          <p class="field-err" v-if="errors.phone">{{ errors.phone }}</p>
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
              v-model="registerForm.password"
              placeholder="请输入密码（至少6位）"
              @blur="validatePassword"
            />
            <svg
              v-if="showPassword"
              class="toggle-password"
              viewBox="0 0 24 24"
              width="18"
              height="18"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              @click="showPassword = !showPassword"
            >
              <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/>
              <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/>
              <path d="m14.12 14.12a3 3 0 1 1-4.24-4.24"/>
              <line x1="1" y1="1" x2="23" y2="23"/>
            </svg>
            <svg
              v-else
              class="toggle-password"
              viewBox="0 0 24 24"
              width="18"
              height="18"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              @click="showPassword = !showPassword"
            >
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
          </div>
          <p class="field-err" v-if="errors.password">{{ errors.password }}</p>
        </div>

        <!-- 确认密码 -->
        <div class="field">
          <div class="input-box">
            <svg class="input-icon" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
            </svg>
            <input
              :type="showConfirm ? 'text' : 'password'"
              v-model="registerForm.confirmPassword"
              placeholder="请再次输入密码"
              @keyup.enter="handleRegister"
              @blur="validateConfirmPassword"
            />
            <svg
              v-if="showConfirm"
              class="toggle-password"
              viewBox="0 0 24 24"
              width="18"
              height="18"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              @click="showConfirm = !showConfirm"
            >
              <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/>
              <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/>
              <path d="m14.12 14.12a3 3 0 1 1-4.24-4.24"/>
              <line x1="1" y1="1" x2="23" y2="23"/>
            </svg>
            <svg
              v-else
              class="toggle-password"
              viewBox="0 0 24 24"
              width="18"
              height="18"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              @click="showConfirm = !showConfirm"
            >
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
          </div>
          <p class="field-err" v-if="errors.confirmPassword">{{ errors.confirmPassword }}</p>
        </div>

        <!-- 注册按钮 -->
        <div class="btn-primary" @click="handleRegister">注 册</div>

        <!-- 登录链接 -->
        <div class="login-link">
          <span>已有账号？</span>
          <a @click="goToLogin">立即登录</a>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { inject, reactive, ref } from "vue";
import { useRouter } from "vue-router";

const myElMessage = inject("myElMessage");
const axios = inject("axios");
const router = useRouter();

const registerForm = reactive({
  adminCode: "",
  adminName: "",
  phone: "",
  password: "",
  confirmPassword: "",
});

const showPassword = ref(false);
const showConfirm = ref(false);

const errors = reactive({
  adminCode: "",
  adminName: "",
  phone: "",
  password: "",
  confirmPassword: "",
});

const validateAdminCode = () => {
  if (!registerForm.adminCode || registerForm.adminCode.trim() === "") {
    errors.adminCode = "请输入管理员账号";
    return false;
  }
  errors.adminCode = "";
  return true;
};

const validateAdminName = () => {
  if (!registerForm.adminName || registerForm.adminName.trim() === "") {
    errors.adminName = "请输入管理员姓名";
    return false;
  }
  errors.adminName = "";
  return true;
};

const formatPhone = () => {
  let val = registerForm.phone.replace(/\s/g, "");
  val = val.replace(/\D/g, "");
  if (val.length > 11) val = val.slice(0, 11);
  const parts = [];
  if (val.length > 0) parts.push(val.slice(0, 3));
  if (val.length > 3) parts.push(val.slice(3, 7));
  if (val.length > 7) parts.push(val.slice(7, 11));
  registerForm.phone = parts.join(" ");
};

const validatePhone = async () => {
  const raw = registerForm.phone.replace(/\s/g, "");
  if (!raw) {
    errors.phone = "请输入手机号";
    return false;
  }
  if (raw.length !== 11) {
    errors.phone = "手机号必须为11位";
    return false;
  }
  // check duplicate
  try {
    const resp = await axios.get("admins/checkTel/" + raw);
    if (resp.data === true) {
      errors.phone = "该手机号已被注册";
      return false;
    }
  } catch (e) {}
  errors.phone = "";
  return true;
};

const validatePassword = () => {
  if (!registerForm.password) {
    errors.password = "密码不能为空";
    return false;
  }
  if (registerForm.password.length < 6) {
    errors.password = "密码长度不能少于6位";
    return false;
  }
  if (registerForm.password.length > 20) {
    errors.password = "密码长度不能超过20位";
    return false;
  }
  errors.password = "";
  if (registerForm.confirmPassword) validateConfirmPassword();
  return true;
};

const validateConfirmPassword = () => {
  if (!registerForm.confirmPassword) {
    errors.confirmPassword = "请确认密码";
    return false;
  }
  if (registerForm.password !== registerForm.confirmPassword) {
    errors.confirmPassword = "两次输入的密码不一致";
    return false;
  }
  errors.confirmPassword = "";
  return true;
};

const handleRegister = async () => {
  const v1 = validateAdminCode();
  const v2 = validateAdminName();
  const v3 = await validatePhone();
  const v4 = validatePassword();
  const v5 = validateConfirmPassword();
  if (!v1 || !v2 || !v3 || !v4 || !v5) return;

  try {
    const response = await axios.post("admins/register", {
      adminCode: registerForm.adminCode.trim(),
      adminName: registerForm.adminName.trim(),
      phone: registerForm.phone.replace(/\s/g, ""),
      password: registerForm.password,
    });
    if (response.data) {
      myElMessage.success("注册成功，请登录");
      router.push("/login");
    } else {
      myElMessage.error("注册失败，账号可能已存在");
    }
  } catch (e) {
    let errorMsg = "注册失败";
    
    // 判断是否是唯一约束冲突错误（账号或手机号已存在）
    if (e.response?.data?.message?.includes('Duplicate entry')) {
      const message = e.response.data.message;
      
      // 根据约束字段名判断是账号还是手机号重复
      if (message.includes('dis_code') || message.includes('admin_code')) {
        errorMsg = "账号已存在，请使用其他账号";
      } else if (message.includes('phone') || message.includes('tel')) {
        errorMsg = "手机号已存在，请使用其他手机号";
      } else {
        errorMsg = "该信息已存在，请修改后重试";
      }
    } else {
      errorMsg = "注册失败：" + (e.response?.data?.message || e.message);
    }
    
    myElMessage.error(errorMsg);
  }
};

const goToLogin = () => {
  router.push("/login");
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

/* ========== 顶部靛紫色装饰条 ========== */
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

/* ========== 注册卡片 ========== */
.register-card {
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

/* ========== 注册按钮 ========== */
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

/* ========== 登录链接 ========== */
.login-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #3949AB;
  cursor: pointer;
  margin-left: 5px;
  font-weight: 500;
  transition: color 0.3s ease;
}

.login-link a:hover {
  color: #5C6BC0;
  text-decoration: underline;
}
</style>
