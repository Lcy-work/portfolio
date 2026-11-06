<!-- src/views/FeedbackList.vue -->
<template>
  <div class="wrapper">
    <div class="fb-header-box">
      <div class="top-ban"></div>
      <header>
        <span class="back-btn" @click="toSelectGrid">← 返回</span>
        <span>历史反馈</span>
        <span class="logout-btn" @click="logout">退出</span>
      </header>
    </div>

    <!-- 空状态提示 -->
    <div v-if="aqiFeedbackArr.length === 0" class="empty-state">
      <el-icon class="empty-icon"><Document /></el-icon>
      <p class="empty-text">暂无反馈记录</p>
      <p class="empty-hint">快去提交您的第一条环保反馈吧~</p>
    </div>

    <!-- 反馈列表 -->
    <ul v-else>
      <li v-for="item in aqiFeedbackArr" :key="item.afId">
        <div class="fb-left">
          <i :style="{ backgroundColor: getAqiColor(item.aqi?.aqiId) }">{{ (item.aqi?.chineseExplain || '—') + '（' + (aqiRangeMap[item.aqi?.aqiId] || '') + '）' }}</i>
          <span class="fb-desc">{{ item.information || '无描述' }}</span>
        </div>
        <div class="fb-right">{{ item.afDate }} {{ item.afTime }}</div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, inject, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getSessionStorage, removeSessionStorage } from "../common.js";
import { ElMessage, ElMessageBox, ElIcon } from 'element-plus';
import { Document } from '@element-plus/icons-vue';

const router = useRouter();
const axios = inject("axios");
const supervisor = getSessionStorage('supervisor');

const aqiFeedbackArr = ref([]);
const aqiColors = {1:'#00C800',2:'#C49400',3:'#E07000',4:'#D00000',5:'#DC143C',6:'#8B0000'};
const aqiRangeMap = {1:'一', 2:'二', 3:'三', 4:'四', 5:'五', 6:'六'};
const getAqiColor = (id) => aqiColors[id] || '#999';

const loadFeedbackList = async () => {
  if (!supervisor || !supervisor.telId) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  try {
    const response = await axios.post('aqiFeedback/listAqiFeedbackByTelId', {
      telId: supervisor.telId
    });
    if (response.data && Array.isArray(response.data)) {
      aqiFeedbackArr.value = response.data;
    } else {
      ElMessage.error('加载失败');
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试');
    console.error(error);
  }
};

onMounted(() => {
  loadFeedbackList();
});

const toSelectGrid = () => {
  router.push('/selectGrid');
};

const logout = () => {
  ElMessageBox.confirm('确定要退出系统吗？', '退出登录', {
    confirmButtonText: '退出',
    cancelButtonText: '取消',
    type: 'warning',
    customClass: 'custom-message-box',
    confirmButtonClass: 'custom-confirm-btn',
    cancelButtonClass: 'custom-cancel-btn'
  }).then(() => {
    removeSessionStorage('supervisor');
    router.push('/login');
  }).catch(() => {});
};
</script>

<style scoped>
.fb-header-box { width: 100%; }
.fb-header-box .top-ban {
  width: 100%;
  height: 4px;
}
.fb-header-box header {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, #46a34f 0%, #3d8f44 100%);
  color: #FFF;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
  padding: 0 16px;
  font-size: 18px;
  font-weight: 600;
}
.fb-header-box i {
  font-size: 20px;
  font-style: normal;
}
.back-btn, .logout-btn {
  font-size: 14px;
  font-weight: 400;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.2s;
}
.back-btn:hover, .logout-btn:hover {
  background: rgba(255,255,255,0.2);
}

/* 空状态样式 */
.empty-state {
  width: 100%;
  margin-top: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}
.empty-icon {
  font-size: 100px;
  margin-bottom: 24px;
  color: #7bbd8f;
  opacity: 0.85;
}
.empty-text {
  font-size: 20px;
  color: #5a8f6b;
  margin: 0 0 12px 0;
  font-weight: 600;
  letter-spacing: 1px;
}
.empty-hint {
  font-size: 15px;
  color: #8fb89e;
  margin: 0;
  font-weight: 400;
  line-height: 1.6;
}

ul {
  width: 92%;
  margin: 74px auto 0;
  padding: 0 0 24px;
  background-color: transparent;
  color: #555;
}
ul li {
  width: 100%;
  border: solid 1px #C6E2FF;
  box-sizing: border-box;
  padding: 12px 16px;
  font-size: 15px;
  line-height: 1.4;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0 0 10px 0;
  background-color: #ECF5FF;
  border-radius: 10px;
  gap: 10px;
}
ul li:last-child { margin-bottom: 0; }
.fb-left {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 0;
}
.fb-left i {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  min-width: 40px;
  height: 28px;
  line-height: 28px;
  padding: 0 10px;
  font-size: 14px;
  font-weight: 700;
  border-radius: 14px;
  color: #FFF;
  margin-right: 12px;
  font-style: normal;
  white-space: nowrap;
  flex-shrink: 0;
  border: 1px solid rgba(0,0,0,0.12);
}
.fb-desc {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  color: #666;
}
.fb-right {
  font-size: 13px;
  color: #999;
  white-space: nowrap;
  flex-shrink: 0;
}
</style>

<style>
/* 自定义退出确认对话框按钮样式 - 全局样式 */
.custom-message-box {
  border-radius: 12px !important;
}

/* 退出按钮 - 主要按钮 */
.custom-message-box .el-button--primary,
.custom-message-box .el-button.el-button--primary {
  background: linear-gradient(135deg, #46a34f 0%, #3d8f44 100%) !important;
  border-color: #46a34f !important;
  color: #fff !important;
  box-shadow: 0 4px 12px rgba(70, 163, 79, 0.3);
}

.custom-message-box .el-button--primary:hover,
.custom-message-box .el-button.el-button--primary:hover {
  background: linear-gradient(135deg, #3d8f44 0%, #357a3b 100%) !important;
  border-color: #3d8f44 !important;
  box-shadow: 0 6px 16px rgba(70, 163, 79, 0.4);
}

/* 取消按钮 - 默认按钮 - 强制覆盖所有相关样式 */
.custom-message-box .el-button--default,
.custom-message-box .el-button.is-default,
.custom-message-box button.el-button--default,
.custom-message-box .el-button:not(.el-button--primary):not(.el-button--success):not(.el-button--warning):not(.el-button--danger):not(.el-button--info) {
  background: #ffffff !important;
  background-color: #ffffff !important;
  background-image: none !important;
  border-color: #46a34f !important;
  color: #46a34f !important;
}

.custom-message-box .el-button--default:hover,
.custom-message-box .el-button.is-default:hover,
.custom-message-box button.el-button--default:hover {
  background: rgba(70, 163, 79, 0.1) !important;
  background-color: rgba(70, 163, 79, 0.1) !important;
  border-color: #3d8f44 !important;
  color: #3d8f44 !important;
}

.custom-message-box .el-button--default:focus,
.custom-message-box .el-button--default:focus-visible,
.custom-message-box .el-button.is-default:focus,
.custom-message-box .el-button.is-default:focus-visible {
  background: rgba(70, 163, 79, 0.1) !important;
  background-color: rgba(70, 163, 79, 0.1) !important;
  border-color: #3d8f44 !important;
  color: #3d8f44 !important;
}

/* 覆盖 Element Plus 的 CSS 变量 */
.custom-message-box {
  --el-button-default-bg-color: #ffffff !important;
  --el-button-default-border-color: #46a34f !important;
  --el-button-default-text-color: #46a34f !important;
  --el-button-default-hover-bg-color: rgba(70, 163, 79, 0.1) !important;
  --el-button-default-hover-border-color: #3d8f44 !important;
  --el-button-default-hover-text-color: #3d8f44 !important;
}
</style>