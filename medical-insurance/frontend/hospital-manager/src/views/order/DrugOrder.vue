<template>
  <div class="drug-order-container">
    <!-- 患者信息头部 -->
    <div class="patient-header">
      <el-icon class="header-icon"><DocumentChecked /></el-icon>
      <span class="patient-title">医嘱患者：{{orderPatient.realName || '未选择患者'}}</span>
    </div>
    
    <el-divider />
    
    <!-- 当前药品信息 -->
    <div class="current-drug" v-if="showDrug">
      <span class="drug-label">当前药品处方医嘱：</span>
      <el-tag type="success" class="drug-name">{{ showDrug }}</el-tag>
    </div>
    
    <el-divider />
    
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-input 
        v-model="inputName" 
        placeholder="请输入药品名称搜索" 
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
    
    <!-- 药品列表 -->
    <div class="table-wrapper">
      <el-table 
        :data="dataList" 
        stripe 
        border
        v-loading="loading"
        element-loading-text="药品数据加载中..."
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="chinaName" label="药品名称" width="200" />
        <el-table-column prop="specifications" label="规格" width="200" />
        <el-table-column prop="drugManufacturer" label="生产企业" min-width="200" />
        <el-table-column prop="drugPrice" label="价格" width="120" align="right">
          <template #default="scope">
            {{ formatPrice(scope.row.drugPrice) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small"
              @click="addDrug(scope.row)"
              class="action-btn"
            >
              添加
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
import { useRouter } from "vue-router";
import { getDrugListPage } from '@/api/drug';
import { ElMessage } from 'element-plus'

const router = useRouter();
const orderPatient = ref({});
const showDrug = ref('');
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

// 搜索药品
const searchName = () => {
  initData(1, pageSize.value, inputName.value);
}

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
  getDrugListPage({
    pageNumber: currentPage,
    pageSize: currentSize,
    drugName: searchKey
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

// 分页
const pageChangeFunc = (currentPage) => {
  initData(currentPage, pageSize.value, inputName.value);
}

// 添加药品
const addDrug = (row) => {
  router.push({ 
    path: "/drugorderadd", 
    query: { 
      id: row.id, 
      chinaName: row.chinaName 
    }
  });
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
.drug-order-container {
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.patient-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.header-icon {
  font-size: 28px;
  color: #409eff;
  margin-right: 12px;
}

.patient-title {
  font-size: 22px;
  font-weight: 600;
  color: #1c2e4a;
}

.current-drug {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 16px 0;
}

.drug-label {
  font-weight: 500;
  color: #606266;
}

.drug-name {
  font-size: 16px;
  padding: 8px 12px;
}

.search-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin: 16px 0;
}

.search-input {
  flex: 1;
  max-width: 400px;
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
    max-width: 100%;
  }
  
  .search-btn {
    width: 100%;
  }
}
</style>