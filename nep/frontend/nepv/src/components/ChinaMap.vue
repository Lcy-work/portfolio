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