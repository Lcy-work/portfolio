<template>
  <el-breadcrumb>
    <el-breadcrumb-item>公众监督数据管理</el-breadcrumb-item>
    <el-breadcrumb-item>公众监督数据详情</el-breadcrumb-item>
  </el-breadcrumb>
  <!-- 直接使用公共组件，DataDetails 内部自己从路由参数取 afId -->
  <DataDetails></DataDetails>
</template>

<script setup>
import DataDetails from '../../components/DataDetails.vue';
</script>