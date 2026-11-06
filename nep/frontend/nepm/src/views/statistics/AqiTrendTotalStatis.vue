<template>
  <el-breadcrumb>
    <el-breadcrumb-item>统计数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>AQI空气质量指数超标趋势统计</el-breadcrumb-item>
  </el-breadcrumb>
  <el-alert title="AQI空气质量指数超标趋势统计" type="success" effect="dark" :closable="false" 
            style="width: 80%; margin: 20px 0; background: linear-gradient(90deg, #3949AB, #5C6BC0);" />
  <div class="table-box">
    <el-table :data="aqiTrendTotalArr" style="width: 100%" :cell-style="{ padding: '4px' }" empty-text="无数据">
      <el-table-column prop="month" label="月份" />
      <el-table-column prop="total" label="检测总数" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, inject } from "vue";
const axios = inject("axios");
const aqiTrendTotalArr = ref([]);
const init = () => {
  axios.post("statistics/listAqiTrendTotalStatis")
    .then(res => { aqiTrendTotalArr.value = res.data; })
    .catch(err => { console.log(err); });
};
init();
</script>

<style scoped>
.table-box { width: 80%; height: 450px; overflow: auto; }
</style>
