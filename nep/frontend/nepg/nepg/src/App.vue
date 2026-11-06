<template>
  <router-view/>
</template>

<style>
/* ========== CSS 全局重置 ========== */
html, body, div, span, h1, h2, h3, h4, h5, h6, ul, ol, li, p {
    margin: 0;
    padding: 0;
}
html, body, #app {
    width: 100%;
    height: 100%;
    font-family: "微软雅黑", -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, sans-serif;
}
ul, ol { list-style: none; }
a { text-decoration: none; }

/* ========== 共通：总容器 ========== */
.wrapper {
    width: 100%;
    min-height: 100vh;
    background-color: #E3F2FD;
    padding-bottom: 40px;
}

/* ========== 共通：头部区域 ========== */
.header-box { width: 100%; }
.header-box header {
    width: 100%;
    height: 15.7vw;
    max-height: 64px;
    background-color: #FFF;
    color: #1976D2;
    position: fixed;
    left: 0;
    top: 0;
    z-index: 100;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0 3.6vw;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.header-box .top-ban {
    width: 100%;
    height: 15.7vw;
    max-height: 64px;
}
.header-box img {
    display: block;
    width: 100%;
    height: auto;
    max-height: 160px;
    object-fit: cover;
}
.header-box .fa {
    font-size: 6vw;
    max-width: 32px;
    cursor: pointer;
    color: #1976D2;
}

/* ========== 共通：底部 ========== */
footer {
    width: 100%;
    position: fixed;
    left: 0;
    bottom: 0;
    z-index: 10;
}
footer img {
    width: 100%;
    display: block;
    height: auto;
    max-height: 80px;
    object-fit: cover;
}

/* ========== 共通：按钮 ========== */
.btn-primary {
    width: 100%;
    height: 48px;
    background-color: #2196F3;
    border-radius: 8px;
    color: #FFF;
    font-size: 17px;
    text-align: center;
    line-height: 48px;
    letter-spacing: 2px;
    font-weight: 500;
    user-select: none;
    cursor: pointer;
    transition: background-color 0.2s;
    border: none;
}
.btn-primary:hover {
    background-color: #1976D2;
}
.btn-primary:active {
    background-color: #1565C0;
}

/* ========== 共通：卡片 ========== */
.card {
    background: #FFF;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

/* ========== 无数据提示 ========== */
.no-data {
    width: 80%;
    margin: 60px auto;
    text-align: center;
    color: #999;
    font-size: 15px;
}
.no-data p {
    padding: 40px 0;
}
</style>
