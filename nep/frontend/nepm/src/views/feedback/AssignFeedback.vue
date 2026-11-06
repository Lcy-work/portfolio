<template>
  <el-breadcrumb>
    <el-breadcrumb-item>公众监督数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>指派网格员</el-breadcrumb-item>
  </el-breadcrumb>
  
  <!-- 复用公共详情组件 -->
  <DataDetails></DataDetails>

  <div class="assign">
    <!-- 是否异地指派的开关 -->
    <el-form :inline="true">
      <el-form-item label="是否异地指派">
        <el-switch v-model="isRemote" size="small" />
      </el-form-item>
    </el-form>

    <!-- 本地指派表单（isRemote 为 false 时显示） -->
    <el-form :inline="true" v-if="!isRemote" size="small">
      <el-form-item label="指派给">
        <el-select v-model="gmIdLocal" placeholder="请选择网格员">
          <el-option
            :label="gridMember.gmName"
            :value="gridMember.gmId"
            v-for="gridMember in gridMemberLocalArr"
            :key="gridMember.gmId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <!-- el-popconfirm：点击后弹出确认框，确认后才执行 assignEvent -->
        <el-popconfirm title="确定要指派吗?" confirm-button-text="确定" cancel-button-text="取消" @confirm="assignEvent">
          <template #reference>
            <el-button>本地指派</el-button>
          </template>
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <!-- 异地指派表单（isRemote 为 true 时显示） -->
    <el-form :inline="true" v-if="isRemote" size="small">
      <el-form-item label="省区域">
        <el-select v-model="provinceIdNoLocal" @change="changeProvince" placeholder="请选择省份">
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
          v-model="cityIdNoLocal" 
          @change="changeCity" 
          :placeholder="provinceIdNoLocal ? '请选择城市' : '请先选择省份'"
          :disabled="!provinceIdNoLocal"
        >
          <el-option
            :label="city.cityName"
            :value="city.cityId"
            v-for="city in cityArr"
            :key="city.cityId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="异地指派给">
        <el-select v-model="gmIdNoLocal" placeholder="请选择网格员">
          <el-option
            :label="gridMember.gmName"
            :value="gridMember.gmId"
            v-for="gridMember in gridMemberNoLocalArr"
            :key="gridMember.gmId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定要指派吗?" confirm-button-text="确定" cancel-button-text="取消" @confirm="assignEvent">
          <template #reference>
            <el-button>异地指派</el-button>
          </template>
        </el-popconfirm>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import DataDetails from "../../components/DataDetails.vue";
import { ref, inject } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getCurDate, getCurTime } from "../../common.js";

const myElMessage = inject('myElMessage');
const router = useRouter();
const route = useRoute();
const axios = inject("axios");

const isRemote = ref(false);             // 是否异地指派

const gmIdLocal = ref(0);                // 本地选中的网格员 ID
const gridMemberLocalArr = ref([]);      // 本地网格员列表

const gmIdNoLocal = ref(0);             // 异地选中的网格员 ID
const gridMemberNoLocalArr = ref([]);   // 异地网格员列表

const provinceArr = ref([]);
const cityArr = ref([]);
const provinceIdNoLocal = ref(0);
const cityIdNoLocal = ref(0);

const init = () => {
  // 初始化1：查询本地网格员（根据反馈数据的省市）
  axios.post("gridMember/listGridMemberByProvinceId", {
    provinceId: route.query.provinceId,
    cityId: route.query.cityId
  }).then(res => {
    gridMemberLocalArr.value = res.data;
  });
  
  // 初始化2：查询所有省（供异地指派用）
  axios.post("gridProvince/listGridProvinceAll").then(res => {
    provinceArr.value = res.data;
  });
};
init();

// 根据省查市，并联动查异地网格员
const listGridCityByProvinceId = () => {
  axios.post("gridCity/listGridCityByProvinceId", { provinceId: provinceIdNoLocal.value }).then(res => {
    cityArr.value = res.data;
  });
};

// 查询异地网格员
const listGridMemberNoLocal = () => {
  axios.post("gridMember/listGridMemberByProvinceId", {
    provinceId: provinceIdNoLocal.value,
    cityId: cityIdNoLocal.value
  }).then(res => {
    gridMemberNoLocalArr.value = res.data;
  });
};

const changeProvince = () => { 
  listGridCityByProvinceId(); 
};

const changeCity = () => { 
  listGridMemberNoLocal(); 
};

// 执行指派
const assignEvent = () => {
  const gmId = isRemote.value ? gmIdNoLocal.value : gmIdLocal.value;
  
  if (!gmId) {
    myElMessage.error('请选择要指派的网格员');
    return;
  }
  
  axios.post("aqiFeedback/updateAqiFeedbackAssign", {
    afId: route.query.afId || route.params.id,
    gmId: gmId,
    state: 1,
    updateDate: getCurDate(),
    updateTime: getCurTime()
  }).then(() => {
    myElMessage.success('指派成功');
    router.push('/index/feedback/list');
  });
};
</script>

<style scoped>
.assign {
  margin-top: 20px;
  display: flex;
  align-items: center;
}
</style>