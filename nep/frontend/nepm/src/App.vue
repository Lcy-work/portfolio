<template>
  <!-- router-view 是路由出口，当前路由匹配的组件会在这里渲染 -->
  <router-view />
</template>

<script setup>
import axios from "axios";
import { provide } from "vue";
import { ElMessage } from "element-plus";

// 设置全局 axios 基础 URL，后端接口统一前缀
axios.defaults.baseURL = "http://localhost:8080/nepm";

// 封装统一消息提示框（显示在页面右上角）
const myElMessage = {
  success(msg) { ElMessage.success(msg); },
  error(msg) { ElMessage.error(msg); },
  warning(msg) { ElMessage.warning(msg); },
  info(msg) { ElMessage.info(msg); }
};

// provide 将数据注入到所有后代组件，子组件通过 inject 获取
provide("axios", axios);
provide("myElMessage", myElMessage);
</script>

<style>
/* CSS 重置，消除浏览器默认边距，让布局更可控 */
html, body, div, span, h1, h2, h3, h4, h5, h6, ul, ol, li, p {
  margin: 0;
  padding: 0;
}
html, body, #app {
  width: 100%;
  height: 100%;
  font-family: "微软雅黑";
}
ul, ol {
  list-style: none;
}
a {
  text-decoration: none;
}
</style>