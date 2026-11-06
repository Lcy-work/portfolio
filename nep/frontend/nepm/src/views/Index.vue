<template>
  <!-- el-container 弹性布局容器，column 方向排列（上下） -->
  <el-container style="display: flex; flex-direction: column; width: 100%; height: 100%">
    
    <!-- 顶部 Header -->
    <el-header style="flex: 0 0 60px; background: linear-gradient(90deg, #3949AB, #5C6BC0)">
      <div>
        <!-- 使用 Element Plus 图标 -->
        <el-icon style="font-size: 30px; margin-right: 8px"><Phone /></el-icon>
        <h1>东软环保公众监督平台管理系统</h1>
      </div>
      <div>
        <!-- 从 sessionStorage 读取管理员信息，展示中文实名 -->
        <p>系统管理员：{{ admins.adminName }}</p>
        <el-button type="danger" size="small" @click="handleLogout">
          退出系统&nbsp;&nbsp;<el-icon><DArrowRight /></el-icon>
        </el-button>
      </div>
    </el-header>

    <!-- 中间区域（侧边栏 + 主内容） -->
    <el-container style="flex: 1">
      
      <!-- 左侧导航菜单 -->
      <el-aside width="200px" style="background-color: #EEF2FF">
        <el-menu
          active-text-color="#3949AB"
          background-color="#EEF2FF"
          default-active="1"
          text-color="#5C6BC0"
          router
        >
          <!-- 一级菜单：公众监督数据管理 -->
          <el-sub-menu index="1">
            <template #title>
              <el-icon><ChatDotRound /></el-icon>
              <span>公众监督数据管理</span>
            </template>
            <el-menu-item index="/index/feedback/list">公众监督数据列表</el-menu-item>
            <el-menu-item index="/index/statistics/list">确认AQI数据列表</el-menu-item>
          </el-sub-menu>

          <!-- 一级菜单：统计数据管理 -->
          <el-sub-menu index="2">
            <template #title>
              <el-icon><DocumentCopy /></el-icon>
              <span>统计数据管理</span>
            </template>
            <el-menu-item index="/index/statistics/province">省分组检查统计</el-menu-item>
            <el-menu-item index="/index/statistics/aqi-distribute">AQI指数分布统计</el-menu-item>
            <el-menu-item index="/index/statistics/aqi-trend">AQI指数趋势统计</el-menu-item>
            <el-menu-item index="/index/statistics/other">其它数据统计</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!-- 右侧主内容区域：子路由在此渲染 -->
      <el-main>
        <router-view v-slot="{ Component }">
          <keep-alive :include="['FeedbackList', 'StatisticsList']">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-main>
    </el-container>

    <!-- 底部 Footer -->
    <el-footer style="flex: 0 0 60px; background: linear-gradient(90deg, #3949AB, #5C6BC0)">
      &copy; 版权所有 东软睿道 www.neusoft.com
    </el-footer>
  </el-container>
</template>

<script setup>
import { Phone, DArrowRight, ChatDotRound, DocumentCopy } from "@element-plus/icons-vue";
import { getSessionStorage, removeSessionStorage } from '../common.js';
import { useRouter } from 'vue-router';
import { ElMessageBox } from "element-plus";

// 从 sessionStorage 读取管理员信息
const admins = getSessionStorage('admins');
const router = useRouter();

// 退出登录：带二次确认的退出系统功能
const handleLogout = () => {
  ElMessageBox.confirm(
    '确定要退出系统吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'nepm-message-box'
    }
  ).then(() => {
    // 用户点击确定，清除 sessionStorage 并跳转登录页
    removeSessionStorage('admins');
    router.push('/login');
  }).catch(() => {
    // 用户点击取消，不做任何操作
  });
};
</script>

<style scoped>
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  user-select: none;
}
.el-header div {
  display: flex;
  align-items: center;
}
.el-header div h1 {
  font-weight: 500;
  font-size: 24px;
  letter-spacing: 1px;
}
.el-header div p {
  font-size: 16px;
  margin-right: 30px;
}
.el-footer {
  text-align: center;
  line-height: 60px;
  font-size: 14px;
  color: #fff;
}
</style>

<!-- 全局样式：自定义 MessageBox 按钮颜色 -->
<style>
.nepm-message-box .el-message-box__btns .el-button--primary {
  background: linear-gradient(135deg, #5C6BC0 0%, #3949AB 100%);
  border-color: #3949AB;
  color: white;
}

.nepm-message-box .el-message-box__btns .el-button--primary:hover {
  background: linear-gradient(135deg, #7986CB 0%, #5C6BC0 100%);
  border-color: #5C6BC0;
}

/* 取消按钮样式 - 使用更具体的选择器 */
.nepm-message-box .el-message-box__btns button.el-button:not(.el-button--primary) {
  color: #3949AB !important;
  border-color: #3949AB !important;
  background-color: white !important;
}

.nepm-message-box .el-message-box__btns button.el-button:not(.el-button--primary):hover {
  color: #5C6BC0 !important;
  border-color: #5C6BC0 !important;
  background-color: rgba(57, 73, 171, 0.1) !important;
}
</style>
