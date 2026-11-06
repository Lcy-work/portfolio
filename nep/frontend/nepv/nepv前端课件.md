# 空气质量监测可视化大屏 —— 前端教学流程文档

> **项目简介**：本项目是一个基于 Vue 3 + Vite + ECharts 构建的空气质量监测数据可视化大屏。

---

## 目录

1. [技术栈总览](#一技术栈总览)
2. [阶段一：创建项目与基础配置](#二阶段一创建项目与基础配置)
3. [阶段二：安装依赖与路由配置](#三阶段二安装依赖与路由配置)
4. [阶段三：全局样式与应用入口](#四阶段三全局样式与应用入口)
5. [阶段四：大屏布局骨架](#五阶段四大屏布局骨架)
6. [阶段五：封装 ECharts 图表组件](#六阶段五封装-echarts-图表组件)
7. [阶段六：接入后端数据](#七阶段六接入后端数据)
8. [阶段七：实时刷新与时钟](#八阶段七实时刷新与时钟)
9. [阶段八：测试与调试](#九阶段八测试与调试)
10. [阶段九：构建与部署](#十阶段九构建与部署)
11. [项目文件结构速查](#十一项目文件结构速查)

---

## 一、技术栈总览

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | ^3.5 | 前端框架（Composition API） |
| Vite | ^8.0 | 构建工具 / 开发服务器 |
| Vue Router | ^5.1 | 单页路由 |
| ECharts | ^6.1 | 数据可视化图表库 |
| Axios | ^1.17 | HTTP 请求 |
| Font Awesome | ^4.7 | 图标库 |

---

## 二、阶段一：创建项目与基础配置

### 1.1 创建 Vue 3 + Vite 项目

打开终端，执行以下命令：

```bash
npm create vue@latest nepv
```

然后进入项目目录：

```bash
cd nepv
npm install
```

### 1.2 配置 Vite（`vite.config.js`）

打开 `vite.config.js`，修改为以下内容，设定开发服务器端口为 8084，并添加 `@` 路径别名：

```js
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 8084   // 前端运行在 8084，避免与后端 8080 冲突
  }
})
```

### 1.3 修改页面标题（`index.html`）

```html
<!DOCTYPE html>
<html lang="">
  <head>
    <meta charset="UTF-8">
    <link rel="icon" href="/favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>空气质量监测可视化大屏</title>
  </head>
  <body>
    <div id="app"></div>
    <script type="module" src="/src/main.js"></script>
  </body>
</html>
```

**✅ 测试：** 运行 `npm run dev`，浏览器打开 `http://localhost:8084`，能看到默认 Vue 欢迎页即表示项目创建成功。

---

## 三、阶段二：安装依赖与路由配置

### 2.1 安装项目依赖

```bash
npm install axios echarts font-awesome
```

安装完成后，`package.json` 的 `dependencies` 应包含：

```json
{
  "axios": "^1.17.0",
  "echarts": "^6.1.0",
  "font-awesome": "^4.7.0",
  "vue": "^3.5.32",
  "vue-router": "^5.1.0"
}
```

### 2.2 配置路由（`src/router/index.js`）

删除脚手架自动生成的路由文件内容，替换为以下代码。本项目只有一个页面 `Index.vue`：

```js
import { createRouter, createWebHashHistory } from 'vue-router'
import Index from '../views/Index.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Index
  },
  {
    path: '/index',
    name: 'Index',
    component: Index
  }
]

const router = createRouter({
  history: createWebHashHistory(),  // 使用 Hash 模式，避免部署时 404
  routes
})

export default router
```

> **知识点**：`createWebHashHistory()` 会在 URL 中加入 `#`，例如 `http://localhost:8084/#/`。它的优势是不需要服务器额外配置，静态部署更方便。

---

## 四、阶段三：全局样式与应用入口

### 3.1 修改入口文件（`src/main.js`）

删除默认内容，引入路由和 Font Awesome 图标样式：

```js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入 Font Awesome 图标库的 CSS
import 'font-awesome/css/font-awesome.min.css'

createApp(App).use(router).mount('#app')
```

### 3.2 修改根组件（`src/App.vue`）

清空默认内容，保留路由出口和全局 CSS 重置：

```vue
<template>
  <router-view/>
</template>

<style>
/********************** CSS 重置 *********************/
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
```

> **知识点**：CSS 重置是为了消除各浏览器的默认样式差异，保证大屏布局占满整个视口（width/height 100%）。

**✅ 测试：** 此时刷新页面应为空白（因为 `Index.vue` 还未创建），控制台无报错即正常。

---

## 五、阶段四：大屏布局骨架

### 5.1 准备静态资源

在 `src/assets/` 目录下放入以下文件：
- `header.png`：顶部 Banner 图片
- `china.js`：ECharts 中国地图地理数据（可从 ECharts 官网下载或项目中复制）

### 5.2 创建主视图骨架（`src/views/Index.vue`）

首先搭建 HTML 结构，**先不写任何数据逻辑**，只做布局：

```vue
<template>
  <div class="wrapper">
    <!-- 顶部 Header -->
    <header>
      <img src="../assets/header.png" />
      <div class="left"></div>
      <div class="right">
        <i class="fa fa-clock-o"></i>
        时间占位
      </div>
    </header>

    <!-- 主体内容：左 / 中 / 右 三栏 -->
    <div class="main">
      <!-- 左侧栏：仪表盘 + 饼图 + 折线图 -->
      <aside>
        <div class="grid-box">
          <ul>
            <li>
              <h4>全国省网格覆盖率(%)</h4>
              <div><!-- 仪表盘1 --></div>
            </li>
            <li>
              <h4>全国大城市网格覆盖率(%)</h4>
              <div><!-- 仪表盘2 --></div>
            </li>
          </ul>
        </div>
        <div><!-- 饼图 --></div>
        <div><!-- 折线图 --></div>
      </aside>

      <!-- 中间栏：地图 + 统计数字 -->
      <section>
        <div class="map"><!-- 中国地图 --></div>
        <div class="statistics">
          <h3>空气质量检测实时统计</h3>
          <ul>
            <li>
              <i class="fa fa-user-o"></i>
              <div>
                <h4>0</h4>
                <p>空气质量检测总数量</p>
              </div>
            </li>
            <li>
              <i class="fa fa-sun-o"></i>
              <div>
                <h4>0</h4>
                <p>空气质量良好数量</p>
              </div>
            </li>
            <li>
              <i class="fa fa-life-ring"></i>
              <div>
                <h4>0</h4>
                <p>空气质量污染数量</p>
              </div>
            </li>
          </ul>
        </div>
      </section>

      <!-- 右侧栏：三个柱状图 -->
      <article>
        <div><!-- 柱状图1：PM2.5 --></div>
        <div><!-- 柱状图2：SO2 --></div>
        <div><!-- 柱状图3：CO --></div>
      </article>
    </div>
  </div>
</template>

<script setup>
// 暂时为空
</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 100%;
  background-color: #001432;
  display: flex;
  flex-direction: column;
}
header {
  width: 100%;
  font-weight: 300;
  color: #aaf9fc;
  position: relative;
  display: flex;
  justify-content: space-between;
  flex: 0 0 2vw;
}
header img {
  display: block;
  width: 100%;
}
header div {
  position: absolute;
  font-size: 1vw;
}
header .left {
  left: 20px;
  top: 0.9vw;
}
header .right {
  right: 20px;
  top: 0.9vw;
}
.main {
  flex: 1;
  width: 100%;
  display: flex;
}
.main aside,
.main article {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.main aside div,
.main article div {
  flex: 1;
}
.main section {
  flex: 0 0 44%;
  display: flex;
  flex-direction: column;
}
.main section .map {
  flex: 3;
}
.main section .statistics {
  flex: 1;
  overflow: hidden;
}
.main aside .grid-box ul {
  width: 100%;
  height: 100%;
  display: flex;
}
.main aside .grid-box ul li {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.main aside .grid-box ul li h4 {
  flex: 1;
  text-align: center;
  color: #fff;
  font-size: 14px;
}
.main aside .grid-box ul li div {
  flex: 4;
}
.main section .statistics h3 {
  text-align: center;
  font-size: 14px;
  color: #fff;
  margin-top: 20px;
}
.main section .statistics ul {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
  color: #fff;
}
.main section .statistics ul li {
  display: flex;
  align-items: center;
}
.main section .statistics ul li .fa {
  font-size: 36px;
  color: #32ccee;
  margin-right: 10px;
}
.main section .statistics ul li div h4 {
  font-size: 26px;
}
.main section .statistics ul li div p {
  font-size: 14px;
  margin-top: 3px;
}
</style>
```

**✅ 测试：** 刷新页面，应看到深蓝色背景的三栏布局，顶部有 Banner 图片，底部有统计图标文字。

---

## 六、阶段五：封装 ECharts 图表组件

本阶段新建 5 个图表组件，每个组件遵循相同的模式：
1. 用 `ref` 绑定 DOM 容器
2. `onMounted` 中初始化 ECharts 实例
3. 用 `defineProps` 接收父组件传入的数据
4. 用 `watch` 监听数据变化后调用 `setOption` 更新图表

### 6.1 柱状图组件（`src/components/BarChart.vue`）

**Props 说明：**
- `bartext`：图表标题文字
- `barcolor`：柱子颜色
- `namedata`：X 轴分类数组（省份简称）
- `valuedata`：Y 轴数值数组

```vue
<!-- 柱状图组件 -->
<template>
  <div ref="barBox" style="width: 100%; height: 100%"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, watch } from 'vue'

const barBox = ref(null)

const myProps = defineProps({
  bartext:  { type: String, required: true },
  barcolor: { type: String, required: true },
  namedata: { type: Array,  required: true },
  valuedata:{ type: Array,  required: true },
})

let myChart = null
let option = ref(null)

// 监听 option 变化，自动更新图表
watch(() => option.value, () => {
  myChart.setOption(option.value)
}, { deep: true })

onMounted(() => {
  myChart = echarts.init(barBox.value, 'dark', {
    renderer: 'canvas',
    useDirtyRect: false,
  })

  option.value = {
    backgroundColor: '#001432',
    title: {
      text: myProps.bartext,
      textStyle: { fontSize: '14px' },
      left: 'center',
      top: '20px',
    },
    xAxis: { type: 'category', data: myProps.namedata },
    yAxis: { type: 'value' },
    series: [{
      data: myProps.valuedata,
      type: 'bar',
      itemStyle: { color: myProps.barcolor },
      barCategoryGap: '50%',
    }],
  }

  myChart.setOption(option.value)
  window.addEventListener('resize', myChart.resize)
})
</script>
```

### 6.2 仪表盘组件（`src/components/GaugeChart.vue`）

**Props 说明：**
- `gaugecolor`：指针及进度颜色
- `gaugedata`：仪表盘数据数组，格式为 `[{ value: 85 }]`

```vue
<template>
  <div ref="gaugeBox" style="width: 100%; height: 100%"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, watch } from 'vue'

const gaugeBox = ref(null)

const myProps = defineProps({
  gaugecolor: { type: String, required: true },
  gaugedata:  { type: Array,  required: true },
})

let myChart = null
let option = ref(null)

watch(() => option.value, () => {
  myChart.setOption(option.value)
}, { deep: true })

onMounted(() => {
  myChart = echarts.init(gaugeBox.value, null, {
    renderer: 'canvas',
    useDirtyRect: false,
  })

  option.value = {
    backgroundColor: '#001432',
    series: [{
      type: 'gauge',
      itemStyle: { color: myProps.gaugecolor },
      progress: { show: true, width: 8 },
      axisLine: { lineStyle: { width: 8 } },
      axisTick: { show: false },
      splitLine: { length: 0, lineStyle: { width: 0, color: '#999' } },
      axisLabel: { distance: 3, color: '#FFF', fontSize: 10 },
      anchor: { show: true, showAbove: true, size: 10, itemStyle: { borderWidth: 10 } },
      title: { show: false },
      detail: {
        valueAnimation: true,
        fontSize: 20,
        offsetCenter: [0, '90%'],
        color: myProps.gaugecolor,
      },
      data: myProps.gaugedata,
    }],
  }

  myChart.setOption(option.value)
  window.addEventListener('resize', myChart.resize)
})
</script>
```

### 6.3 饼图组件（`src/components/PieChart.vue`）

**Props 说明：**
- `pieData`：饼图数据数组，格式为 `[{ name: '优', value: 120 }, ...]`

```vue
<template>
  <div ref="pieBox" style="width: 100%; height: 100%"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, watch } from 'vue'

const pieBox = ref(null)

const myProps = defineProps({
  pieData: { type: Array, required: true },
})

let myChart = null
let option = ref(null)

watch(() => option.value, () => {
  myChart.setOption(option.value)
}, { deep: true })

onMounted(() => {
  myChart = echarts.init(pieBox.value, 'dark', {
    renderer: 'canvas',
    useDirtyRect: false,
  })

  option.value = {
    backgroundColor: '#001432',
    title: {
      text: '空气质量指数级别分布',
      left: 'center',
      textStyle: { fontSize: '14px' },
    },
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left', top: 'bottom' },
    series: [{
      name: '空气质量指数级别分布',
      type: 'pie',
      radius: '50%',
      data: myProps.pieData,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    }],
  }

  myChart.setOption(option.value)
  window.addEventListener('resize', myChart.resize)
})
</script>
```

### 6.4 折线图组件（`src/components/LineChart.vue`）

**Props 说明：**
- `linenamedata`：X 轴月份数组，如 `['01', '02', ..., '12']`
- `linevaluedata`：Y 轴数值数组

```vue
<template>
  <div ref="lineBox" style="width: 100%; height: 100%"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, watch } from 'vue'

const lineBox = ref(null)

const myProps = defineProps({
  linenamedata:  { type: Array, required: true },
  linevaluedata: { type: Array, required: true },
})

let myChart = null
let option = ref(null)

watch(() => option.value, () => {
  myChart.setOption(option.value)
}, { deep: true })

onMounted(() => {
  myChart = echarts.init(lineBox.value, 'dark', {
    renderer: 'canvas',
    useDirtyRect: false,
  })

  option.value = {
    backgroundColor: '#001432',
    title: {
      text: '12个月内空气质量超标趋势',
      textStyle: { fontSize: '14px' },
      left: 'center',
      top: '20px',
    },
    xAxis: { type: 'category', data: myProps.linenamedata },
    yAxis: { type: 'value' },
    series: [{
      data: myProps.linevaluedata,
      type: 'line',
      smooth: true,   // 平滑曲线
    }],
  }

  myChart.setOption(option.value)
  window.addEventListener('resize', myChart.resize)
})
</script>
```

### 6.5 中国地图组件（`src/components/ChinaMap.vue`）

> **前置步骤**：需要在 `src/assets/china.js` 中包含 ECharts 地图注册代码。该文件通过 `import` 引入后，ECharts 会自动注册名为 `'china'` 的地图数据。

**Props 说明：**
- `mapArrData`：各省数据数组，格式为 `[{ name: '北京', value: 3 }, ...]`

```vue
<template>
  <div ref="chinaMapBox" style="width: 100%; height: 100%"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import '../assets/china.js'   // 注册中国地图数据
import { ref, onMounted, watch } from 'vue'

const chinaMapBox = ref(null)

const myProps = defineProps({
  mapArrData: { type: Array, required: true },
})

let myChart = null
let option = ref(null)

watch(() => option.value, () => {
  myChart.setOption(option.value)
}, { deep: true })

onMounted(() => {
  myChart = echarts.init(chinaMapBox.value, null, {
    renderer: 'canvas',
    useDirtyRect: false,
  })

  option.value = {
    backgroundColor: '#001432',
    series: {
      type: 'map',
      mapType: 'china',
      label: {
        normal:    { show: true, color: '#000' },
        emphasis:  { show: true, color: 'red'  },
      },
      data: myProps.mapArrData,
      name: '累计超标',
    },
    visualMap: {
      left: 'left',
      top: 'bottom',
      splitList: [
        { start: 4, end: 5 },
        { start: 3, end: 4 },
        { start: 2, end: 3 },
        { start: 1, end: 2 },
        { start: 0, end: 1 },
      ],
      color: ['#9D2933', '#CB3A56', '#FF7500', '#FAFF72', '#C9DD22'],
      show: true,
    },
    tooltip: { trigger: 'item' },
  }

  myChart.setOption(option.value)
  window.addEventListener('resize', myChart.resize)
})
</script>
```

**✅ 测试：** 此时五个组件文件均已创建，暂时先在 `Index.vue` 中引入并用静态假数据测试各图表是否能正常渲染（详见下一步）。

---

## 七、阶段六：接入后端数据

### 7.1 在 `Index.vue` 中引入所有组件并配置 Axios

在 `<script setup>` 顶部添加引入：

```js
import BarChart   from '../components/BarChart.vue'
import GaugeChart from '../components/GaugeChart.vue'
import PieChart   from '../components/PieChart.vue'
import LineChart  from '../components/LineChart.vue'
import ChinaMap   from '../components/ChinaMap.vue'

import axios from 'axios'
import { ref, onMounted } from 'vue'

// 配置后端接口基础路径
axios.defaults.baseURL = 'http://localhost:8080/nepm/'
```

### 7.2 声明所有响应式数据变量

```js
// 柱状图数据（省分组分项检测数据统计）
const provinceNameData = ref([])   // 省份名称（X轴）
const spmValueData     = ref([])   // PM2.5 超标数
const so2ValueData     = ref([])   // SO2 超标数
const coValueData      = ref([])   // CO 超标数

// 仪表盘数据（网格覆盖率）
const provinceCoverage = ref([])
const cityCoverage     = ref([])

// 饼图数据（AQI 级别分布）
const pieArrData = ref([])

// 折线图数据（12个月趋势）
const linenameData  = ref([])
const linevalueData = ref([])

// 中国地图数据
const mapArrData = ref([])

// 实时统计数字
const aqiCount     = ref(0)
const aqiGoodCount = ref(0)
```

### 7.3 封装数据初始化函数 `init()`

```js
const init = () => {

  // 接口1：省分组分项检测数据 → 柱状图 + 地图
  axios.post('statistics/listProvinceItemTotalStatis')
    .then((response) => {
      // 清空旧数据
      provinceNameData.value.length = 0
      spmValueData.value.length     = 0
      so2ValueData.value.length     = 0
      coValueData.value.length      = 0
      mapArrData.value.length       = 0

      let arr = response.data
      for (let i = 0; i < arr.length; i++) {
        provinceNameData.value.push(arr[i].provinceAbbr)   // 省份简称
        spmValueData.value.push(arr[i].spmTotal)
        so2ValueData.value.push(arr[i].so2Total)
        coValueData.value.push(arr[i].coTotal)
        mapArrData.value.push({
          name:  arr[i].provinceName,  // 全称（地图用）
          value: arr[i].aqiTotal
        })
      }
    })
    .catch(error => console.log(error))

  // 接口2：大城市网格覆盖率 → 仪表盘
  axios.post('statistics/getCityCoverage')
    .then((response) => {
      cityCoverage.value.length = 0
      cityCoverage.value.push({ value: response.data })
    })
    .catch(error => console.log(error))

  // 接口3：全省网格覆盖率 → 仪表盘
  axios.post('statistics/getProvinceCoverage')
    .then((response) => {
      provinceCoverage.value.length = 0
      provinceCoverage.value.push({ value: response.data })
    })
    .catch(error => console.log(error))

  // 接口4：AQI 指数级别分布 → 饼图
  axios.post('statistics/listAqiDistributeTotalStatis')
    .then((response) => {
      pieArrData.value.length = 0
      let arr = response.data
      for (let i = 0; i < arr.length; i++) {
        pieArrData.value.push({
          name:  arr[i].aqiExplain.substr(0, 2),  // 取前两个字
          value: arr[i].total
        })
      }
    })
    .catch(error => console.log(error))

  // 接口5：12个月超标趋势 → 折线图
  axios.post('statistics/listAqiTrendTotalStatis')
    .then((response) => {
      linenameData.value.length  = 0
      linevalueData.value.length = 0
      let arr = response.data
      for (let i = 0; i < arr.length; i++) {
        linenameData.value.unshift(arr[i].month.substr(5, 2))  // 取月份两位
        linevalueData.value.unshift(arr[i].total)
      }
    })
    .catch(error => console.log(error))

  // 接口6：检测总数量 → 统计数字
  axios.post('statistics/getAqiCount')
    .then((response) => { aqiCount.value = response.data })
    .catch(error => console.log(error))

  // 接口7：良好数量 → 统计数字
  axios.post('statistics/getAqiGoodCount')
    .then((response) => { aqiGoodCount.value = response.data })
    .catch(error => console.log(error))
}

// 页面加载时立即调用
init()
```

### 7.4 在模板中绑定组件与数据

将阶段四中的注释占位符替换为真实的组件标签（完整模板见下方）：

```html
<!-- 左侧：两个仪表盘 -->
<GaugeChart gaugecolor="#67C23A" :gaugedata="provinceCoverage" />
<GaugeChart gaugecolor="#F56C6C" :gaugedata="cityCoverage" />

<!-- 左侧：饼图 -->
<PieChart :pieData="pieArrData" />

<!-- 左侧：折线图 -->
<LineChart :linenamedata="linenameData" :linevaluedata="linevalueData" />

<!-- 中间：地图 -->
<ChinaMap :mapArrData="mapArrData" />

<!-- 中间：统计数字 -->
<h4>{{ aqiCount }}</h4>
<h4>{{ aqiGoodCount }}</h4>
<h4>{{ aqiCount - aqiGoodCount }}</h4>

<!-- 右侧：三个柱状图 -->
<BarChart bartext="悬浮颗粒物（PM2.5）浓度超标累计"
          barcolor="#409EFF"
          :namedata="provinceNameData"
          :valuedata="spmValueData" />

<BarChart bartext="二氧化硫（SO2）浓度超标累计"
          barcolor="#67C23A"
          :namedata="provinceNameData"
          :valuedata="so2ValueData" />

<BarChart bartext="一氧化碳（CO）浓度超标累计"
          barcolor="#F56C6C"
          :namedata="provinceNameData"
          :valuedata="coValueData" />
```

**✅ 测试：** 确保后端服务已运行，刷新页面，所有图表应渲染出真实数据。若图表为空白，打开浏览器控制台查看网络请求是否成功（检查 CORS 跨域配置）。

---

## 八、阶段七：实时刷新与时钟

### 8.1 添加实时时钟

```js
const currentTime = ref('')

const updateTime = () => {
  const date = new Date()
  currentTime.value = date.toLocaleString()  // 格式：年/月/日 时:分:秒
}

onMounted(() => {
  updateTime()                          // 立即执行一次，避免初始空白
  setInterval(updateTime, 1000)         // 每秒更新
})
```

在模板 `header` 区域绑定：

```html
<div class="right">
  <i class="fa fa-clock-o"></i>
  {{ currentTime }}
</div>
```

### 8.2 添加数据轮询（每 5 秒刷新一次）

```js
// 在 init() 调用之后添加
setInterval(() => {
  init()
}, 5000)
```

> **知识点**：轮询是最简单的实时数据方案，每隔固定时间重新请求接口。`arr.length = 0` 的方式清空数组是为了保留原始响应式引用，避免 Vue 无法检测到变化。

**✅ 测试：** 观察页面右上角时钟是否每秒更新，统计数字是否会随后端数据变化而刷新。

---

## 九、阶段八：测试与调试

### 9.1 界面检查清单

| 检查项 | 预期结果 |
|--------|---------|
| 页面背景色 | 深蓝色 `#001432` |
| 顶部 Banner | 图片正常显示，右侧有实时时钟 |
| 左侧两个仪表盘 | 分别显示省/城市覆盖率百分比 |
| 左侧饼图 | 显示 AQI 等级分布，有图例 |
| 左侧折线图 | 显示 12 个月趋势曲线 |
| 中间中国地图 | 各省有颜色热力分布，可悬停提示 |
| 中间统计数字 | 三个数值分别显示总量、良好量、污染量 |
| 右侧三个柱状图 | 分别显示 PM2.5、SO2、CO 数据，颜色不同 |
| 数据轮询 | 每 5 秒数字/图表自动刷新 |

### 9.2 常见问题排查

**图表不显示（空白）**

1. 打开控制台，查看是否有 JS 报错
2. 检查 Network 标签，确认接口请求是否返回 200
3. 确认后端服务已启动（`http://localhost:8080/nepm/`）

**跨域错误（CORS）**

控制台出现 `Access-Control-Allow-Origin` 错误时，需要在后端 Spring Boot 中配置跨域，或在 `vite.config.js` 中添加代理：

```js
server: {
  port: 8084,
  proxy: {
    '/nepm': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

同时将 `axios.defaults.baseURL` 改为 `'/nepm/'`。

**地图不显示**

确认 `src/assets/china.js` 文件存在，且在 `ChinaMap.vue` 中已通过 `import '../assets/china.js'` 引入。

**图表在窗口缩放后变形**

确认每个组件的 `onMounted` 中都有：

```js
window.addEventListener('resize', myChart.resize)
```

---

## 十、阶段九：构建与部署

### 10.1 生产环境构建

```bash
npm run build
```

构建完成后会生成 `dist/` 目录，里面包含所有静态资源。

### 10.2 本地预览构建结果

```bash
npm run preview
```

默认在 `http://localhost:4173` 打开，检查构建后的产物是否正常。

### 10.3 部署到 Nginx

将 `dist/` 目录内容上传到服务器，Nginx 配置示例：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /var/www/nepv/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;  # 支持 Hash 路由
    }
}
```

> 由于使用的是 `createWebHashHistory`（Hash 模式），即使不配置 `try_files` 也不会出现 404，但建议加上以保持一致性。

---

## 十一、项目文件结构速查

```
nepv/
├── index.html                  # 页面入口 HTML
├── vite.config.js              # Vite 构建配置（端口、路径别名）
├── package.json                # 依赖清单
├── public/
│   └── favicon.ico
└── src/
    ├── main.js                 # 应用入口（挂载 App、路由、Font Awesome）
    ├── App.vue                 # 根组件（CSS 重置 + router-view）
    ├── router/
    │   └── index.js            # 路由配置（Hash 模式，只有 Index 页）
    ├── assets/
    │   ├── header.png          # 顶部 Banner 图片
    │   └── china.js            # ECharts 中国地图数据
    ├── components/             # 可复用图表组件
    │   ├── BarChart.vue        # 柱状图（props: bartext, barcolor, namedata, valuedata）
    │   ├── GaugeChart.vue      # 仪表盘（props: gaugecolor, gaugedata）
    │   ├── PieChart.vue        # 饼图（props: pieData）
    │   ├── LineChart.vue       # 折线图（props: linenamedata, linevaluedata）
    │   └── ChinaMap.vue        # 中国地图（props: mapArrData）
    └── views/
        └── Index.vue           # 大屏主视图（整合所有组件 + axios 数据请求）
```

---

## 开发顺序总结

```
阶段一：创建项目 + 配置 Vite 端口
    ↓
阶段二：安装依赖（echarts/axios/font-awesome）+ 配置路由
    ↓
阶段三：配置 main.js 入口 + App.vue 全局 CSS 重置
    ↓
阶段四：搭建 Index.vue 的 HTML 骨架 + Flex 布局 CSS
    ↓
阶段五：逐一创建 5 个图表组件（用假数据测试各组件是否渲染正常）
    ↓
阶段六：在 Index.vue 中引入组件，接入 axios 请求真实后端数据
    ↓
阶段七：添加实时时钟 + 5 秒轮询刷新
    ↓
阶段八：全面测试（界面 + 数据 + 跨域 + 自适应）
    ↓
阶段九：npm run build 构建 + 部署到服务器
```

