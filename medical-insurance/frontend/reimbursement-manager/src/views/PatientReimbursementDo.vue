<template>
  <div class="reimbursement-container">
    <!-- 页面头部 -->
    <el-row justify="space-between" class="page-header">
      <el-col :span="10">
        <h2>参保人员费用报销</h2>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" @click="backToMain">返回</el-button>
      </el-col>
    </el-row>

    <!-- 患者基本信息 -->
    <el-row class="patient-info">
      <el-col :span="6">
        <div class="info-item">
          <span class="info-label">姓名：</span>
          <el-tag class="info-value">{{ patientEntity.realName }}</el-tag>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="info-item">
          <span class="info-label">人员类别：</span>
          <el-tag class="info-value">{{ patientEntity.workStatus }}</el-tag>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="info-item">
          <span class="info-label">结算类别：</span>
          <el-tag class="info-value">{{ patientEntity.registMethod }}</el-tag>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="info-item">
          <span class="info-label">总费用：</span>
          <el-tag class="info-value total-money">{{ allMoney.total }} 元</el-tag>
        </div>
      </el-col>
    </el-row>

    <el-divider />

    <!-- 药品分类卡片 -->
    <el-row :gutter="20" class="drug-cards">
      <el-col :span="6">
        <el-card class="drug-card">
          <template #header>
            <div class="card-header">
              <el-icon class="card-icon"><SetUp /></el-icon>
              <span>甲类药品</span>
              <el-tag class="reimbursement-tag">报销比例{{ drugRem.AReimbursement }}%</el-tag>
              <el-tag class="cost-tag">费用 {{ allMoney.ADrug }} 元</el-tag>
            </div>
          </template>
          <el-scrollbar height="300px">
            <el-table :data="drugList.AType" stripe>
              <el-table-column prop="chinaName" label="药品名称" />
              <el-table-column prop="drugUnit" label="单位" width="80" />
              <el-table-column prop="drugPrice" label="价格" width="80" />
            </el-table>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="drug-card">
          <template #header>
            <div class="card-header">
              <el-icon class="card-icon"><SetUp /></el-icon>
              <span>乙类药品</span>
              <el-tag class="reimbursement-tag">报销比例{{ drugRem.BReimbursement }}%</el-tag>
              <el-tag class="cost-tag">费用 {{ allMoney.BDrug }} 元</el-tag>
            </div>
          </template>
          <el-scrollbar height="300px">
            <el-table :data="drugList.BType" stripe>
              <el-table-column prop="chinaName" label="药品名称" />
              <el-table-column prop="drugUnit" label="单位" width="80" />
              <el-table-column prop="drugPrice" label="价格" width="80" />
            </el-table>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="drug-card">
          <template #header>
            <div class="card-header">
              <el-icon class="card-icon"><SetUp /></el-icon>
              <span>丙类药品</span>
              <el-tag class="reimbursement-tag">报销比例{{ drugRem.CReimbursement }}%</el-tag>
              <el-tag class="cost-tag">费用 {{ allMoney.CDrug }} 元</el-tag>
            </div>
          </template>
          <el-scrollbar height="300px">
            <el-table :data="drugList.CType" stripe>
              <el-table-column prop="chinaName" label="药品名称" />
              <el-table-column prop="drugUnit" label="单位" width="80" />
              <el-table-column prop="drugPrice" label="价格" width="80" />
            </el-table>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="drug-card">
          <template #header>
            <div class="card-header">
              <el-icon class="card-icon"><SetUp /></el-icon>
              <span>其他项目</span>
              <el-tag class="cost-tag">费用 {{ allMoney.other }} 元</el-tag>
            </div>
          </template>
          <el-scrollbar height="300px">
            <el-table :data="otherList" stripe>
              <el-table-column prop="medicalName" label="项目名称" />
              <el-table-column prop="medicalUnit" label="单位" width="80" />
              <el-table-column prop="medicalPrice" label="价格" width="80" />
            </el-table>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报销计算规则 -->
    <el-card class="reimbursement-card">
      <template #header>
        <div class="card-header">
          <el-icon class="card-icon"><SetUp /></el-icon>
          <span style="">医保报销费用计算</span>
        </div>
      </template>
      <el-scrollbar height="200px">
        <el-table :data="tableDataList02" stripe border style="width: 80%; margin: 0 auto;">
          <el-table-column label="起付线" prop="minPayLevel" align="center" />
          <el-table-column label="等级线" prop="maxPayLevel" align="center" />
          <el-table-column label="报销比例" prop="payProportion" align="center">
            <template #default="scope">
              <el-tag effect="dark">{{ scope.row.payProportion }} %</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-scrollbar>
    </el-card>

    <!-- 报销结果 -->
    <el-row justify="space-between" class="reimbursement-footer">
      <el-col :span="16">
        <h3>报销费用：</h3>
        <el-tag class="total-reimbursement-tag">{{ allMoney.allPay }} 元</el-tag>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" @click="open">确认</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import router from "@/router";
import { ref, onMounted, computed, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute } from "vue-router";
import { api } from "@/api/index"; // 导入统一API

// 参保人员信息
const patientEntity = ref({});
// 参保人员ID
const patientId = ref();
// 药品分类列表（甲/乙/丙类）
const drugList = ref({
  AType: [],
  BType: [],
  CType: []
});
// 其他项目列表（诊疗项目+医疗服务）
const otherList = ref([]);
// 药品报销比例
const drugRem = ref({
  AReimbursement: 0,
  BReimbursement: 0,
  CReimbursement: 0
});
// 费用统计
const allMoney = reactive({
  ADrug: 0,    // 甲类药品总费用
  BDrug: 0,    // 乙类药品总费用
  CDrug: 0,    // 丙类药品总费用
  other: 0,    // 其他项目总费用
  total: 0,    // 总费用
  allPay: 0    // 可报销总费用
});
// 医院报销阶梯数据
const tableDataList02 = reactive([]);

// 计算总费用（甲类+乙类+丙类+其他）
allMoney.total = computed(() => {
  return (allMoney.ADrug + allMoney.BDrug + allMoney.CDrug + allMoney.other).toFixed(2);
});

// 计算可报销总费用
allMoney.allPay = computed(() => {
  // 基础报销费用（按药品类别比例计算）
  let basePay = allMoney.ADrug * (drugRem.value.AReimbursement / 100)
    + allMoney.BDrug * (drugRem.value.BReimbursement / 100)
    + allMoney.CDrug * (drugRem.value.CReimbursement / 100)
    + allMoney.other;

  // 根据医院报销阶梯计算最终报销费用
  if (tableDataList02.length > 0) {
    if (basePay < tableDataList02[0].minPayLevel) {
      basePay = 0; // 未达起付线，不报销
    } else if (basePay < tableDataList02[0].maxPayLevel) {
      basePay = (basePay - tableDataList02[0].minPayLevel) * (tableDataList02[0].payProportion / 100);
    } else if (basePay < tableDataList02[1]?.maxPayLevel) {
      basePay = (basePay - tableDataList02[1].minPayLevel) * (tableDataList02[1].payProportion / 100)
        + (tableDataList02[0].maxPayLevel - tableDataList02[0].minPayLevel) * (tableDataList02[0].payProportion / 100);
    } else if (basePay < tableDataList02[2]?.maxPayLevel) {
      basePay = (basePay - tableDataList02[2].minPayLevel) * (tableDataList02[2].payProportion / 100)
        + (tableDataList02[0].maxPayLevel - tableDataList02[0].minPayLevel) * (tableDataList02[0].payProportion / 100)
        + (tableDataList02[1].maxPayLevel - tableDataList02[1].minPayLevel) * (tableDataList02[1].payProportion / 100);
    }
  }

  return basePay.toFixed(2);
});

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

// 获取药品报销比例（甲/乙/丙类）
const getDrugReimbursement = () => {
  api.drugReimbursement.getAll().then(response => {
    response.data.forEach(item => {
      if (item.drugReimbursementType === '甲类') {
        drugRem.value.AReimbursement = item.drugReimbursementProportion;
      } else if (item.drugReimbursementType === '乙类') {
        drugRem.value.BReimbursement = item.drugReimbursementProportion;
      } else if (item.drugReimbursementType === '丙类') {
        drugRem.value.CReimbursement = item.drugReimbursementProportion;
      }
    });
  });
};

// 初始化参保人员费用数据（药品+诊疗+医疗服务）
const initPatient = () => {
  const pid = patientId.value;

  // 加载药品费用并分类统计
  api.inpatientDrug.getByPatientId(pid).then(response => {
    const drugData = response.data.data;
    drugData.forEach(item => {
      if (item.insuranceType === '甲类') {
        drugList.value.AType.push(item);
        allMoney.ADrug += item.drugPrice;
      } else if (item.insuranceType === '乙类') {
        drugList.value.BType.push(item);
        allMoney.BDrug += item.drugPrice;
      } else if (item.insuranceType === '丙类') {
        drugList.value.CType.push(item);
        allMoney.CDrug += item.drugPrice;
      }
    });
  });

  // 加载诊疗项目费用
  api.inpatientDiagnosis.getByPatientId(pid).then(response => {
    const diagnosisData = response.data.data;
    diagnosisData.forEach(item => {
      otherList.value.push(item);
      allMoney.other += item.medicalPrice;
    });

    // 加载医疗服务费用
    api.inpatientMedical.getByPatientId(pid).then(response => {
      const medicalData = response.data.data;
      medicalData.forEach(item => {
        otherList.value.push(item);
        allMoney.other += item.medicalPrice;
      });
    });
  });
};

// 获取医院报销阶梯比例（三级医院）
const getHospitalLevelReimbursement = () => {
  api.hospitalReimbursement.getByLevel('三级医院').then(response => {
    // 根据参保人员工作状态筛选对应报销阶梯（在职/退休）
    const workType = patientEntity.value.workStatus === '在职' ? 1 : 0;
    response.data.forEach(item => {
      if (item.peopleType === workType) {
        tableDataList02.push(item);
      }
    });
  });
};

// 确认报销操作
const open = () => {
  ElMessageBox.confirm(
    `报销金额总计为：${allMoney.allPay}元`,
    '确认报销',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    ElMessage({
      type: 'success',
      message: '报销成功',
    });
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消报销',
    });
  });
};

// 页面初始化
onMounted(() => {
  patientId.value = useRoute().params.id;
  getPatientById();
  getDrugReimbursement();
  initPatient();
  getHospitalLevelReimbursement();
});
</script>

<style scoped>
/* 页面整体样式 */
.reimbursement-container {
  padding: 20px;
  background-color: #f5f7fa;
}

/* 页面头部样式 */
.page-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

.page-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 患者信息样式 */
.patient-info {
  margin-bottom: 30px;
  align-items: center; /* 垂直居中对齐 */
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 0; /* 移除底部间距 */
}

.info-label {
  font-size: 16px;
  color: #666;
  width: 80px;
  margin-right: 10px;
  text-align: right; /* 标签右对齐 */
}

.info-value {
  font-size: 16px;
  color: #333;
  padding: 4px 8px;
  border-radius: 4px;
  min-width: 80px; /* 确保标签有最小宽度 */
}

.total-money {
  background-color: #1890FF;
  color: #fff;
  font-weight: bold;
}

/* 药品卡片样式 */
.drug-cards {
  margin-bottom: 30px;
}

.drug-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 100%; /* 确保卡片高度一致 */
  transition: transform 0.2s;
}

.drug-card:hover {
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: 500;
  padding-bottom: 10px;
  margin-bottom: 20px;
  justify-content: center; /* 水平居中 */
  align-items: center;
}


.card-icon {
  margin-right: 8px;
  color: #1890FF;
  font-size: 18px;
}

.reimbursement-tag,
.cost-tag {
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 4px;
  white-space: nowrap; /* 防止标签换行 */
}

.reimbursement-tag {
  background-color: #52c41a;
  color: #fff;
}

.cost-tag {
  background-color: #faad14;
  color: #fff;
}

/* 表格样式优化 */
.el-table {
  width: 100%;
  border-radius: 4px;
}

.el-table th,
.el-table td {
  text-align: center;
  padding: 8px 0;
}

/* 报销计算规则卡片 */
.reimbursement-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.reimbursement-card .card-header {
  padding: 16px;
  border-bottom: 1px solid #ebedf0;
}

/* 报销结果区域 */
.reimbursement-footer {
  margin-top: 20px;
  align-items: center;
}

.reimbursement-footer h3 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.total-reimbursement-tag {
  font-size: 24px;
  background-color: #1890FF;
  color: #fff;
  padding: 6px 16px;
  border-radius: 4px;
}

/* 按钮样式优化 */
.el-button {
  border-radius: 4px;
  transition: all 0.2s;
}

.el-button:hover {
  transform: translateY(-1px);
}

/* 滚动条美化 */
.el-scrollbar__wrap {
  overflow-x: hidden;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  
 


  
  .info-item {
    margin-bottom: 10px;
  }
}
</style>