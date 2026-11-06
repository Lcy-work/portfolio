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