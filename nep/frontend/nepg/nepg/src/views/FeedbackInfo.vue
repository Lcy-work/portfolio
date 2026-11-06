<!-- src/views/FeedbackInfo.vue — NEPG 网格员反馈任务详情 + 实测录入 -->
<template>
  <div class="wrapper">
    <!-- 顶部导航栏 -->
    <div class="fi-header-box">
      <header>
        <i class="fa fa-angle-left" @click="goBack"></i>
        <span>反馈任务详细信息</span>
        <i class="fa fa-bars" @click="toFeedbackList"></i>
      </header>
      <div class="fi-top-ban"></div>
    </div>

    <div class="fi-content">
      <!-- 反馈用户信息 -->
      <div class="section-title">反馈用户</div>
      <div class="info-card">
        <table class="info-table">
          <tr>
            <td class="label">反馈用户</td>
            <td class="value">{{ aqiFeedback.supervisor?.realName || '—' }} &nbsp; {{ formatPhone(aqiFeedback.supervisor?.telId) }}</td>
          </tr>
          <tr>
            <td class="label">网格</td>
            <td class="value">{{ aqiFeedback.gridProvince?.provinceName || '' }} {{ aqiFeedback.gridCity?.cityName || '' }}</td>
          </tr>
          <tr>
            <td class="label">详细地址</td>
            <td class="value">{{ aqiFeedback.address || '—' }}</td>
          </tr>
          <tr>
            <td class="label">投诉等级</td>
            <td class="value">
              <span
                class="level-badge-inline"
                :style="{ backgroundColor: getLevelColor(aqiFeedback.aqi?.aqiId) }"
              >
                {{ aqiFeedback.aqi?.chineseExplain || '待' }}级
              </span>
              <span class="level-desc">{{ aqiFeedback.aqi?.aqiExplain || '' }}</span>
            </td>
          </tr>
          <tr>
            <td class="label">信息描述</td>
            <td class="value">{{ aqiFeedback.information || '—' }}</td>
          </tr>
        </table>
      </div>

      <!-- 实测数据录入 -->
      <div class="section-title">实测数据录入</div>
      <div class="input-card">
        <table class="aqi-table">
          <tr>
            <td class="param-label">SO&#8322;浓度</td>
            <td class="param-input">
              <input
                type="number"
                v-model="so2Value"
                placeholder="请输入SO₂浓度"
                step="0.1"
                @input="validateInput('so2')"
              />
              <span class="unit">μg/m³</span>
            </td>
            <td class="param-level">
              <span
                class="level-badge"
                :style="{ backgroundColor: so2LevelObj.color || '#999' }"
              >
                {{ so2LevelObj.chineseExplain || '—' }}
              </span>
            </td>
          </tr>
          <tr>
            <td class="param-label">CO浓度</td>
            <td class="param-input">
              <input
                type="number"
                v-model="coValue"
                placeholder="请输入CO浓度"
                step="0.1"
                @input="validateInput('co')"
              />
              <span class="unit">mg/m³</span>
            </td>
            <td class="param-level">
              <span
                class="level-badge"
                :style="{ backgroundColor: coLevelObj.color || '#999' }"
              >
                {{ coLevelObj.chineseExplain || '—' }}
              </span>
            </td>
          </tr>
          <tr>
            <td class="param-label">PM2.5</td>
            <td class="param-input">
              <input
                type="number"
                v-model="spmValue"
                placeholder="请输入PM2.5浓度"
                step="0.1"
                @input="validateInput('spm')"
              />
              <span class="unit">μg/m³</span>
            </td>
            <td class="param-level">
              <span
                class="level-badge"
                :style="{ backgroundColor: spmLevelObj.color || '#999' }"
              >
                {{ spmLevelObj.chineseExplain || '—' }}
              </span>
            </td>
          </tr>
        </table>

        <!-- 综合 AQI 等级 -->
        <div class="aqi-result">
          <span class="aqi-label">实测AQI等级：</span>
          <span
            class="aqi-badge-large"
            :style="{ backgroundColor: aqiObj.color || '#999' }"
          >
            {{ aqiObj.chineseExplain || '—' }}级 {{ aqiObj.aqiExplain || '' }}
          </span>
        </div>

        <!-- 提交按钮 -->
        <div class="fi-btn" @click="submitData">提交实测数据</div>
      </div>

      <div style="height: 100px;"></div>
    </div>

    <!-- 底部 -->
    <footer>
      <img src="@/assets/bottom.png" alt="footer" />
    </footer>
  </div>
</template>

<script setup>
import { ref, inject, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getSessionStorage, getCurDate, getCurTime } from "../common.js";
import { ElMessage, ElMessageBox } from 'element-plus';

const route = useRoute();
const router = useRouter();
const axios = inject("axios");
const gridMember = getSessionStorage("gridMember");

// 任务详情
const aqiFeedback = ref({
  supervisor: {},
  aqi: {}
});

// AQI 等级标准数据
const aqiArr = ref([]);

// AQI等级颜色映射
const levelColors = {
  1: '#02E300',
  2: '#FFD700',
  3: '#FFA500',
  4: '#FF6347',
  5: '#DC143C',
  6: '#8B0000'
};

const formatPhone = (phone) => {
  if (!phone) return '';
  const digits = String(phone).replace(/\D/g, '');
  if (digits.length <= 3) return digits;
  if (digits.length <= 7) return digits.slice(0, 3) + '  ' + digits.slice(3);
  return digits.slice(0, 3) + '  ' + digits.slice(3, 7) + '  ' + digits.slice(7, 11);
};

const getLevelColor = (levelId) => {
  return levelColors[levelId] || '#999999';
};

// 三项实测浓度值
const so2Value = ref('');
const coValue = ref('');
const spmValue = ref('');

const validateInput = (type) => {
  let value;
  if (type === 'so2') value = so2Value.value;
  else if (type === 'co') value = coValue.value;
  else if (type === 'spm') value = spmValue.value;

  if (value && parseFloat(value) < 0) {
    ElMessage.warning('浓度值不能为负数');
    if (type === 'so2') so2Value.value = '';
    else if (type === 'co') coValue.value = '';
    else if (type === 'spm') spmValue.value = '';
  }
};

// SO₂ 等级计算
const so2LevelObj = computed(() => {
  const value = parseFloat(so2Value.value);
  if (isNaN(value)) return {};
  if (value <= 50) return aqiArr.value.find(item => item.aqiId === 1) || {};
  if (value <= 150) return aqiArr.value.find(item => item.aqiId === 2) || {};
  if (value <= 475) return aqiArr.value.find(item => item.aqiId === 3) || {};
  if (value <= 800) return aqiArr.value.find(item => item.aqiId === 4) || {};
  if (value <= 1600) return aqiArr.value.find(item => item.aqiId === 5) || {};
  return aqiArr.value.find(item => item.aqiId === 6) || {};
});

// CO 等级计算
const coLevelObj = computed(() => {
  const value = parseFloat(coValue.value);
  if (isNaN(value)) return {};
  if (value <= 5) return aqiArr.value.find(item => item.aqiId === 1) || {};
  if (value <= 10) return aqiArr.value.find(item => item.aqiId === 2) || {};
  if (value <= 35) return aqiArr.value.find(item => item.aqiId === 3) || {};
  if (value <= 60) return aqiArr.value.find(item => item.aqiId === 4) || {};
  if (value <= 90) return aqiArr.value.find(item => item.aqiId === 5) || {};
  return aqiArr.value.find(item => item.aqiId === 6) || {};
});

// PM2.5 等级计算
const spmLevelObj = computed(() => {
  const value = parseFloat(spmValue.value);
  if (isNaN(value)) return {};
  if (value <= 35) return aqiArr.value.find(item => item.aqiId === 1) || {};
  if (value <= 75) return aqiArr.value.find(item => item.aqiId === 2) || {};
  if (value <= 115) return aqiArr.value.find(item => item.aqiId === 3) || {};
  if (value <= 150) return aqiArr.value.find(item => item.aqiId === 4) || {};
  if (value <= 250) return aqiArr.value.find(item => item.aqiId === 5) || {};
  return aqiArr.value.find(item => item.aqiId === 6) || {};
});

// 综合 AQI：取最高等级
const aqiObj = computed(() => {
  const levels = [so2LevelObj.value, coLevelObj.value, spmLevelObj.value];
  return levels.reduce((max, current) => {
    return (current.aqiId || 0) > (max.aqiId || 0) ? current : max;
  }, levels[0] || {});
});

// 初始化
const init = async () => {
  if (!gridMember || !gridMember.gmId) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  const afId = route.query.afId;
  if (!afId) {
    ElMessage.error('缺少任务 ID 参数');
    router.push('/feedbackList');
    return;
  }

  try {
    const response = await axios.get('aqiFeedback/getAqiFeedbackById', {
      params: { afId: afId }
    });
    if (response.data) {
      aqiFeedback.value = response.data || aqiFeedback.value;
    } else {
      ElMessage.error('加载任务详情失败');
    }
  } catch (error) {
    console.error('加载任务详情错误:', error);
    ElMessage.error('网络错误，请稍后重试');
  }

  try {
    const response = await axios.post('aqi/listAqiAll');
    if (response.data) {
      aqiArr.value = response.data;
    }
  } catch (error) {
    console.error('加载 AQI 标准错误:', error);
  }
};

onMounted(() => {
  init();
});

// 提交实测数据
const submitData = async () => {
  if (!so2Value.value || !coValue.value || !spmValue.value) {
    ElMessage.warning('请填写全部三项实测浓度值');
    return;
  }

  if (isNaN(so2Value.value) || isNaN(coValue.value) || isNaN(spmValue.value)) {
    ElMessage.warning('请输入有效的数字');
    return;
  }

  if (parseFloat(so2Value.value) < 0 || parseFloat(coValue.value) < 0 || parseFloat(spmValue.value) < 0) {
    ElMessage.warning('浓度值不能为负数');
    return;
  }

  try {
    await ElMessageBox.confirm(
      '确认提交以下数据？\nSO₂: ' + so2Value.value + ' μg/m³ (' + (so2LevelObj.value.chineseExplain || '—') + '级)\nCO: ' + coValue.value + ' mg/m³ (' + (coLevelObj.value.chineseExplain || '—') + '级)\nPM₂.₅: ' + spmValue.value + ' μg/m³ (' + (spmLevelObj.value.chineseExplain || '—') + '级)\n综合: ' + (aqiObj.value.chineseExplain || '—') + '级 ' + (aqiObj.value.aqiExplain || ''),
      '确认提交',
      {
        confirmButtonText: '确认提交',
        cancelButtonText: '取消',
        type: 'info'
      }
    );
  } catch {
    return;
  }

  const param = {
    provinceId: aqiFeedback.value.provinceId,
    cityId: aqiFeedback.value.cityId,
    address: aqiFeedback.value.address,
    so2Value: parseInt(so2Value.value, 10),
    so2Level: so2LevelObj.value.aqiId,
    coValue: parseInt(coValue.value, 10),
    coLevel: coLevelObj.value.aqiId,
    spmValue: parseInt(spmValue.value, 10),
    spmLevel: spmLevelObj.value.aqiId,
    aqiId: aqiObj.value.aqiId,
    confirmDate: getCurDate(),
    confirmTime: getCurTime(),
    gmId: gridMember.gmId,
    fdId: String(aqiFeedback.value.afId),
    information: aqiFeedback.value.information
  };

  try {
    const response = await axios.post('aqiFeedback/confirm', param);
    if (response.data) {
      await updateAqiFeedbackState();
      ElMessage.success('实测数据提交成功');
      router.push('/feedbackList');
    } else {
      ElMessage.error('提交失败');
    }
  } catch (error) {
    console.error('提交错误:', error);
    ElMessage.error('提交失败，请检查网络连接');
  }
};

const updateAqiFeedbackState = async () => {
  try {
    await axios.put('aqiFeedback/updateState', {
      afId: aqiFeedback.value.afId,
      state: 2
    });
  } catch (error) {
    console.error('更新状态错误:', error);
  }
};

const goBack = () => {
  router.back();
};

const toFeedbackList = () => {
  router.push('/feedbackList');
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

.fi-header-box {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.fi-header-box header {
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

.fi-header-box header span {
  color: #FFF;
  font-size: 18px;
  font-weight: 600;
}

.fi-header-box header .fa {
  color: #FFF;
  font-size: 20px;
  cursor: pointer;
}

.fi-top-ban {
  width: 100%;
  height: 4px !important;
  max-height: 4px !important;
  background: rgba(255, 255, 255, 0.3);
}

/* ========== 主体内容 — 自动撑满剩余空间 ========== */
.fi-content {
  flex: 1;
  margin-top: 54px;
  padding-bottom: 24px;
}

/* ========== 区块标题 ========== */
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1565C0;
  padding: 20px 4vw 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* ========== 用户信息卡片 ========== */
.info-card {
  margin: 0 4vw;
  background: #FFF;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.info-table {
  width: 100%;
  border-collapse: collapse;
}

.info-table tr {
  border-bottom: 1px solid #f0f0f0;
}
.info-table tr:last-child {
  border-bottom: none;
}

.info-table td {
  padding: 14px 16px;
  font-size: 14px;
  vertical-align: middle;
}

.info-table .label {
  color: #999;
  white-space: nowrap;
  font-weight: 500;
  background-color: #fafafa;
  width: 90px;
}

.info-table .value {
  color: #333;
  word-break: break-all;
}

.level-badge-inline {
  display: inline-block;
  min-width: 32px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  color: #FFF;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 700;
  padding: 0 8px;
  margin-right: 8px;
}

.level-desc {
  font-size: 13px;
  color: #999;
}

/* ========== 实测数据录入卡片 ========== */
.input-card {
  margin: 0 4vw 20px;
  background: #FFF;
  border-radius: 12px;
  padding: 20px 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

/* ========== 参数表格 ========== */
.aqi-table {
  width: 100%;
  border-collapse: collapse;
}

.aqi-table tr {
  border-bottom: 1px solid #f5f5f5;
}
.aqi-table tr:last-child {
  border-bottom: none;
}

.aqi-table td {
  padding: 14px 6px;
  vertical-align: middle;
}

.param-label {
  font-size: 14px;
  font-weight: 600;
  color: #555;
  white-space: nowrap;
  width: 80px;
}

.param-input {
  display: flex;
  align-items: center;
  gap: 8px;
}

.param-input input {
  width: 100%;
  max-width: 160px;
  height: 40px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  padding: 0 12px;
  font-size: 15px;
  outline: none;
  box-sizing: border-box;
  transition: all 0.3s ease;
  background-color: #fafafa;
}
.param-input input:focus {
  border-color: #1565C0;
  box-shadow: 0 0 0 3px rgba(21, 101, 192, 0.1);
  background-color: #fff;
}

.unit {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

.param-level {
  text-align: center;
}

.level-badge {
  display: inline-block;
  min-width: 36px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  color: #FFF;
  border-radius: 15px;
  font-size: 14px;
  font-weight: 700;
  padding: 0 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

/* ========== 综合 AQI 结果 ========== */
.aqi-result {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f5faff 0%, #e3f2fd 100%);
  border-radius: 12px;
  border: 2px solid #1565C0;
}

.aqi-label {
  font-size: 15px;
  color: #1565C0;
  font-weight: 600;
}

.aqi-badge-large {
  display: inline-block;
  padding: 8px 24px;
  color: #FFF;
  border-radius: 20px;
  font-size: 18px;
  font-weight: 700;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

/* ========== 提交按钮 ========== */
.fi-btn {
  margin-top: 24px;
  border-radius: 10px;
  height: 50px;
  line-height: 50px;
  font-size: 17px;
  font-weight: 600;
  letter-spacing: 2px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #42A5F5 0%, #1565C0 100%) !important;
  color: white;
  box-shadow: 0 4px 15px rgba(21, 101, 192, 0.3);
  text-align: center;
}

.fi-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(21, 101, 192, 0.4);
}

.fi-btn:active {
  transform: translateY(0);
}

/* ========== 底部 — 隐藏覆盖 ========== */
footer {
  display: none !important;
}
</style>
