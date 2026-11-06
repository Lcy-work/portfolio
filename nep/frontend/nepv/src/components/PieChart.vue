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