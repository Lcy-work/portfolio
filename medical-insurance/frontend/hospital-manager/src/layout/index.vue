<template>
  <div class="main-layout">
    <el-container class="app-container">
      <!-- 左侧导航栏 -->
      <el-aside 
        class="navigation-sidebar" 
        :width="sidebarWidth"
        v-show="isSidebarVisible"
      >
        <div class="logo-area">
          <router-link to="/home">
            <img src="../assets/医疗机构.png" class="logo-img"/>
            <span class="system-name">东软医保报销：医院</span>
          </router-link>
        </div>
        
        <el-menu 
          class="main-menu"
          active-text-color="#2c3e50" 
          background-color="#f8f9fa"
          text-color="#495057"
          :default-active="activeMenu"
          @select="navigateTo"
          border-right="1px solid #dee2e6"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>入院登记</span>
          </el-menu-item>
          
          <el-sub-menu index="diagnosis-group">
            <template #title>
              <el-icon><icon-menu /></el-icon>
              <span>患者诊断</span>
            </template>
            <el-menu-item index="/patientdisease">患者选择</el-menu-item>
            <el-menu-item index="/adminssiondisease">入院诊断</el-menu-item>
            <el-menu-item index="/maindisease">主要诊断</el-menu-item>
            <el-menu-item index="/otherdisease">其他诊断</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="order-group">
            <template #title>
              <el-icon><Coin /></el-icon>
              <span>患者医嘱</span>
            </template>
            <el-menu-item index="/patientorder">患者选择</el-menu-item>
            <el-menu-item index="/drugorder">药品处方医嘱</el-menu-item>
            <el-menu-item index="/diagnosistreatmentorder">诊疗项目医嘱</el-menu-item>
            <el-menu-item index="/medicalserviceorder">医疗服务医嘱</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-container class="content-container">
        <el-header class="app-header">
          <div class="header-left">
            <el-icon 
              class="toggle-btn" 
              @click="toggleSidebar"
            >
              <Fold />
            </el-icon>
          </div>
          
          <div class="header-right">
            <el-dropdown class="user-dropdown">
              <el-icon class="setting-icon"><Setting /></el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>{{ currentUser }}</el-dropdown-item>
                  <el-dropdown-item @click="logout">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="page-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { HomeFilled, Menu as IconMenu, Fold, Setting, Coin } from '@element-plus/icons-vue';
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";

const router = useRouter();
const isSidebarVisible = ref(true);
const currentUser = ref('');
const sidebarWidth = '200px';
const activeMenu = ref('2');

const navigateTo = (path) => { 
  router.push(path);
};

const logout = () => {
  window.localStorage.removeItem("user");
  router.push('/');
};

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

onMounted(() => {
  const userData = window.localStorage.getItem('user');
  if (!userData) {
    logout();
    return;
  }
  
  try {
    const user = JSON.parse(userData);
    currentUser.value = user.loginName || '';
  } catch {
    logout();
  }
});
</script>

<style scoped>
.main-layout {
  height: 100vh;
  overflow: hidden;
}

.app-container {
  height: 100%;
}

.navigation-sidebar {
  background: #f8f9fa;
  height: 100vh;
  transition: width 0.3s;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.05);
}

.logo-area {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #dee2e6;
  background-color: #ffffff;
}

.logo-img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.system-name {
  color: #2c3e50;
  font-size: 14px;
  font-weight: bold;
}

.main-menu {
  border-right: 1px solid #dee2e6;
}

.content-container {
  display: flex;
  flex-direction: column;
}

.app-header {
  background: #ffffff;
  color: #495057;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
  border-bottom: 1px solid #dee2e6;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.toggle-btn, .setting-icon {
  color: #495057;
  cursor: pointer;
  font-size: 18px;
  transition: color 0.2s;
}

.toggle-btn:hover, .setting-icon:hover {
  color: #007bff;
}

.page-content {
  padding: 20px;
  background: #ffffff;
  min-height: calc(100vh - 50px);
  color: #343a40;
}
</style>