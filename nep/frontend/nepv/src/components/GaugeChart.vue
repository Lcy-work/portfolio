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