<!-- src/views/FeedbackList.vue — NEPG 网格员反馈任务列表 -->
<template>
  <div class="wrapper">
    <!-- 顶部导航栏 -->
    <div class="fl-header-box">
      <header>
        <i class="fa fa-angle-left" @click="goBack"></i>
        <span>反馈任务列表</span>
        <i class="fa fa-bars" @click="toggleMenu"></i>
      </header>
      <div class="fl-top-ban"></div>
    </div>

    <div class="fl-content">
      <!-- 统计卡片 -->
      <div class="stats-row" v-if="aqiFeedbackArrSize > 0">
        <div class="stat-card">
          <span class="stat-num">{{ aqiFeedbackArrSize }}</span>
          <span class="stat-label">待处理任务</span>
        </div>
        <div class="stat-card">
          <span class="stat-num">{{ gridMember?.gmName || '网格员' }}</span>
          <span class="stat-label">当前用户</span>
        </div>
      </div>

      <!-- 任务列表 -->
      <div class="list-container" v-if="aqiFeedbackArrSize > 0">
        <div
          class="task-card"
          v-for="item in aqiFeedbackArr"
          :key="item.afId"
          @click="goToDetail(item.afId)"
        >
          <div class="task-left">
            <div
              class="task-level-badge"
              :style="{ backgroundColor: getLevelColor(item.aqi?.aqiId) }"
            >
              {{ item.aqi?.chineseExplain || '待' }}
            </div>
            <div class="task-info">
              <p class="task-location">
                {{ item.gridProvince?.provinceName || '' }} {{ item.gridCity?.cityName || '' }} &nbsp; {{ item.afDate || '' }}
              </p>
              <p class="task-address">{{ item.address || '未知地址' }}</p>
            </div>
          </div>
          <div class="task-right">
            <span class="go-btn">去检测</span>
            <i class="fa fa-angle-right"></i>
          </div>
        </div>
      </div>

      <!-- 无数据提示 -->
      <div v-else class="no-data">
        <i class="fa fa-inbox" style="font-size:48px;color:#ccc;display:block;margin-bottom:12px;"></i>
        <p>暂无反馈任务</p>
      </div>
    </div>

    <!-- 底部 -->
    <footer>
      <img src="@/assets/bottom.png" alt="footer" />
    </footer>
  </div>
</template>

<script setup>
import { ref, inject, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getSessionStorage, removeSessionStorage } from "../common.js";
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const axios = inject("axios");
const gridMember = getSessionStorage("gridMember");

const aqiFeedbackArr = ref([]);
const aqiFeedbackArrSize = ref(0);

// AQI等级颜色映射
const levelColors = {
  1: '#02E300',
  2: '#FFD700',
  3: '#FFA500',
  4: '#FF6347',
  5: '#DC143C',
  6: '#8B0000'
};

const getLevelColor = (levelId) => {
  return levelColors[levelId] || '#999999';
};

const init = async () => {
  try {
    if (!gridMember || !gridMember.gmId) {
      ElMessage.warning('请先登录');
      router.push('/login');
      return;
    }

    console.log('========== 反馈任务列表调试信息 ==========');
    console.log('当前用户信息:', gridMember);
    console.log('用户ID (gmId):', gridMember.gmId);
    console.log('用户姓名:', gridMember.gmName);

    const requestData = {
      gmId: gridMember.gmId,
      state: 1
    };
    
    console.log('请求参数:', requestData);
    console.log('=========================================');

    const response = await axios.post('aqiFeedback/listAqiFeedbackByGmId', requestData);

    console.log('接口响应状态:', response.status);
    console.log('返回数据类型:', typeof response.data);
    console.log('返回数据是否为数组:', Array.isArray(response.data));
    console.log('返回数据:', response.data);

    if (response.data && Array.isArray(response.data)) {
      aqiFeedbackArr.value = response.data;
      aqiFeedbackArrSize.value = aqiFeedbackArr.value.length;
      
      console.log('========== 查询结果 ==========');
      console.log('任务列表长度:', aqiFeedbackArrSize.value);
      console.log('任务详情:', aqiFeedbackArr.value);
      console.log('==============================');
      
      if (aqiFeedbackArrSize.value === 0) {
        ElMessage.info('暂无待处理任务。请在NEPM管理端将NEPS的反馈任务分配给您。');
      }
    } else {
      console.error('返回数据格式错误:', response.data);
      ElMessage.error('获取任务列表失败：数据格式错误');
    }
  } catch (error) {
    console.error('========== 请求错误 ==========');
    console.error('错误类型:', error.constructor.name);
    console.error('错误消息:', error.message);
    console.error('完整错误:', error);
    console.error('==============================');
    ElMessage.error('网络错误，请稍后重试');
  }
};

onMounted(() => {
  init();
});

const goToDetail = (afId) => {
  router.push({
    path: '/feedbackInfo',
    query: { afId: afId }
  });
};

const toggleMenu = () => {
  logout();
};

const goBack = () => {
  router.back();
};

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    removeSessionStorage('gridMember');
    ElMessage.success('已退出登录');
    router.push('/login');
  }).catch(() => {});
};
</script>

<style scoped>
/* ========== 全屏填充布局，覆盖底部 bottom.png ========== */
.wrapper {
  display: flex !important;
  flex-direction: column !important;
  min-height: 100vh !important;
  padding-bottom: 0 !important;
}

.fl-header-box {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.fl-header-box header {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, #42A5F5 0%, #1565C0 100%) !important;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-sizing: border-box;
  color: #FFF !important;
}

.fl-header-box header span {
  color: #FFF;
  font-size: 18px;
  font-weight: 600;
}

.fl-header-box header .fa {
  color: #FFF;
  font-size: 20px;
  cursor: pointer;
}

.fl-top-ban {
  width: 100%;
  height: 4px !important;
  max-height: 4px !important;
  background: rgba(255, 255, 255, 0.3);
}

/* ========== 主体内容区域 — 自动撑满剩余空间 ========== */
.fl-content {
  flex: 1;
  margin-top: 54px;
  padding-bottom: 24px;
}

/* ========== 统计卡片 ========== */
.stats-row {
  display: flex;
  gap: 12px;
  padding: 16px 4vw;
  margin-top: 4px;
}

.stat-card {
  flex: 1;
  background: #FFF;
  border-radius: 12px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-num {
  font-size: 22px;
  font-weight: 700;
  color: #1565C0;
}

.stat-label {
  font-size: 13px;
  color: #999;
}

/* ========== 任务列表 ========== */
.list-container {
  padding: 0 4vw;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-card {
  background: #FFF;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  cursor: pointer;
  transition: transform 0.15s, box-shadow 0.15s;
}
.task-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(21, 101, 192, 0.15);
}
.task-card:active {
  transform: scale(0.98);
}

.task-left {
  display: flex;
  align-items: center;
  gap: 14px;
  flex: 1;
  min-width: 0;
}

.task-level-badge {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  color: #FFF;
  font-size: 18px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.task-info {
  flex: 1;
  min-width: 0;
}

.task-location {
  font-size: 15px;
  color: #333;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 6px;
}

.task-address {
  font-size: 13px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.task-right {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
  margin-left: 12px;
}

.go-btn {
  font-size: 14px;
  color: #1565C0;
  font-weight: 600;
}

.task-right .fa {
  font-size: 20px;
  color: #1565C0;
}

/* ========== 无数据 — 全屏居中 ========== */
.no-data {
  position: fixed;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #999;
  font-size: 14px;
  z-index: 1;
}

/* ========== 底部 — 隐藏覆盖 ========== */
footer {
  display: none !important;
}
</style>
