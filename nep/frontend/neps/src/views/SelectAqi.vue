<!-- src/views/SelectAqi.vue -->
<template>
  <div class="wrapper">
    <header>
      <img src="@/assets/header.png" alt="环保主题插图">
    </header>

    <h1>东软环保公众监督平台</h1>
    <p class="subtitle">填写举报信息</p>

    <section>
      <!-- 基本信息展示 -->
      <div class="info-card">
        <div class="info-item">
          <span class="label">省份：</span>
          <span class="value">{{ route.query.provinceName || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">城市：</span>
          <span class="value">{{ route.query.cityName || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">地址：</span>
          <span class="value">{{ route.query.address || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">日期：</span>
          <span class="value">{{ getCurDate() }}</span>
        </div>
        <div class="info-item">
          <span class="label">时间：</span>
          <span class="value">{{ getCurTime() }}</span>
        </div>
      </div>

      <!-- AQI指数选择 -->
      <div class="form-item">
        <label>AQI指数范围</label>
        <el-select
          v-model="aqiId"
          placeholder="请选择AQI指数范围"
          size="large"
          style="width: 100%"
        >
          <el-option
            v-for="item in aqiArr"
            :key="item.id"
            :label="item.level + '（' + item.range + '）'"
            :value="item.id"
          >
            <span class="aqi-text" :style="{ color: item.color }">{{ item.level }}</span>
            <span class="aqi-range">({{ item.range }})</span>
          </el-option>
        </el-select>
      </div>

      <!-- 举报详情 -->
      <div class="form-item">
        <label>举报详情</label>
        <el-input
          v-model="information"
          type="textarea"
          :rows="5"
          placeholder="请详细描述您发现的污染情况，包括污染源类型、具体位置、影响范围等..."
          maxlength="500"
          show-word-limit
        />
      </div>

      <div class="button-box" @click="handleSubmit">
        提 交
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, inject, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getSessionStorage, getCurDate, getCurTime } from "../common.js";
import { ElMessage } from 'element-plus';

const router = useRouter();
const route = useRoute();
const axios = inject("axios");

// AQI数据 - 真实的空气质量指数分级
const defaultAqiData = [
  { id: 1, level: '优', range: '一', color: '#00C800', description: '空气质量令人满意，基本无空气污染' },
  { id: 2, level: '良', range: '二', color: '#C49400', description: '空气质量可接受，对极少数异常敏感人群健康有较弱影响' },
  { id: 3, level: '轻度污染', range: '三', color: '#E07000', description: '易感人群症状有轻度加剧，健康人群出现刺激症状' },
  { id: 4, level: '中度污染', range: '四', color: '#D00000', description: '进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响' },
  { id: 5, level: '重度污染', range: '五', color: '#99004C', description: '心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群中普遍出现症状' },
  { id: 6, level: '严重污染', range: '六', color: '#7E0023', description: '健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病' }
];

const aqiArr = ref([]);
const aqiId = ref(null);
const information = ref("");

const supervisor = getSessionStorage('supervisor');

// AQI等级颜色映射
const aqiColorMap = {1:'#00C800', 2:'#C49400', 3:'#E07000', 4:'#D00000', 5:'#99004C', 6:'#7E0023'};
const aqiRangeMap = {1:'一', 2:'二', 3:'三', 4:'四', 5:'五', 6:'六'};

// 加载AQI列表（优先使用后端数据，失败则使用默认数据）
const loadAqiList = async () => {
  try {
    const response = await axios.post('aqi/listAqiAll');
    if (response.data && response.data.length > 0) {
      // 后端字段名与模板不匹配，做归一化映射
      aqiArr.value = response.data.map(item => ({
        id: item.aqiId,
        level: item.chineseExplain,
        range: aqiRangeMap[item.aqiId] || '',
        color: aqiColorMap[item.aqiId] || '#999',
        description: item.aqiExplain || ''
      }));
    } else {
      aqiArr.value = defaultAqiData;
    }
  } catch (error) {
    console.error('加载AQI列表失败，使用默认数据:', error);
    aqiArr.value = defaultAqiData;
  }
};

const handleSubmit = async () => {
  if (!aqiId.value) {
    ElMessage.warning('请选择AQI指数范围');
    return;
  }
  if (!information.value || !information.value.trim()) {
    ElMessage.warning('请填写举报详情');
    return;
  }

  const feedbackData = {
    telId: supervisor?.telId,
    provinceId: Number(route.query.provinceId),
    cityId: Number(route.query.cityId),
    address: route.query.address,
    estimatedGrade: aqiId.value,
    information: information.value.trim(),
    afDate: getCurDate(),
    afTime: getCurTime(),
    state: 1
  };

  console.log('提交反馈数据:', feedbackData);

  try {
    const response = await axios.post('aqiFeedback/saveAqiFeedback', feedbackData);
    console.log('反馈响应:', response.data);

    // 兼容不同的返回格式
    let isSuccess = false;
    if (typeof response.data === 'number') {
      isSuccess = response.data > 0;
    } else if (typeof response.data === 'boolean') {
      isSuccess = response.data === true;
    } else if (response.data && typeof response.data === 'object') {
      isSuccess = response.data.code === 200 || response.data.code === 1;
    }

    if (isSuccess) {
      ElMessage.success('提交成功');
      router.push('/feedbackList');
    } else {
      const message = typeof response.data === 'object'
        ? (response.data.message || '提交失败')
        : '提交失败';
      ElMessage.error(message);
    }
  } catch (error) {
    console.error('提交错误详情:', error);

    let errorMsg = '网络错误，请稍后重试';

    if (error.response) {
      if (error.response.status === 500) {
        errorMsg = '服务器内部错误，请联系管理员或稍后重试';
      } else if (error.response.status === 400) {
        errorMsg = error.response.data?.message || '请求参数错误';
      } else {
        errorMsg = `请求失败 (${error.response.status})`;
      }
    } else if (error.request) {
      errorMsg = '无法连接到服务器，请检查后端服务是否启动';
    }

    ElMessage.error(errorMsg);
  }
};

onMounted(() => {
  loadAqiList();
});

const toSelectGrid = () => {
  router.push('/selectGrid');
};
</script>

<style scoped>
.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #cfe8e9;
  padding-bottom: 40px;
}

header {
  width: 100%;
  max-width: 750px;
  margin: 0 auto;
  text-align: center;
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
}

.subtitle {
  text-align: center;
  color: #d9534f;
  font-size: 20px;
  font-weight: bold;
  margin: 0 0 30px;
}

section {
  width: 86%;
  max-width: 500px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 12px;
  box-sizing: border-box;
  padding: 30px 30px 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* 信息卡片样式 */
.info-card {
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 25px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e4e7ed;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .label {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.info-item .value {
  color: #666;
  font-size: 15px;
  text-align: right;
  flex: 1;
  margin-left: 20px;
}

/* 表单项样式 */
.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

/* AQI选项样式 */
.aqi-text {
  font-weight: 700;
  font-size: 15px;
}
.aqi-range {
  color: #666;
  font-size: 14px;
  margin-left: 4px;
  font-weight: 600;
}

/* 按钮样式 */
.button-box {
  width: 100%;
  height: 50px;
  border-radius: 8px;
  background-color: #70b0bc;
  color: #fff;
  text-align: center;
  line-height: 50px;
  font-size: 18px;
  letter-spacing: 2px;
  user-select: none;
  margin-top: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.button-box:hover {
  background-color: #5a9aa6;
}

/* Element Plus 组件样式覆盖 */
:deep(.el-select) {
  width: 100% !important;
}

:deep(.el-textarea__inner) {
  font-family: inherit;
  resize: vertical;
}

:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  text-align: left !important;
}

:deep(.el-input__placeholder),
:deep(.el-textarea__placeholder) {
  text-align: left !important;
}
</style>
