<!-- src/views/Register.vue -->
<template>
  <div class="wrapper">
    <header>
      <img src="@/assets/header.png" alt="环保主题插图">
    </header>

    <h1>东软环保公众监督平台</h1>
    <p class="subtitle">保护环境人人有责</p>

    <section>
      <!-- 手机号码 -->
      <div class="field" :class="{ 'field-error': errors.telId }">
        <label>手机号码</label>
        <div class="input-box">
          <el-icon class="input-icon"><User /></el-icon>
          <input type="text" v-model="displayPhone"
            @input="onPhoneInput" @blur="checkTelExists"
            placeholder="请输入手机号码" maxlength="13"
          >
        </div>

        <p class="field-err" v-if="errors.telId">{{ errors.telId }}</p>
      </div>

      <!-- 真实姓名 -->
      <div class="field" :class="{ 'field-error': errors.realName }">
        <label>真实姓名</label>
        <div class="input-box">
          <el-icon class="input-icon"><UserFilled /></el-icon>
          <input type="text" v-model="supervisorForm.realName"
            placeholder="真实姓名便于我们联系您" @blur="validateRealName"
          >
        </div>
        <p class="field-err" v-if="errors.realName">{{ errors.realName }}</p>
      </div>

      <!-- 年龄 -->
      <div class="field">
        <label>年龄</label>
        <div class="input-box">
          <el-icon class="input-icon"><Calendar /></el-icon>
          <input type="date" v-model="supervisorForm.birthday" placeholder="请选择生日">
        </div>
      </div>

      <!-- 性别 -->
      <div class="field">
        <label>性别</label>
        <div class="sex-row">
          <label class="radio-label">
            <el-icon class="radio-icon"><Male /></el-icon>
            <input type="radio" name="sex" :value="1" v-model="supervisorForm.sex"> 男
          </label>
          <label class="radio-label">
            <el-icon class="radio-icon"><Female /></el-icon>
            <input type="radio" name="sex" :value="0" v-model="supervisorForm.sex"> 女
          </label>
        </div>
      </div>

      <!-- 密码 -->
      <div class="field" :class="{ 'field-error': errors.password }">
        <label>密码</label>
        <div class="input-box">
          <el-icon class="input-icon"><Lock /></el-icon>
          <input :type="showPassword ? 'text' : 'password'"
            v-model="supervisorForm.password" placeholder="请输入密码(6-20位)"
            @input="validatePassword"
          >
          <el-icon class="eye-icon" @click="togglePasswordVisibility">
            <component :is="showPassword ? View : Hide" />
          </el-icon>
        </div>
        <p class="field-err" v-if="errors.password">{{ errors.password }}</p>
      </div>

      <!-- 确认密码 -->
      <div class="field" :class="{ 'field-error': errors.confirmPassword }">
        <label>确认密码</label>
        <div class="input-box">
          <el-icon class="input-icon"><Lock /></el-icon>
          <input :type="showConfirmPassword ? 'text' : 'password'"
            v-model="supervisorForm.confirmPassword" placeholder="请再次输入密码"
            @input="validateConfirmPassword"
          >
          <el-icon class="eye-icon" @click="toggleConfirmPasswordVisibility">
            <component :is="showConfirmPassword ? View : Hide" />
          </el-icon>
        </div>
        <p class="field-err" v-if="errors.confirmPassword">{{ errors.confirmPassword }}</p>
      </div>

      <div class="button-box" @click="handleRegister">注 册</div>

      <div class="login-link" @click="goToLogin">已有账号？立即登录</div>
    </section>
  </div>
</template>

<script setup>
import { reactive, inject, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from 'element-plus';
import { User, UserFilled, Calendar, Male, Female, Lock, View, Hide } from '@element-plus/icons-vue';

const router = useRouter();
const axios = inject("axios");

// 手机号格式化：XXX XXXX XXXX
const formatPhone = (value) => {
  const digits = value.replace(/\D/g, '').slice(0, 11);
  if (digits.length <= 3) return digits;
  if (digits.length <= 7) return digits.slice(0, 3) + ' ' + digits.slice(3);
  return digits.slice(0, 3) + ' ' + digits.slice(3, 7) + ' ' + digits.slice(7);
};

const displayPhone = ref('');
const phoneTouched = ref(false);
const onPhoneInput = (e) => {
  const raw = e.target.value;
  displayPhone.value = formatPhone(raw);
  supervisorForm.telId = displayPhone.value.replace(/\s/g, '');
  phoneTouched.value = true;
};

const supervisorForm = reactive({
  telId: '', realName: '', birthday: '', sex: 1, password: '', confirmPassword: ''
});

const errors = reactive({ telId: '', realName: '', password: '', confirmPassword: '' });

const showPassword = ref(false);
const showConfirmPassword = ref(false);
const togglePasswordVisibility = () => { showPassword.value = !showPassword.value; };
const toggleConfirmPasswordVisibility = () => { showConfirmPassword.value = !showConfirmPassword.value; };

const validateTelId = (telId) => {
  if (!telId) return '请输入手机号码';
  if (!/^1[3-9]\d{9}$/.test(telId)) return '请输入正确的手机号码';
  return '';
};

const validateRealName = () => {
  const name = supervisorForm.realName.trim();
  if (!name) { errors.realName = '请输入真实姓名'; return false; }
  if (!/^[一-龥]{2,10}$/.test(name)) { errors.realName = '姓名应为2-10个中文字符'; return false; }
  errors.realName = ''; return true;
};

const validatePassword = () => {
  const pwd = supervisorForm.password;
  if (!pwd) { errors.password = '请输入密码'; return false; }
  if (pwd.length < 6 || pwd.length > 20) { errors.password = '密码长度为6-20位'; return false; }
  if (!/[a-zA-Z]/.test(pwd) || !/\d/.test(pwd)) { errors.password = '密码需包含字母和数字'; return false; }
  errors.password = '';
  if (supervisorForm.confirmPassword) validateConfirmPassword();
  return true;
};

const validateConfirmPassword = () => {
  if (!supervisorForm.confirmPassword) { errors.confirmPassword = '请再次输入密码'; return false; }
  if (supervisorForm.confirmPassword !== supervisorForm.password) { errors.confirmPassword = '两次密码输入不一致'; return false; }
  errors.confirmPassword = ''; return true;
};

const checkTelExists = async () => {
  if (!supervisorForm.telId) return;
  const err = validateTelId(supervisorForm.telId);
  if (err) { errors.telId = err; return; }
  try {
    const res = await axios.get(`supervisor/checkTel/${supervisorForm.telId}`);
    if (res.data === true) { errors.telId = '该手机号已注册'; ElMessage.warning('该手机号已注册'); }
    else errors.telId = '';
  } catch (e) { console.error(e); }
};

const handleRegister = async () => {
  errors.telId = validateTelId(supervisorForm.telId);
  if (errors.telId) return ElMessage.warning(errors.telId);
  if (!validateRealName()) return ElMessage.warning(errors.realName);
  if (!supervisorForm.birthday) return ElMessage.warning('请选择生日');
  if (!validatePassword()) return ElMessage.warning(errors.password);
  if (!validateConfirmPassword()) return ElMessage.warning(errors.confirmPassword);
  try {
    const data = {
      telId: supervisorForm.telId.trim(), realName: supervisorForm.realName.trim(),
      birthday: supervisorForm.birthday, sex: supervisorForm.sex, password: supervisorForm.password
    };
    const res = await axios.post('supervisor/saveSupervisor', data);
    let ok = false;
    if (typeof res.data === 'number') ok = res.data > 0;
    else if (res.data && typeof res.data === 'object') ok = res.data.code === 200 || res.data.code === 1;
    if (ok) { ElMessage.success('注册成功'); router.push('/login'); }
    else ElMessage.error(typeof res.data === 'object' ? (res.data.message || '注册失败') : '注册失败');
  } catch (error) {
    let msg = '网络错误，请稍后重试';
    if (error.response) {
      if (error.response.status === 500) msg = '服务器内部错误';
      else if (error.response.status === 400) msg = error.response.data?.message || '请求参数错误';
    } else if (error.request) msg = '无法连接到服务器';
    ElMessage.error(msg);
  }
};

const goToLogin = () => router.push('/login');
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
  color: #d9534f; 
  font-size: 20px; 
  font-weight: bold; 
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
  padding: 20px 30px 40px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15); 
  box-sizing: border-box;
  position: relative;
  z-index: 1;
}

/* ---- 输入域 ---- */
.field { margin-top: 16px; }
.field label { 
  display: block; 
  font-size: 14px; 
  color: #555; 
  margin-bottom: 6px; 
  font-weight: 500;
}
.field.field-error .input-box { border-color: #f56c6c; }

/* 输入框 */
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

/* 提示 & 错误 — 紧贴输入框下沿 */
.field-hint, .field-err {
  font-size: 12px; 
  margin: 3px 0 0 8px; 
  text-align: left; 
  line-height: 1.3;
}
.field-hint { color: #999; }
.field-err  { color: #f56c6c; }

/* 性别选择 */
.sex-row { 
  display: flex; 
  gap: 24px; 
  padding: 6px 0; 
}
.sex-row .radio-label { 
  display: flex; 
  align-items: center; 
  font-size: 16px; 
  cursor: pointer; 
  gap: 6px;
  transition: color 0.3s;
}
.sex-row .radio-label:hover {
  color: #74c7c3;
}
.radio-icon {
  font-size: 18px;
  color: #46a34f;
}
.sex-row .radio-label input { 
  margin-right: 6px; 
  width: 18px; 
  height: 18px; 
  accent-color: #46a34f;
}

/* 按钮 & 链接 */
.button-box {
  width: 100%; 
  height: 50px; 
  border-radius: 8px;
  background: linear-gradient(135deg, #46a34f 0%, #3d8f44 100%); 
  color: #fff; 
  text-align: center; 
  line-height: 50px;
  font-size: 18px; 
  letter-spacing: 2px; 
  cursor: pointer; 
  user-select: none;
  margin-top: 24px; 
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

.login-link { 
  text-align: center; 
  margin-top: 20px; 
  color: #46a34f; 
  font-size: 14px; 
  cursor: pointer; 
  transition: color 0.3s;
}
.login-link:hover { 
  color: #3d8f44; 
}
</style>
