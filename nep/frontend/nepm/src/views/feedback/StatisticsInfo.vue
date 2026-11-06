<template>
  <el-breadcrumb>
    <el-breadcrumb-item>确认AQI数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>确认AQI数据详情</el-breadcrumb-item>
  </el-breadcrumb>
  <el-descriptions title="确认AQI数据详情" :column="1" border style="margin-top: 20px;">
    <template #extra>
      <el-button type="primary" @click="back">返回</el-button>
    </template>
    <el-descriptions-item><template #label>确认AQI数据编号</template>{{ statistics.id }}</el-descriptions-item>
    <el-descriptions-item>
      <template #label>确认信息所在地址</template>
      <el-tag size="small">{{ statistics.gridProvince?.provinceName }}</el-tag>&nbsp;
      <el-tag size="small">{{ statistics.gridCity?.cityName }}</el-tag>&nbsp;
      <el-tag size="small">{{ statistics.address }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>确认AQI等级</template>
      <span :style="{ color: statistics.aqi?.color, fontSize: '16px', fontWeight: '700' }">
        {{ aqiLevelFullMap[statistics.aqi?.aqiId] }}
      </span>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>SO₂浓度 / 等级</template>
      {{ statistics.so2Value }} μg/m³ / {{ statistics.so2Level }} 级
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>CO浓度 / 等级</template>
      {{ statistics.coValue }} mg/m³ / {{ statistics.coLevel }} 级
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>PM2.5浓度 / 等级</template>
      {{ statistics.spmValue }} μg/m³ / {{ statistics.spmLevel }} 级
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>确认日期时间</template>
      <el-tag size="small">{{ statistics.confirmDate }}</el-tag>&nbsp;
      <el-tag size="small">{{ statistics.confirmTime }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>网格员信息</template>
      <el-tag size="small">{{ statistics.gridMember?.gmName }}</el-tag>&nbsp;
      <el-tag size="small">{{ statistics.gridMember?.tel }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>信息描述</template>
      {{ statistics.information }}
    </el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
import { ref, inject } from "vue";
import { useRouter, useRoute } from "vue-router";

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
const route = useRoute();
const router = useRouter();
const axios = inject('axios');

const statistics = ref({});

const init = () => {
  axios.post("statistics/getStatisticsById", { id: Number(route.params.id) })
    .then(res => { statistics.value = res.data; })
    .catch(err => { console.log(err); });
};
init();

const back = () => { router.go(-1); };
</script>
