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