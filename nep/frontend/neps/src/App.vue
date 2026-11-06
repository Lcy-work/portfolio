<!-- src/App.vue -->
<template>
  <router-view/>
</template>

<script setup>
// axios 已在 main.js 中通过 app.provide 全局注入
// 子组件可直接使用 inject('axios') 获取，无需在此重复提供
</script>

<style>
/* ========== CSS 全局重置 ========== */
html, body, div, span, h1, h2, h3, h4, h5, h6, ul, ol, li, p {
    margin: 0;
    padding: 0;
}
html, body, #app {
    width: 100%;
    height: 100%;
    font-family: "微软雅黑";
}
ul, ol { list-style: none; }
a { text-decoration: none; }

/* ========== 共通：总容器 ========== */
.wrapper {
    width: 100%;
    height: 100%;
    background-color: #CFE8E9;
}

/* ========== 共通：头部区域 ========== */
.header-box { width: 100%; }
.header-box header {
    width: 100%;
    height: 15.7vw;
    background-color: #FFF;
    color: #555;
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0 3.6vw;
}
.header-box .top-ban {
    width: 100%;
    height: 15.7vw;    /* 占位，防止内容被 fixed header 遮挡 */
}
.header-box img { display: block; width: 100%; }
.header-box i { font-size: 8vw; }

/* ========== 共通：表格表单 ========== */
.table-form {
    width: 86vw;
    margin: 0 auto;
    margin-top: 5vw;
    border-collapse: collapse;
    font-size: 4.2vw;
}
.table-form tr td { height: 12vw; }
.table-form input,
.table-form select,
.table-form textarea {
    background-color: #CFE8E9;
    border: none;
    outline: none;
}
.table-form tr td:first-child {
    color: #777;
    font-weight: 700;
}

/* ========== 共通：按钮 ========== */
.btn {
    width: 100%;
    height: 12vw;
    background-color: #70B0BC;
    margin-top: 5vw;
    border-radius: 2vw;
    color: #FFF;
    font-size: 5vw;
    text-align: center;
    line-height: 12vw;
    letter-spacing: 1vw;
    font-weight: 500;
    user-select: none;
}
</style>