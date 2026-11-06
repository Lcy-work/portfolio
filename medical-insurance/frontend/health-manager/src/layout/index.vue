<template>
  <el-container class="layout-container-main">
    <!-- 侧边栏导航 -->
    <el-aside 
      :width="asideMenuShow ? '220px' : '64px'" 
      transition="aside-transition"
      class="sidebar-container"
    >
      <div class="sidebar-header">
        <router-link to="/home" class="sidebar-logo-link">
          <img src="../assets/Neutech.png" class="sidebar-logo" alt="东软医保"/>
          <span class="sidebar-title" :class="{ 'hidden-title': !asideMenuShow }">
            东软医保报销：管理
          </span>
        </router-link>
      </div>
      
      <el-menu
        :collapse="!asideMenuShow"
        active-text-color="#1677ff"
        background-color="#fff"
        text-color="#4e5969"
        class="sidebar-menu"
        default-active="2"
        @select="handleSelect"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><IconMenu /></el-icon>
            <span>数据信息维护</span>
          </template>
          <el-menu-item index="/drugManager">
            <el-icon><FirstAidKit /></el-icon>
            <span>医保药品数据维护</span>
          </el-menu-item>
          <el-menu-item index="/diagnosistreatment">
            <el-icon><Operation /></el-icon>
            <span>诊疗项目数据维护</span>
          </el-menu-item>
          <el-menu-item index="/medicalservice">
            <el-icon><OfficeBuilding /></el-icon>
            <span>医疗服务设施数据维护</span>
          </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="2">
          <template #title>
            <el-icon><Coin /></el-icon>
            <span>报销比例维护</span>
          </template>
          <el-menu-item index="/drugreimbursement">
            <el-icon><PriceTag /></el-icon>
            <span>药品报销比例</span>
          </el-menu-item>
          <el-menu-item index="/tertiaryhospital">
            <el-icon><House /></el-icon>
            <span>三级医院报销比例</span>
          </el-menu-item>
          <el-menu-item index="/secondaryhopsital">
            <el-icon><House /></el-icon>
            <span>二级医院报销比例</span>
          </el-menu-item>
          <el-menu-item index="/firstlevelhospital">
            <el-icon><House /></el-icon>
            <span>一级医院报销比例</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航栏 -->
      <el-header class="header-container">
        <div @click="toggleClick" class="menu-toggle">
          <el-icon><Fold /></el-icon>
        </div>
        
        <div class="breadcrumb-container">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="user-menu">
          <el-dropdown class="user-dropdown">
            <div class="user-info">
              <el-avatar size="small" icon="User" class="user-avatar"></el-avatar>
              <span class="user-name" :class="{ 'hidden-name': !asideMenuShow }">
                {{ loginName }}
              </span>
              <el-icon class="user-caret"><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="user-dropdown-menu">
                <el-dropdown-item icon="Setting">个人设置</el-dropdown-item>
                <el-dropdown-item icon="QuestionFilled">帮助中心</el-dropdown-item>
                <el-dropdown-item icon="TurnOff" @click="backToLogin">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 主要内容 -->
      <el-main class="content-container">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { 
  Menu as IconMenu, 
  Fold, 
  Setting, 
  Coin, 
  CaretBottom, 
  User,
  TurnOff,  // 替换 Logout
  QuestionFilled,  // 替换 Question
  FirstAidKit,  // 替换 MedicineBox
  Operation,  // 替换 Stethoscope
  OfficeBuilding,  // 替换 Building
  PriceTag,  // 替换 Percent
  House  // 替换 Hospital
} from "@element-plus/icons-vue";

const router = useRouter();
const asideMenuShow = ref(true);
const loginName = ref("");

// 根据路由获取当前页面标题
const currentPageTitle = computed(() => {
  const routeMap = {
    "/drugManager": "医保药品数据维护",
    "/diagnosistreatment": "诊疗项目数据维护",
    "/medicalservice": "医疗服务设施数据维护",
    "/drugreimbursement": "药品报销比例",
    "/tertiaryhospital": "三级医院报销比例",
    "/secondaryhopsital": "二级医院报销比例",
    "/firstlevelhospital": "一级医院报销比例"
  };
  return routeMap[router.currentRoute.value.path] || "系统管理";
});

// 导航菜单选择
const handleSelect = (index) => {
  router.push(index);
};

// 退出登录
const backToLogin = () => {
  window.localStorage.removeItem("user");
  router.push("/");
};

// 切换侧边栏显示/隐藏
const toggleClick = () => {
  asideMenuShow.value = !asideMenuShow.value;
};

// 页面加载时获取用户信息
onMounted(() => {
  const user = window.localStorage.getItem("user");
  if (!user) {
    backToLogin();
  } else {
    try {
      const userData = JSON.parse(user);
      if (!userData.loginName) {
        backToLogin();
      } else {
        loginName.value = userData.loginName;
      }
    } catch (error) {
      console.error("解析用户信息失败", error);
      backToLogin();
    }
  }
});
</script>

<style scoped>
/* 侧边栏样式 */
.sidebar-container {
  background-color: #fff;
  border-right: 1px solid #e5e7eb;
  transition: width 0.3s ease;
}

.sidebar-header {
  height: 56px;
  line-height: 56px;
  padding: 0 16px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
}

.sidebar-logo-link {
  display: flex;
  align-items: center;
  color: #1f2329;
  text-decoration: none;
}

.sidebar-logo {
  width: 28px;
  height: 28px;
  object-fit: contain;
}

.sidebar-title {
  margin-left: 12px;
  font-size: 16px;
  font-weight: 500;
  transition: opacity 0.3s ease, width 0.3s ease;
  white-space: nowrap;
  overflow: hidden;
}

.hidden-title {
  opacity: 0;
  width: 0;
  margin-left: 0;
}

.sidebar-menu {
  border-right: none;
}

/* 顶部导航栏样式 */
.header-container {
  background-color: #fff;
  height: 56px;
  line-height: 56px;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.menu-toggle {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.menu-toggle:hover {
  background-color: #f5f5f5;
}

.breadcrumb-container {
  margin-left: 16px;
  flex: 1;
}

.user-menu {
  display: flex;
  align-items: center;
}

.user-dropdown {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 0 12px;
  height: 36px;
  border-radius: 18px;
  transition: background-color 0.2s ease;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.user-avatar {
  margin-right: 8px;
}

.user-name {
  margin-right: 4px;
  font-size: 14px;
  transition: opacity 0.3s ease;
}

.hidden-name {
  opacity: 0;
  width: 0;
  margin-right: 0;
}

.user-caret {
  font-size: 12px;
  transition: transform 0.2s ease;
}

.user-dropdown:hover .user-caret {
  transform: rotate(180deg);
}

.user-dropdown-menu {
  min-width: 160px;
}

/* 主内容区样式 */
.content-container {
  padding: 20px;
  background-color: #f5f7fa;
  overflow: auto;
}

/* 动画效果 */
.aside-transition {
  transition: width 0.3s ease;
}

/* 统一替换 ::v-deep 为 :deep()（如果有相关样式） */
:deep(.el-menu-item.is-active) {
  background-color: #e6f7ff !important;
}

:deep(.el-sub-menu__title:hover) {
  background-color: #f5f7fa !important;
}
</style>