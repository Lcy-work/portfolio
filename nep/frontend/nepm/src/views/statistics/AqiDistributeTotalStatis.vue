<template>
  <el-breadcrumb>
    <el-breadcrumb-item>统计数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>AQI空气质量指数级别分布统计</el-breadcrumb-item>
  </el-breadcrumb>
  <el-alert title="AQI空气质量指数级别分布统计" type="success" effect="dark" :closable="false" 
            style="width: 80%; margin: 20px 0; background: linear-gradient(90deg, #3949AB, #5C6BC0);" />
  <div class="table-box">
    <el-table :data="aqiDistributeTotalArr" style="width: 100%" :cell-style="{ padding: '4px' }" empty-text="无数据">
      <el-table-column prop="aqiId" label="等级编号" width="100" />
      <el-table-column label="AQI说明">
        <template #default="{ row }">
          <span :style="{ color: aqiColorMap[row.aqiId], fontSize: '16px', fontWeight: '700' }">
            {{ aqiNameMap[row.aqiId] }}（{{ aqiLevelMap[row.aqiId] }}）
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="total" label="数量" width="100" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, inject } from "vue";
const axios = inject("axios");
const aqiLevelMap = { 1: '一', 2: '二', 3: '三', 4: '四', 5: '五', 6: '六' };
const aqiNameMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' };
const aqiColorMap = { 1: '#00C800', 2: '#D4A800', 3: '#E07000', 4: '#D00000', 5: '#DC143C', 6: '#8B0000' };
const aqiDistributeTotalArr = ref([]);
const init = () => {
  axios.post("statistics/listAqiDistributeTotalStatis")
    .then(res => { aqiDistributeTotalArr.value = res.data; })
    .catch(err => { console.log(err); });
};
init();
</script>

<style scoped>
.table-box { width: 80%; height: 450px; overflow: auto; }
</style>
