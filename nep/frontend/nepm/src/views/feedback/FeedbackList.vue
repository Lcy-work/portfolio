<template>
  <!-- 面包屑导航 -->
  <el-breadcrumb>
    <el-breadcrumb-item>公众监督数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>公众监督数据列表</el-breadcrumb-item>
  </el-breadcrumb>

  <!-- 搜索表单（inline 行内布局） -->
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
    <el-form-item label="预估等级">
      <el-select v-model="queryForm.estimatedGrade" style="width: 80px" placeholder="请选择等级" clearable>
        <el-option label="全部" :value="null" />
        <el-option
          :label="aqiLevelFullMap[aqi.aqiId]"
          :value="aqi.aqiId"
          v-for="aqi in aqiArr"
          :key="aqi.aqiId"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="反馈日期">
      <el-date-picker
        v-model="queryForm.afDate"
        type="date"
        style="width: 100px"
        placeholder="反馈日期"
        format="YYYY/MM/DD"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="danger" @click="clear">清空</el-button>
      <el-button type="primary" @click="query">查询</el-button>
    </el-form-item>
    <el-form-item>
      <!-- 单选按钮组：切换未指派/已指派 -->
      <el-radio-group v-model="queryForm.state">
        <el-radio label="0">未指派</el-radio>
        <el-radio label="1">已指派</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>

  <!-- 数据表格 -->
  <el-table :data="pageResponseDto.list" style="width: 100%" :cell-style="{ padding: '4px' }" empty-text="无数据">
    <el-table-column prop="afId" label="编号" width="90" />
    <el-table-column prop="supervisor.realName" label="反馈者姓名" />
    <el-table-column prop="gridProvince.provinceName" label="所在省" />
    <el-table-column prop="gridCity.cityName" label="所在市" />
    <!-- 自定义列：根据 aqi.color 动态设置文字颜色 -->
    <el-table-column prop="estimatedGrade" label="预估污染等级">
      <template #default="scope">
        <p :style="{
          color: scope.row.aqi.color,
          fontSize: '14px',
          fontWeight: '700',
          textAlign: 'center'
        }">
          {{ aqiLevelFullMap[scope.row.aqi.aqiId] }}
        </p>
      </template>
    </el-table-column>
    <el-table-column prop="afDate" label="反馈日期" />
    <el-table-column prop="afTime" label="反馈时间" />
    <!-- 操作列：查看详情 + 指派/重新指派 -->
    <el-table-column label="操作" width="120">
      <template #default="scope">
        <el-button link type="primary" size="small" @click="detailsEvent(scope.row.afId)">
          <el-icon title="查看详情" style="font-size: 18px"><Tickets /></el-icon>
        </el-button>
        <!-- 未指派时显示"指派"图标 -->
        <el-button link type="danger" size="small" @click="assignEvent(scope.row)" v-if="queryForm.state == 0">
          <el-icon title="指派网格员" style="font-size: 18px"><Pointer /></el-icon>
        </el-button>
        <!-- 已指派时显示"重新指派"图标 -->
        <el-button link type="danger" size="small" @click="assignEvent(scope.row)" v-if="queryForm.state == 1">
          <el-icon title="重新指派" style="font-size: 18px"><Edit /></el-icon>
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分页组件 -->
  <div class="pagination">
    <el-pagination
      background
      small
      layout="prev, pager, next, total"
      :total="pageResponseDto.totalRow"
      :page-size="pageResponseDto.maxPageNum"
      style="margin-top: 20px"
      v-model:current-page="pageNum"
    />
  </div>
</template>
<script setup>
import { Tickets, Pointer, Edit } from "@element-plus/icons-vue";
import { reactive, ref, inject, watch, defineOptions } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

// 设置组件名称，用于 keep-alive 缓存
defineOptions({
  name: 'FeedbackList'
});

const store = useStore();
const router = useRouter();
const axios = inject("axios");

// 搜索条件表单
const queryForm = reactive({
  provinceId: null,
  cityId: null,
  estimatedGrade: null,
  afDate: "",
  state: "0",       // 默认查询"未指派"数据
});

// 当前页码，配合 el-pagination 的 v-model:current-page 双向绑定
const pageNum = ref(1);
// 监听 pageNum 变化，切换页码时自动重新查询
watch(pageNum, (newValue) => {
  listAqiFeedbackPage(newValue);
});

const provinceArr = ref([]);
const cityArr = ref([]);
const aqiArr = ref([]);
const pageResponseDto = ref({});      // 分页响应数据（含 list、totalRow、maxPageNum）
// AQI等级完整映射：优（一）、良（二）、轻度污染（三）、中度污染（四）、重度污染（五）、严重污染（六）
const aqiLevelFullMap = { 
  1: '优（一）', 
  2: '良（二）', 
  3: '轻度污染（三）', 
  4: '中度污染（四）', 
  5: '重度污染（五）', 
  6: '严重污染（六）' 
};
const aqiLevelMap = { 1: '一', 2: '二', 3: '三', 4: '四', 5: '五', 6: '六' };

// 查询分页列表
const listAqiFeedbackPage = (pageNum) => {
  let param = {
    provinceId: queryForm.provinceId,
    cityId: queryForm.cityId,
    estimatedGrade: queryForm.estimatedGrade,
    afDate: queryForm.afDate,
    state: queryForm.state,
    pageNum: pageNum,
    maxPageNum: store.state.maxPageNum,    // 从 Vuex 读取每页条数
  };
  axios.post("aqiFeedback/listAqiFeedbackPage", param)
    .then((response) => {
      pageResponseDto.value = response.data;
    })
    .catch((error) => { 
      console.log(error); 
    });
};

// 查询所有省份
const listGridProvinceAll = () => {
  axios.post("gridProvince/listGridProvinceAll")
    .then((response) => {
      provinceArr.value = response.data;
    })
    .catch((error) => { 
      console.log(error); 
    });
};

// 根据省 ID 查询市列表
const listGridCityByProvinceId = (provinceId) => {
  axios.post("gridCity/listGridCityByProvinceId", { provinceId: provinceId })
    .then((response) => {
      cityArr.value = response.data;
    })
    .catch((error) => { 
      console.log(error); 
    });
};

// 查询所有 AQI 等级
const listAqiAll = () => {
  axios.post("aqi/listAqiAll")
    .then((response) => {
      aqiArr.value = response.data;
    })
    .catch((error) => { 
      console.log(error); 
    });
};

// 页面初始化：同时发起三个请求
const init = () => {
  listAqiFeedbackPage(1);      // 查询第一页数据
  listGridProvinceAll();       // 查询所有省份
  listAqiAll();                // 查询所有AQI等级
};
init();

// 清空搜索条件
const clear = () => {
  queryForm.provinceId = null;
  queryForm.cityId = null;
  queryForm.estimatedGrade = null;
  queryForm.afDate = '';
  queryForm.state = '0';
};

// 点击查询按钮
const query = () => { 
  pageNum.value = 1;           // 重置到第一页
  listAqiFeedbackPage(1);      // 重新查询数据
};

// 省份切换时，联动更新市列表
const changeProvince = () => { 
  queryForm.cityId = null;        // 清空城市选择
  if (queryForm.provinceId) {
    listGridCityByProvinceId(queryForm.provinceId);  // 根据选中的省份查询城市列表
  } else {
    cityArr.value = [];        // 如果没有选择省份，清空城市列表
  }
};

// 跳转详情页，通过路由参数传 afId
const detailsEvent = (afId) => {
  router.push({ path: `/index/feedback/info/${afId}`, query: { afId: afId } });
};

// 跳转指派页，传递 afId、provinceId、cityId
const assignEvent = (af) => {
  router.push({
    path: `/index/feedback/assign/${af.afId}`,
    query: {
      afId: af.afId,
      provinceId: af.gridProvince?.provinceId,
      cityId: af.gridCity?.cityId
    }
  });
};

</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
}
</style>