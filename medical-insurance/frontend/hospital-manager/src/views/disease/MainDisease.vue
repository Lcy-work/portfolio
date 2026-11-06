<template>
  <div class="main-diagnosis-container">
    <!-- 患者信息头部 -->
    <div class="patient-header">
      <el-icon class="header-icon"><DocumentChecked /></el-icon>
      <span class="patient-title">主要诊断患者: {{diseasePatient.realName}}</span>
    </div>
    
    <el-divider />
    
    <!-- 当前诊断信息 -->
    <div class="current-diagnosis">
      <span class="diagnosis-label">主要诊断疾病：</span>
      <span class="diagnosis-value">{{ showDisease || '暂无诊断信息' }}</span>
    </div>
    
    <el-divider />
    
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-input 
        v-model="inputName" 
        placeholder="请输入疾病名称" 
        clearable
        @keyup.enter="searchName"
        style="width: 240px; margin-right: 12px;"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="searchName" icon="Search">搜索</el-button>
    </div>
    
    <!-- 疾病列表区域 -->
    <div class="table-wrapper">
      <el-table 
        :data="dataList" 
        stripe 
        style="width: 100%;"
        v-loading="loading"
        element-loading-text="数据加载中..."
      >
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="diseaseCode" label="疾病编码" width="200" />
        <el-table-column prop="diseaseName" label="疾病名称" />
        <el-table-column prop="diseaseICD" label="国际ICD编码" width="200" />
        <el-table-column prop="diseaseCategory" label="疾病类型" width="150" align="center">
          <template #default="scope">
            <el-tag :type="getDiseaseTagType(scope.row.diseaseCategory)" size="small">
              {{ scope.row.diseaseCategory }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small"
              @click="addDisease(scope.row)"
            >
              设为主要
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
import { ElMessageBox, ElMessage } from 'element-plus'
import { getDiseaseListPage, addMainDisease } from '@/api/disease';

// 患者信息
const diseasePatient = ref({});
const inputName = ref('');
const loading = ref(false);

// 分页相关
const total = ref(0);
const pageNumberNow = ref(1);
const pageSize = ref(10);

// 数据列表
const dataList = ref([]);
const showDisease = ref('');

// 获取疾病标签类型
const getDiseaseTagType = (category) => {
  const map = {
    '传染性疾病': 'danger',
    '慢性疾病': 'success',
    '一般疾病': 'primary'
  };
  return map[category] || '';
};

// 搜索疾病
const searchName = () => {
  initData(1, pageSize.value, inputName.value);
};

// 分页变化
const pageChangeFunc = (currentPage) => {
  initData(currentPage, pageSize.value, inputName.value);
};


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
  getDiseaseListPage({
    pageNumber: currentPage,
    pageSize: currentSize,
    diseaseName: searchKey
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

// 添加主要诊断
const addDisease = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定将"${row.diseaseName}"设为主要诊断吗？`,
      '设置主要诊断',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    const response = await addMainDisease(diseasePatient.value.id, row.id);
    showDisease.value = row.diseaseName;
    ElMessage.success(response.data);
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('设置主要诊断失败: ' + error.message);
    }
  }
};

// 初始化
onMounted(() => {
  const patient = sessionStorage.getItem("diseasePatient");
  if (patient) {
    diseasePatient.value = JSON.parse(patient);
  }
  initData(1, pageSize.value, '');
});
</script>

<style scoped>
.main-diagnosis-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.patient-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.header-icon {
  margin-right: 10px;
  font-size: 24px;
  color: #409eff;
}

.patient-title {
  font-size: 20px;
  font-weight: 500;
}

.current-diagnosis {
  padding: 12px 0;
}

.diagnosis-label {
  font-weight: 500;
  margin-right: 8px;
}

.diagnosis-value {
  color: #409eff;
  font-weight: 500;
}

.search-section {
  margin: 16px 0;
  display: flex;
  align-items: center;
}

.table-wrapper {
  margin: 20px 0;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

:deep(.el-divider) {
  margin: 16px 0;
}
</style>