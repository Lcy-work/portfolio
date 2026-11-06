<template>
  <el-container class="layout-container-main">
    <!-- 侧边栏 -->
    <el-aside :width="asideMenuShow ? '220px' : '60px'" class="sidebar-container transition-all">
      <div class="sidebar-logo-container">
        <router-link to="/home" class="logo-link">
          <img src="../assets/yiliaojigou.png" class="sidebar-logo" />
          <h1 class="sidebar-title" v-show="asideMenuShow">东软医保报销系统</h1>
        </router-link>
      </div>
      <el-menu
        active-text-color="#2C6ECB"
        background-color="#F8FAFC"
        class="el-menu-vertical-demo"
        default-active="2"
        text-color="#4B5563"
        @select="handleSelect"
        border-right="1px solid #E2E8F0"
      >
        <el-menu-item index="/home" class="menu-item">
          <el-icon class="menu-icon"><HomeFilled /></el-icon>
          <span v-show="asideMenuShow" class="menu-text">参保人员信息管理</span>
        </el-menu-item>
        <el-menu-item index="/patientpay" class="menu-item">
          <el-icon class="menu-icon"><Coin /></el-icon>
          <span v-show="asideMenuShow" class="menu-text">参保人员费用查询</span>
        </el-menu-item>
        <el-menu-item index="/patientreimbursement" class="menu-item">
          <el-icon class="menu-icon"><IconMenu /></el-icon>
          <span v-show="asideMenuShow" class="menu-text">参保人员费用报销</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="header-container">
        <div @click="toggleClick" class="hamburger-container">
          <el-icon class="hamburger" v-if="asideMenuShow"><Fold /></el-icon>
          <el-icon class="hamburger" v-else><Expand /></el-icon>
        </div>
        <div class="breadcrumb-container">
          <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="right-menu">
          <el-dropdown class="user-dropdown">
            <div class="user-info">
              <el-avatar class="user-avatar" icon="User" size="small"></el-avatar>
              <span class="user-name">{{ loginName }}</span>
              <el-icon class="dropdown-icon"><ChevronDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>系统设置</el-dropdown-item>
                <el-dropdown-divider />
                <el-dropdown-item @click="backToLogin()" class="logout-item">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 主内容 -->
      <el-main class="content-container">
        <div class="content-wrapper">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { HomeFilled, Menu as IconMenu, Fold, Expand, User, ChevronDown, Coin } from '@element-plus/icons-vue';
import { useRouter, useRoute } from "vue-router";
import { ref, onMounted, watch } from "vue";

const router = useRouter();
const route = useRoute();
const asideMenuShow = ref(true);
const loginName = ref('');
const currentRouteName = ref('');

// 监听路由变化更新面包屑
watch(() => route.path, (newPath) => {
  const routeMap = {
    '/home': '参保人员信息管理',
    '/patientpay': '参保人员费用查询',
    '/patientreimbursement': '参保人员费用报销'
  };
  currentRouteName.value = routeMap[newPath] || '';
}, { immediate: true });

// 菜单选择处理
const handleSelect = (index) => {
  router.push(index);
}

// 切换侧边栏显示状态
const toggleClick = () => {
  asideMenuShow.value = !asideMenuShow.value;
}
 
// 退出登录
const backToLogin = () => {
  window.localStorage.removeItem("user");
  router.push('/');
}

// 初始化
onMounted(() => {
  let user = window.localStorage.getItem('user');
  if (user) {
    try {
      const userData = JSON.parse(user);
      loginName.value = userData.loginName || '用户';
    } catch (e) {
      backToLogin();
    }
  } else {
    backToLogin();
  }
})
</script>

<style scoped>
/* 基础样式 */
.transition-all {
  transition: all 0.3s ease;
}

/* 整体容器 */
.layout-container-main {
  height: 100vh;
  background-color: #F1F5F9;
}

/* 侧边栏 */
.sidebar-container {
  background-color: #F8FAFC;
  box-shadow: 1px 0 3px rgba(0, 0, 0, 0.05);
  z-index: 10;
}

/* 侧边栏logo */
.sidebar-logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #E2E8F0;
  padding: 0 15px;
}

.logo-link {
  display: flex;
  align-items: center;
  width: 100%;
  text-decoration: none;
}

.sidebar-logo {
  width: 32px;
  height: 32px;
  border-radius: 4px;
}

.sidebar-title {
  margin: 0 0 0 12px;
  color: #2C3E50;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s;
}

/* 菜单样式 */
.el-menu-vertical-demo {
  border-right: none;
  padding-top: 15px;
}

.menu-item {
  height: 50px !important;
  line-height: 50px !important;
  margin: 0 8px !important;
  border-radius: 6px !important;
  transition: all 0.2s ease !important;
}

.menu-icon {
  font-size: 18px;
  margin-right: 12px;
}

.menu-text {
  font-size: 14px;
}

.el-menu-item:hover {
  background-color: #EFF6FF !important;
  color: #2C6ECB !important;
}

.el-menu-item.is-active {
  background-color: #E0EDFF !important;
  color: #2C6ECB !important;
  font-weight: 500;
}

/* 顶部导航 */
.header-container {
  background-color: #FFFFFF;
  height: 60px;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.hamburger-container {
  cursor: pointer;
  color: #4B5563;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.hamburger-container:hover {
  background-color: #F1F5F9;
}

.hamburger {
  font-size: 20px;
}

.breadcrumb-container {
  flex: 1;
  margin: 0 20px;
}

.breadcrumb {
  font-size: 13px;
  color: #64748B;
}

.right-menu {
  display: flex;
  align-items: center;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 10px;
  border-radius: 20px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #F1F5F9;
}

.user-avatar {
  background-color: #E0EDFF;
  color: #2C6ECB;
}

.user-name {
  font-size: 14px;
  color: #334155;
  font-weight: 500;
}

.dropdown-icon {
  font-size: 14px;
  color: #94A3B8;
}

.logout-item {
  color: #DC2626;
}

/* 主内容区域 */
.content-container {
  background-color: #F8FAFC;
  padding: 24px;
  height: calc(100vh - 60px);
}

.content-wrapper {
  background-color: #FFFFFF;
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  padding: 20px;
  height: 100%;
  overflow: auto;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #F1F5F9;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #CBD5E1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94A3B8;
}

/* 动画过渡 */
.transition-all {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
</style>