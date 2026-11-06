<template>
  <div class="diagnosis-treatment-container">
    <!-- 患者信息头部 -->
    <div class="patient-header">
      <el-icon class="header-icon"><DocumentChecked /></el-icon>
      <span class="patient-title">医嘱患者：{{orderPatient.realName || '未选择患者'}}</span>
    </div>
    
    <el-divider />
    
    <!-- 标题区域 -->
    <div class="section-title">
      <h3>诊疗项目医嘱</h3>
    </div>
    
    <el-divider />
    
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-input 
        v-model="inputName" 
        placeholder="请输入诊疗项目名称" 
        clearable
        @keyup.enter="searchName"
        class="search-input"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="searchName" icon="Search" class="search-btn">搜索</el-button>
    </div>
    
    <!-- 诊疗项目列表 -->
    <div class="table-wrapper">
      <el-table 
        :data="dataList" 
        stripe 
        border
        v-loading="loading"
        element-loading-text="诊疗项目加载中..."
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="medicalName" label="诊疗项目名称" width="180" />
        <el-table-column prop="medicalNumber" label="项目编码" width="150" />
        <el-table-column prop="countryNumber" label="国家编码" width="300" />
        <el-table-column prop="midicalInfo" label="项目说明" min-width="200" />
        <el-table-column prop="medicalExclude" label="除外内容" width="200" />
        <el-table-column prop="medicalUnit" label="计价单位" width="100" align="center" />
        <el-table-column prop="medicalPrice" label="价格" width="100" align="center">
          <template #default="scope">
            {{ formatPrice(scope.row.medicalPrice) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small"
              @click="addDiagnosis(scope.row)"
              class="action-btn"
            >
              增加
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 分页器 -->
    <div class="pagination-wrapper">
      <el-pagination 
        background 
        layout="prev, pager, next" 
        :total="total" 
        :page-size="pageSize"
        v-model:currentPage="pageNumberNow" 
        @current-change="pageChangeFunc"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { Search, DocumentChecked } from "@element-plus/icons-vue";
import router from "@/router"
import { getDiagnosisTreatmentList } from '@/api/diagnosis';
import DiagnosisTreatmentOrderAdd from "./DiagnosisTreatmentOrderAdd.vue";
import { ElMessage } from 'element-plus'

const orderPatient = ref({});
const inputName = ref('');
const dataList = ref([]);
const total = ref(0);
const pageNumberNow = ref(1);
const pageSize = ref(10);
const loading = ref(false);

// 格式化价格显示
const formatPrice = (price) => {
  return `¥${parseFloat(price).toFixed(2)}`;
};

// 初始化数据
const initData = (pageNumber, pageSize, searchName) => {
  // 参数校验（确保类型正确）
  const currentPage = typeof pageNumber === "number" && pageNumber > 0 
    ? pageNumber 
    : 1;
  const currentSize = typeof pageSize === "number" && pageSize > 0 
    ? pageSize 
    : 10;
  const searchKey = typeof searchName === "string" ? searchName : "";

  // 更新分页组件的当前页码
  pageNumberNow.value = currentPage;

  // 调用接口，传递校验后的参数
  getDiagnosisTreatmentList({
    pageNumber: currentPage,
    pageSize: currentSize,
    DiagnosisTreatmentName: searchKey
  }).then((response) => {
    const result = response.data;
    console.log("后端响应数据：", result);

    if (!result) {
      ElMessage.error("后端返回数据格式错误");
      return;
    }
    if (result.code === 200 || result.code === "200") {
      // 绑定表格数据（修复赋值错误）
      dataList.value = Array.isArray(result.data.records) ? result.data.records : [];
      // 绑定总记录数
      total.value = typeof result.data.total === "number" ? result.data.total : 0;
      ElMessage.success(`加载成功，共${total.value}条数据`);
    } else {
      ElMessage.error(`获取数据失败：${result.msg || '未知错误'}`);
    }
  }).catch((error) => {
    console.error("请求分页接口失败：", error);
    ElMessage.error("网络错误，无法获取数据");
  });
};

// 搜索
const searchName = () => {
  initData(1, pageSize.value, inputName.value);
}

// 添加诊疗项目
const addDiagnosis = (row) => {
  router.push({ 
    path: "/diagnosistreatmentorderadd", 
    query: { 
      id: row.id, 
      medicalName: row.medicalName 
    } 
  });
}

// 分页变化
const pageChangeFunc = (currentPage) => {
  initData(currentPage, pageSize.value, inputName.value);
}

// 初始化页面
onMounted(() => {
  const patient = sessionStorage.getItem("orderPatient");
  if (patient) {
    orderPatient.value = JSON.parse(patient);
  }
  initData(1, pageSize.value, '');
});
</script>

<style scoped>
.diagnosis-treatment-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.patient-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.header-icon {
  font-size: 24px;
  color: #409eff;
  margin-right: 12px;
}

.patient-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.section-title {
  margin: 16px 0;
}

.section-title h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.search-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin: 16px 0;
}

.search-input {
  width: 300px;
}

.table-wrapper {
  margin: 20px 0;
  border-radius: 8px;
  overflow: hidden;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.action-btn {
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 4px;
}

:deep(.el-divider) {
  margin: 16px 0;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input {
    width: 100%;
  }
  
  .search-btn {
    width: 100%;
  }
}
</style>