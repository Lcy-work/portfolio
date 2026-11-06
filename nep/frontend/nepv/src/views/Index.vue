<template>
  <div class="wrapper">
    <!-- 顶部 Header -->
    <header>
      <img src="../assets/header.png" />
      <div class="left"></div>
      <div class="right">
        <i class="fa fa-clock-o"></i>
        {{ currentTime }}
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
              <div>
                <GaugeChart gaugecolor="#67C23A" :gaugedata="provinceCoverage" />
              </div>
            </li>
            <li>
              <h4>全国大城市网格覆盖率(%)</h4>
              <div>
                <GaugeChart gaugecolor="#F56C6C" :gaugedata="cityCoverage" />
              </div>
            </li>
          </ul>
        </div>
        <div>
          <PieChart :pieData="pieArrData" />
        </div>
        <div>
          <LineChart :linenamedata="linenameData" :linevaluedata="linevalueData" />
        </div>
      </aside>

      <!-- 中间栏：地图 + 统计数字 -->
      <section>
        <div class="map">
          <ChinaMap :mapArrData="mapArrData" />
        </div>
        <div class="statistics">
          <h3>空气质量检测实时统计</h3>
          <ul>
            <li>
              <i class="fa fa-user-o"></i>
              <div>
                <h4>{{ aqiCount }}</h4>
                <p>空气质量检测总数量</p>
              </div>
            </li>
            <li>
              <i class="fa fa-sun-o"></i>
              <div>
                <h4>{{ aqiGoodCount }}</h4>
                <p>空气质量良好数量</p>
              </div>
            </li>
            <li>
              <i class="fa fa-life-ring"></i>
              <div>
                <h4>{{ aqiCount - aqiGoodCount }}</h4>
                <p>空气质量污染数量</p>
              </div>
            </li>
          </ul>
        </div>
      </section>

      <!-- 右侧栏：三个柱状图 -->
      <article>
        <div>
          <BarChart bartext="悬浮颗粒物（PM2.5）浓度超标累计"
                    barcolor="#409EFF"
                    :namedata="provinceNameData"
                    :valuedata="spmValueData" />
        </div>
        <div>
          <BarChart bartext="二氧化硫（SO2）浓度超标累计"
                    barcolor="#67C23A"
                    :namedata="provinceNameData"
                    :valuedata="so2ValueData" />
        </div>
        <div>
          <BarChart bartext="一氧化碳（CO）浓度超标累计"
                    barcolor="#F56C6C"
                    :namedata="provinceNameData"
                    :valuedata="coValueData" />
        </div>
      </article>
    </div>
  </div>
</template>

<script setup>
import BarChart   from '../components/BarChart.vue'
import GaugeChart from '../components/GaugeChart.vue'
import PieChart   from '../components/PieChart.vue'
import LineChart  from '../components/LineChart.vue'
import ChinaMap   from '../components/ChinaMap.vue'

import axios from 'axios'
import { ref, onMounted } from 'vue'

// 配置后端接口基础路径（使用相对路径，通过 Vite 代理转发）
axios.defaults.baseURL = '/nepm/'

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

// 实时时钟
const currentTime = ref('')

const updateTime = () => {
  const date = new Date()
  currentTime.value = date.toLocaleString()  // 格式：年/月/日 时:分:秒
}

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
      cityCoverage.value.push({ value: parseFloat(response.data) })
    })
    .catch(error => console.log(error))

  // 接口3：全省网格覆盖率 → 仪表盘
  axios.post('statistics/getProvinceCoverage')
    .then((response) => {
      provinceCoverage.value.length = 0
      provinceCoverage.value.push({ value: parseFloat(response.data) })
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
onMounted(() => {
  init()
  updateTime()                          // 立即执行一次，避免初始空白
  setInterval(updateTime, 1000)         // 每秒更新时钟
  setInterval(() => {
    init()                              // 每 5 秒刷新一次数据
  }, 5000)
})
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
