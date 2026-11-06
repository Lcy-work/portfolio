<template>
  <div class="auth-container">
    <div class="form-wrapper">
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        auto-complete="on"
        label-position="left"
      >
        <div class="header-section">
          <h2 class="system-name">医保费用报销系统</h2>
          <p class="welcome-text">欢迎使用东软医疗保险报销平台</p>
        </div>

        <el-form-item prop="username">
          <div class="input-prepend">
            <User class="input-icon" /> 
          </div>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="请输入用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
            class="custom-input"
          />
        </el-form-item>

        <el-form-item prop="password">
          <div class="input-prepend">
            <Lock class="input-icon" />
          </div>
          <el-input
            ref="passwordRef"
            :key="passwordType"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="请输入密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter="handleLogin"
            class="custom-input"
          />
          <div class="password-toggle" @click="showPwd">
            <View v-if="passwordType === 'password'" class="toggle-icon" />
            <Hide v-else class="toggle-icon" />
          </div>
        </el-form-item>
        <el-button
          size="large"
          :loading="loading"
          type="primary"
          class="login-btn"
          @click.prevent="handleLogin"
        >
          {{ loading ? '' : '登录' }}
        </el-button>

       
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, toRefs, inject, nextTick } from 'vue';
import { User, Lock, View, Hide } from '@element-plus/icons-vue';
import { ElForm, ElInput, ElMessage, ElButton } from 'element-plus';
import router from '@/router';

const axios = inject("axios");
axios.defaults.baseURL = "http://localhost:8081"

const user = ref({});
const loginFormRef = ref(ElForm);
const passwordRef = ref(ElInput);
const state = reactive({
  redirect: '',

  loginForm: {
    username: '',
    password: ''
  },
  loginRules: {
    username: [{ required: true, trigger: 'blur', validator: validateUsername }],
    password: [{ required: true, trigger: 'blur', validator: validatePassword }]
  },
  loading: false,
  passwordType: 'password',
  capslockTooltipDisabled: true,
  otherQuery: {}
});

function validateUsername(rule, value, callback) {
  if (value.length < 1) {
    callback(new Error('用户名不能为空'));
  } else {
    callback();
  }
}

function validatePassword(rule, value, callback) {
  if (value.length < 6) {
    callback(new Error('密码长度不能小于6个字符'));
  } else {
    callback();
  }
}

const {
  loginForm,
  loginRules,
  loading,
  passwordType,
  capslockTooltipDisabled
} = toRefs(state);


function showPwd() {
  if (state.passwordType === 'password') {
    state.passwordType = '';
  } else {
    state.passwordType = 'password';
  }
  nextTick(() => {
    passwordRef.value.focus();
  });
}

function handleLogin() {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      state.loading = true;
      axios.get("/login", {
        params: {
          username: state.loginForm.username,
          password: state.loginForm.password
        }
      })
        .then((response) => {
          if (response.data.code === 0) {
            // 登录成功
            const userInfo = response.data.data; 
            window.localStorage.setItem('user', JSON.stringify(userInfo));
            ElMessage.success(response.data.msg);
            router.push("/home");
          } else {
            // 登录失败
            ElMessage.warning(response.data.msg);
          }
          state.loading = false;
        })
        .catch((error) => {
          console.error("登录请求失败:", error);
          ElMessage.error("登录请求失败，请检查网络连接");
          state.loading = false;
        });
    } else {
      return false;
    }
  });
}


onMounted(() => {
});
</script>

<style scoped>
.auth-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(120deg, #1a5fb4, #26a269);
  padding: 20px;
}

.form-wrapper {
  width: 100%;
  max-width: 480px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  padding: 40px 30px;
  margin-bottom: 20px;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.system-name {
  color: #1a5fb4;
  font-size: 24px;
  margin-bottom: 8px;
}

.welcome-text {
  color: #666;
  font-size: 15px;
}

.login-form {
  margin-top: 20px;
}

.input-prepend {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: #f0f7ff;
  border-radius: 8px 0 0 8px;
  border-right: 1px solid #e0e0e0;
}

.input-icon {
  width: 18px;
  height: 18px;
  color: #1a5fb4;
}

.custom-input {
  flex: 1;
}

:deep(.el-input__wrapper) {
  border-radius: 0 8px 8px 0 !important;
  border-left: none !important;
  box-shadow: none !important;
  padding: 0 15px;
}

:deep(.el-form-item) {
  margin-bottom: 25px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
  background: #fff;
  display: flex;
}

:deep(.el-form-item__content) {
  display: flex;
  flex: 1;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  color: #777;
}

.toggle-icon {
  width: 18px;
  height: 18px;
}

.caps-warning {
  display: flex;
  align-items: center;
  background: #fff8e1;
  color: #e6a23c;
  padding: 8px 15px;
  border-radius: 6px;
  margin-bottom: 20px;
  font-size: 14px;
}

.warning-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  background: #e6a23c;
  color: white;
  border-radius: 50%;
  margin-right: 8px;
  font-weight: bold;
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(to right, #1a5fb4, #26a269);
  border: none;
  transition: all 0.3s;
}

.login-btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(26, 95, 180, 0.3);
}

.demo-section {
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px dashed #e0e0e0;
  text-align: center;
}

.demo-title {
  color: #666;
  font-size: 14px;
  margin-bottom: 10px;
}

.demo-account {
  display: flex;
  justify-content: center;
  gap: 25px;
  font-size: 14px;
  color: #444;
}

.demo-account strong {
  color: #1a5fb4;
}

.footer-note {
  color: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  margin-top: 15px;
  text-align: center;
}
</style>