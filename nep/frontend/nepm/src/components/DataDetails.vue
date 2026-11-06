<template>
  <el-descriptions title="公众监督数据详情" :column="1" border style="margin-top: 20px;">
    <template #extra>
      <el-button type="primary" @click="back">返回</el-button>
    </template>
    <el-descriptions-item>
      <template #label><div>公众监督反馈信息编号</div></template>
      {{ aqiFeedback.afId }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label><div>反馈者信息</div></template>
      <el-tag size="small">{{ aqiFeedback.supervisor?.realName }}</el-tag>&nbsp;
      <el-tag size="small">{{ aqiFeedback.supervisor?.sex == 1 ? '男' : '女' }}</el-tag>&nbsp;
      <el-tag size="small">{{ aqiFeedback.supervisor?.birthday }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label><div>反馈者联系电话</div></template>
      {{ aqiFeedback.telId }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label><div>反馈信息所在地址</div></template>
      <el-tag size="small">{{ aqiFeedback.gridProvince?.provinceName }}</el-tag>&nbsp;
      <el-tag size="small">{{ aqiFeedback.gridCity?.cityName }}</el-tag>&nbsp;
      <el-tag size="small">{{ aqiFeedback.address }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label><div>反馈者信息描述</div></template>
      {{ aqiFeedback.information }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label><div>预估等级</div></template>
      <span :style="{ color: aqiFeedback.aqi?.color, fontSize: '16px', fontWeight: '700' }">
        {{ aqiLevelFullMap[aqiFeedback.aqi?.aqiId] }}
      </span>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label><div>反馈日期时间</div></template>
      <el-tag size="small">{{ aqiFeedback.afDate }}</el-tag>&nbsp;
      <el-tag size="small">{{ aqiFeedback.afTime }}</el-tag>
    </el-descriptions-item>
  </el-descriptions>

  <!-- 已指派时显示指派网格员信息提示 -->
  <el-alert
    :title="'已指派网格员：' + aqiFeedback.gridMember?.gmName + '（电话号码：' + aqiFeedback.gridMember?.tel + '）'"
    type="success"
    center
    show-icon
    style="margin-top: 20px;"
    v-if="aqiFeedback.state == 1"
  />
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

// 用嵌套对象初始化，避免模板渲染时报 "Cannot read property of undefined" 错误
const aqiFeedback = ref({
  supervisor: {},
  gridProvince: {},
  gridCity: {},
  aqi: {},
  gridMember: {}
});

const init = () => {
  axios.get("aqiFeedback/getAqiFeedbackById", { params: { afId: route.query.afId || route.params.id } })
    .then((response) => { aqiFeedback.value = response.data; })
    .catch((error) => { console.log(error); });
};
init();

const back = () => { router.go(-1); };    // 返回上一页
</script>
