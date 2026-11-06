<template>
  <el-row justify="space-between" class="page-header">
    <el-col :span="10">
      <h2>参保人员费用报销详情</h2>
    </el-col>
    <el-col :span="2">
      <el-button type="primary" @click="backToMain">返回</el-button>
    </el-col>
  </el-row>
  <el-row class="patient-info">
    <el-col :span="6">
      <div class="info-item">
        <span class="info-label">姓名：</span>
        <el-tag class="info-value">{{ patientEntity.realName }}</el-tag>
      </div>
    </el-col>
    <el-col :span="6">
      <div class="info-item">
        <span class="info-label">性别：</span>
        <el-tag class="info-value">{{ patientEntity.gender }}</el-tag>
      </div>
    </el-col>
    <el-col :span="6">
      <div class="info-item">
        <span class="info-label">住院号：</span>
        <el-tag class="info-value">{{ patientEntity.caseNumber }}</el-tag>
      </div>
    </el-col>
    <el-col :span="6">
      <div class="info-item">
        <span class="info-label">总费用：</span>
        <el-tag class="info-value total-money">{{ allMoney.total }}</el-tag>
      </div>
    </el-col>
  </el-row>
  <el-divider />
  <el-row class="diagnosis-info">
    <el-col :span="8">
      <div class="diagnosis-item">
        <span class="diagnosis-label">入院诊断：</span>
        <span class="diagnosis-value">{{ firstReim }}</span>
      </div>
    </el-col>
    <el-col :span="8">
      <div class="diagnosis-item">
        <span class="diagnosis-label">主要诊断：</span>
        <span class="diagnosis-value">{{ mainReim }}</span>
      </div>
    </el-col>
    <el-col :span="8">
      <div class="diagnosis-item">
        <span class="diagnosis-label">其他诊断：</span>
        <span class="diagnosis-value">{{ otherReim }}</span>
      </div>
    </el-col>
  </el-row>
  <el-row :gutter="20" class="chart-container">
    <el-col :span="12">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <el-icon class="card-icon"><SetUp /></el-icon>
            <span>药品费用图表</span>
          </div>
        </template>
        <div>
          <div id="drugEcharts" class="chart" :style="{width:'100%',height:'400px'}"></div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <el-icon class="card-icon"><Operation /></el-icon>
            <span>报销详情图表</span>
          </div>
        </template>
        <div>
          <div id="moneyEcharts" class="chart" :style="{width:'100%',height:'400px'}"></div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import * as echarts from 'echarts';
import { ref, onMounted, nextTick } from 'vue';
import router from '@/router';
import { useRoute } from 'vue-router';
import { api } from "@/api/index"; // 导入统一API

const patientId = ref();
const patientEntity = ref({});
const firstReim = ref("");
const mainReim = ref("");
const otherReim = ref("");

// 药品费用分类
const DrugMoney = ref({
  AType: 0,
  BType: 0,
  CType: 0,
});

// 总费用统计
const allMoney = ref({
  drug: 0,
  dianosis: 0,
  medical: 0,
  total: 0
});

// 初始化药品费用图表
const initDrugChart = () => {
  nextTick(() => {
    const myChart = echarts.init(document.getElementById('drugEcharts'), "light");
    myChart.setOption({
      title: {
        text: '药品费用占比分析',
        left: 'center',
        textStyle: {
          color: '#333',
          fontSize: 18,
          fontWeight: 'bold'
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b} <br/>药品总金额：{c}元 <br/> 药品占比：{d}%',
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        textStyle: {
          color: '#333'
        },
        borderColor: '#ddd',
        borderWidth: 1,
        shadowBlur: 5
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        itemGap: 15,
        textStyle: {
          color: '#666'
        }
      },
      series: [
        {
          name: '药品费用',
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],
          data: [
            { value: DrugMoney.value.AType, name: '甲类药品', itemStyle: { color: '#52C41A' } },
            { value: DrugMoney.value.BType, name: '乙类药品', itemStyle: { color: '#1890FF' } },
            { value: DrugMoney.value.CType, name: '丙类药品', itemStyle: { color: '#FAAD14' } },
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
          },
          label: {
            color: '#333',
            formatter: '{b}\n {hr|} \n 金额：{c}元\n 百分比：{d}%',
            borderWidth: 1,
            borderColor: '#eee',
            borderRadius: 4,
            rich: {
              hr: {
                borderColor: "#8C8D8E",
                width: '100%',
                borderWidth: 1,
                height: 0
              }
            }
          },
          labelLine: {
            length: 10,
            length2: 10,
            lineStyle: {
              color: '#ccc'
            }
          }
        }
      ]
    });
    window.addEventListener('resize', () => myChart.resize());
  });
};

// 初始化报销详情图表
const initMoneyChart = () => {
  nextTick(() => {
    const myChart = echarts.init(document.getElementById('moneyEcharts'), "light");
    myChart.setOption({
      title: {
        text: '报销类型占比分析',
        left: 'center',
        textStyle: {
          color: '#333',
          fontSize: 18,
          fontWeight: 'bold'
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b} <br/>报销总金额：{c}元 <br/> 占比：{d}%',
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        textStyle: {
          color: '#333'
        },
        borderColor: '#ddd',
        borderWidth: 1,
        shadowBlur: 5
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        itemGap: 15,
        textStyle: {
          color: '#666'
        }
      },
      series: [
        {
          name: '医疗保险报销费用',
          type: 'pie',
          radius: ['35%', '60%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2,
          },
          data: [
            { value: allMoney.value.drug, name: '保险药品', itemStyle: { color: '#52C41A' } },
            { value: allMoney.value.dianosis, name: '诊疗项目', itemStyle: { color: '#1890FF' } },
            { value: allMoney.value.medical, name: '医疗服务', itemStyle: { color: '#FAAD14' } },
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
          },
          label: {
            color: '#333',
            formatter: '{b}\n {hr|} \n 金额：{c}元\n 百分比：{d}%',
            borderWidth: 1,
            borderColor: '#eee',
            borderRadius: 4,
            rich: {
              hr: {
                borderColor: "#8C8D8E",
                width: '100%',
                borderWidth: 1,
                height: 0
              }
            }
          },
          labelLine: {
            length: 10,
            length2: 10,
            lineStyle: {
              color: '#ccc'
            }
          }
        }
      ]
    });
    window.addEventListener('resize', () => myChart.resize());
  });
};

// 返回报销列表页
const backToMain = () => {
  router.push("/patientreimbursement");
};

// 获取参保人员基本信息
const getPatientById = () => {
  api.patientRegistration.getById(patientId.value).then(response => {
    patientEntity.value = response.data;
  });
};

// 获取入院诊断信息
const getFirst = () => {
  api.inpatientDisease.getByPatientAndType(patientId.value, 1).then(response => {
    firstReim.value = response.data;
  });
};

// 获取主要诊断信息
const getMain = () => {
  api.inpatientDisease.getByPatientAndType(patientId.value, 2).then(response => {
    mainReim.value = response.data;
  });
};

// 获取其他诊断信息
const getOther = () => {
  api.inpatientDisease.getByPatientAndType(patientId.value, 3).then(response => {
    otherReim.value = response.data;
  });
};

// 初始化参保人员费用数据
const initPatientMoney = () => {
  const pid = patientId.value;
  // 加载药品费用
  api.inpatientDrug.getByPatientId(pid).then(response => {
    const drugList = response.data.data;
    drugList.forEach(item => {
      if (item.insuranceType === '甲类') {
        DrugMoney.value.AType += item.drugPrice;
      } else if (item.insuranceType === '乙类') {
        DrugMoney.value.BType += item.drugPrice;
      } else if (item.insuranceType === '丙类') {
        DrugMoney.value.CType += item.drugPrice;
      }
    });
    allMoney.value.drug = DrugMoney.value.AType + DrugMoney.value.BType + DrugMoney.value.CType;
    initDrugChart();

    // 加载诊疗项目费用
    api.inpatientDiagnosis.getByPatientId(pid).then(response => {
      const dianosisList = response.data.data;
      dianosisList.forEach(item => {
        allMoney.value.dianosis += item.medicalPrice;
      });

      // 加载医疗服务费用
      api.inpatientMedical.getByPatientId(pid).then(response => {
        const medicalList = response.data.data;
        medicalList.forEach(item => {
          allMoney.value.medical += item.medicalPrice;
        });
        allMoney.value.total = allMoney.value.drug + allMoney.value.dianosis + allMoney.value.medical;
        initMoneyChart();
      });
    });
  });
};

// 页面初始化
onMounted(() => {
  const route = useRoute();
  patientId.value = route.params.id;
  getPatientById();
  getFirst();
  getMain();
  getOther();
  initPatientMoney();
});
</script>

<style scoped>
/* 页面头部样式 */
.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 患者信息样式 */
.patient-info {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.info-label {
  font-size: 16px;
  color: #666;
  width: 60px;
  margin-right: 10px;
}

.info-value {
  font-size: 16px;
  color: #333;
  padding: 4px 8px;
  border-radius: 4px;
}

.total-money {
  background-color: #1890FF;
  color: #fff;
}

/* 诊断信息样式 */
.diagnosis-info {
  margin-bottom: 20px;
}

.diagnosis-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.diagnosis-label {
  font-size: 16px;
  color: #666;
  
  font-weight: bold;

  width: 80px;
  margin-right: 10px;
}

.diagnosis-value {
  font-size: 16px;
  
  color: #333;
}

/* 图表容器样式 */
.chart-container {
  margin-top: 20px;
}

.chart-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: 500;
}

.card-icon {
  margin-right: 8px;
  color: #1890FF;
}

/* 图表样式 */
.chart {
  border-radius: 4px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}
</style>