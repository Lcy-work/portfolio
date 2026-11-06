<template>
  <el-breadcrumb>
    <el-breadcrumb-item>统计数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>其他数据统计</el-breadcrumb-item>
  </el-breadcrumb>
  <el-descriptions title="其他数据统计" :column="1" border style="margin-top: 20px; width: 50%;">
    <el-descriptions-item>
      <template #label>空气质量检测总数量</template>
      {{ aqiCount }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>空气质量检测良好数量</template>
      {{ aqiGoodCount }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>省网格覆盖率</template>
      {{ provinceCoverage }}%
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>大城市网格覆盖率</template>
      {{ cityCoverage }}%
    </el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
import { ref, inject } from "vue";
const axios = inject("axios");

const aqiCount = ref(0);
const aqiGoodCount = ref(0);
const provinceCoverage = ref(0);
const cityCoverage = ref(0);

// 4个独立接口并发请求，互不依赖
const init = () => {
  // 空气质量检测总数量
  axios.post("statistics/getAqiCount").then(res => {
    aqiCount.value = res.data;
  });
  
  // 空气质量检测良好数量
  axios.post("statistics/getAqiGoodCount").then(res => {
    aqiGoodCount.value = res.data;
  });
  
  // 省网格覆盖率
  axios.post("statistics/getProvinceCoverage").then(res => {
    provinceCoverage.value = res.data;
  });
  
  // 大城市网格覆盖率
  axios.post("statistics/getCityCoverage").then(res => {
    cityCoverage.value = res.data;
  });
};
init();
</script>