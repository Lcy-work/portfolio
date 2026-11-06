<template>
  <el-breadcrumb>
    <el-breadcrumb-item>确认AQI数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>确认AQI数据列表</el-breadcrumb-item>
  </el-breadcrumb>

  <!-- 搜索表单 -->
  <el-form :inline="true" :model="queryForm" size="small" style="margin-top: 20px">
    <el-form-item label="省区域">
      <el-select v-model="queryForm.provinceId" @change="changeProvince" style="width: 100px" placeholder="请选择省份" clearable>
        <el-option label="全部" :value="null" />
        <el-option
          :label="province.provinceName"
          :value="province.provinceId"
          v-for="province in provinceArr"
          :key="province.provinceId"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="市区域">
      <el-select 
        v-model="queryForm.cityId" 
        style="width: 100px" 
        :placeholder="queryForm.provinceId ? '请选择城市' : '请先选择省份'" 
        clearable
        :disabled="!queryForm.provinceId"
      >
        <el-option label="全部" :value="null" />
        <el-option
          :label="city.cityName"
          :value="city.cityId"
          v-for="city in cityArr"
          :key="city.cityId"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="确认日期">
      <el-date-picker
        v-model="queryForm.confirmDate"
        type="date"
        style="width: 100px"
        placeholder="确认日期"
        format="YYYY/MM/DD"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="danger" @click="clear">清空</el-button>
      <el-button type="primary" @click="query">查询</el-button>
    </el-form-item>
  </el-form>

  <el-table :data="pageResponseDto.list" style="width: 100%" :cell-style="{ padding: '4px' }" empty-text="无数据">
    <el-table-column prop="id" label="编号" width="80" />
    <el-table-column prop="gridProvince.provinceName" label="省份" />
    <el-table-column prop="gridCity.cityName" label="城市" />
    <el-table-column prop="confirmDate" label="确认日期" />
    <el-table-column label="AQI等级">
      <template #default="{ row }">
        <span :style="{ color: row.aqi.color, fontSize: '14px', fontWeight: '700' }">
          {{ aqiLevelFullMap[row.aqi.aqiId] }}
        </span>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="120">
      <template #default="{ row }">
        <el-button type="primary" size="small" @click="detailsEvent(row.id)">详情</el-button>
      </template>
    </el-table-column>
  </el-table>

  <div class="pagination">
    <el-pagination background small layout="prev, pager, next, total"
      :total="pageResponseDto.totalRow" :page-size="pageResponseDto.maxPageNum"
      style="margin-top: 20px" v-model:current-page="pageNum" />
  </div>
</template>

<script setup>
import { reactive, ref, inject, watch, defineOptions } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

// 设置组件名称，用于 keep-alive 缓存
defineOptions({
  name: 'StatisticsList'
});

const store = useStore();
const router = useRouter();
const axios = inject("axios");

const queryForm = reactive({ provinceId: null, cityId: null, confirmDate: '' });
const pageNum = ref(1);
watch(pageNum, (newValue) => { listStatisticsPage(newValue); });

const aqiLevelMap = { 1: '一', 2: '二', 3: '三', 4: '四', 5: '五', 6: '六' };
// AQI等级完整映射：优（一）、良（二）、轻度污染（三）、中度污染（四）、重度污染（五）、严重污染（六）
const aqiLevelFullMap = { 
  1: '优（一）', 
  2: '良（二）', 
  3: '轻度污染（三）', 
  4: '中度污染（四）', 
  5: '重度污染（五）', 
  6: '严重污染（六）' 
};
const provinceArr = ref([]);
const cityArr = ref([]);
const pageResponseDto = ref({});

const listStatisticsPage = (pageNum) => {
  axios.post("statistics/listStatisticsPage", {
    provinceId: queryForm.provinceId,
    cityId: queryForm.cityId,
    confirmDate: queryForm.confirmDate,
    pageNum: pageNum,
    maxPageNum: store.state.maxPageNum,
  }).then(res => {
    pageResponseDto.value = res.data;
  });
};

const listGridProvinceAll = () => {
  axios.post("gridProvince/listGridProvinceAll").then(res => {
    provinceArr.value = res.data;
  });
};

const listGridCityByProvinceId = (provinceId) => {
  axios.post("gridCity/listGridCityByProvinceId", { provinceId: provinceId }).then(res => {
    cityArr.value = res.data;
  });
};

const init = () => {
  listGridProvinceAll();
  listStatisticsPage(1);
};
init();

const clear = () => {
  queryForm.provinceId = null;
  queryForm.cityId = null;
  queryForm.confirmDate = '';
};

const query = () => {
  pageNum.value = 1;
  listStatisticsPage(1);
};

const changeProvince = () => {
  if (queryForm.provinceId) {
    listGridCityByProvinceId(queryForm.provinceId);
  } else {
    cityArr.value = [];
  }
};

const detailsEvent = (id) => {
  router.push(`/index/statistics/info/${id}`);
};
</script>

<style scoped>
.pagination { display: flex; justify-content: center; }
</style>
