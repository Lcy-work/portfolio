<template>
  <el-breadcrumb>
    <el-breadcrumb-item>统计数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>省分组分项检查统计</el-breadcrumb-item>
  </el-breadcrumb>
  <el-alert title="省分组分项检查统计" type="success" effect="dark" :closable="false" 
            style="width: 80%; margin: 20px 0; background: linear-gradient(90deg, #3949AB, #5C6BC0);" />
  <div class="table-box">
    <el-table :data="provinceItemTotalArr" style="width: 100%" :cell-style="{ padding: '4px' }" empty-text="无数据">
      <el-table-column prop="provinceName" label="省份" />
      <el-table-column prop="so2Total" label="SO₂总量" />
      <el-table-column prop="coTotal" label="CO总量" />
      <el-table-column prop="spmTotal" label="PM2.5总量" />
      <el-table-column prop="aqiTotal" label="AQI检测次数" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, inject } from "vue";
const axios = inject("axios");
const provinceItemTotalArr = ref([]);
const init = () => {
  axios.post("statistics/listProvinceItemTotalStatis")
    .then(res => { provinceItemTotalArr.value = res.data; })
    .catch(err => { console.log(err); });
};
init();
</script>

<style scoped>
.table-box { width: 80%; height: 450px; overflow: auto; }
</style>
