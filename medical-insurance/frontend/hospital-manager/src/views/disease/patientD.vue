<template>
  <div class="patient-diagnosis-container">
    <!-- 头部信息 -->
    <div class="header-section">
      <el-icon class="header-icon"><DocumentChecked /></el-icon>
      <h2 class="header-title">诊断患者选择: {{register.realName || '未选择患者'}}</h2>
    </div>
    
    <el-divider />
    
    <!-- 诊断操作按钮 -->
    <div class="action-buttons">
      <el-button 
        type="primary" 
        :disabled="selectedPatient" 
        @click="toAdmission"
        icon="CircleCheck"
        class="diagnosis-btn"
      >
        增加入院诊断
      </el-button>
      <el-button 
        type="primary" 
        :disabled="selectedPatient" 
        @click="toMain"
        icon="Star"
        class="diagnosis-btn"
      >
        增加主要诊断
      </el-button>
      <el-button 
        type="primary" 
        :disabled="selectedPatient" 
        @click="toOther"
        icon="DocumentAdd"
        class="diagnosis-btn"
      >
        增加其他诊断
      </el-button>
    </div>
    
    <el-divider />
    
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-input 
        v-model="inputName" 
        placeholder="请输入患者姓名搜索" 
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
    
    <!-- 患者列表 -->
    <div class="table-wrapper">
      <el-table 
        :data="tableDataList" 
        border 
        stripe
        highlight-current-row
        @row-click="selectRow"
        v-loading="loading"
        element-loading-text="患者数据加载中..."
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="caseNumber" label="住院号" width="150" align="center" />
        <el-table-column prop="realName" label="姓名" width="120" align="center" />
        <el-table-column prop="cardNumber" label="身份证号" min-width="180" />
        <el-table-column prop="gender" label="性别" width="80" align="center" />
        <el-table-column prop="workStatus" label="工作状态" width="100" align="center" />
        <el-table-column prop="registMethod" label="结算类别" width="100" align="center" />
        <el-table-column prop="visitDate" label="入院时间" width="180" align="center" />
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
    
    <!-- 患者详细信息 -->
    <div class="patient-detail-card">
      <div class="detail-header">
        <el-icon><User /></el-icon>
        <span>患者详细信息</span>
      </div>
      
      <el-descriptions border :column="2">
        <el-descriptions-item label="住院号">{{ register.caseNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ register.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ register.cardNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="年龄">
          <div class="age-container">
            {{ register.age || '-' }}
            <el-select v-model="register.ageType" class="small-select">
              <el-option key="岁" value="岁" label="岁"></el-option>
              <el-option key="月" value="月" label="月"></el-option>
              <el-option key="日" value="日" label="日"></el-option>
            </el-select>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ register.birthday || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-select v-model="register.gender" class="small-select">
            <el-option key="男" value="男" label="男"></el-option>
            <el-option key="女" value="女" label="女"></el-option>
          </el-select>
        </el-descriptions-item>
        <el-descriptions-item label="结算类别">
          <el-select v-model="register.registMethod" class="small-select">
            <el-option key="自费" value="自费" label="自费"></el-option>
            <el-option key="医保" value="医保" label="医保"></el-option>
          </el-select>
        </el-descriptions-item>
        <el-descriptions-item label="工作状态">
          <el-select v-model="register.workStatus" class="small-select">
            <el-option key="在职" value="在职" label="在职"></el-option>
            <el-option key="退休" value="退休" label="退休"></el-option>
          </el-select>
        </el-descriptions-item>
        <el-descriptions-item label="入院时间">{{ register.visitDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="家庭住址" :span="2">{{ register.homeAddress || '-' }}</el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import router from "@/router"
import { ElMessage } from 'element-plus';
import { getPatientListPage, getPatientById } from '@/api/patient';

const tableDataList = ref([]); // 表格数据
const inputName = ref(''); // 搜索框输入值
const total = ref(0); // 总条数
const register = ref({}); // 选中的患者信息
const pageNumberNow = ref(1); // 当前页码（分页组件绑定）
const selectedPatient = ref(true); // 是否选中患者
const loading = ref(false); // 加载状态
const pageSize = ref(10); // 每页条数

// 初始化表格数据
const initPatient = (pageNumber, pageSize, searchName) => {
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
  getPatientListPage({
    pageNumber: currentPage,
    pageSize: currentSize,
    realName: searchKey
  }).then((response) => {
    const result = response.data;
    console.log("后端响应数据：", result);

    if (!result) {
      ElMessage.error("后端返回数据格式错误");
      return;
    }
    if (result.code === 200 || result.code === "200") {
      // 绑定表格数据（修复赋值错误）
      tableDataList.value = Array.isArray(result.data.records) ? result.data.records : [];
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

// 分页页码改变事件
const pageChangeFunc = (currentPage)=>{
  initPatient(currentPage, pageSize.value, inputName.value); // 修复变量名和pageSize
}

// 搜索事件
const searchName = () => {
  initPatient(1, pageSize.value, inputName.value); // 修复函数名
}

// 选择患者行
const selectRow = async (row) => {
  try {
    loading.value = true;
    const response = await getPatientById(row.id);
    register.value = response.data;
    selectedPatient.value = false; // 允许操作按钮点击
  } catch (error) {
    console.error('获取患者详情失败:', error);
    ElMessage.error('获取患者详情失败，请重试'); // 补充错误提示
  } finally {
    loading.value = false;
  }
}

// 跳转至各诊断页面
const toAdmission = () => {
  sessionStorage.setItem("diseasePatient", JSON.stringify(register.value));
  router.push("/adminssiondisease"); // 注意路径是否正确（可能是"admissiondisease"）
}

const toMain = () => {
  sessionStorage.setItem("diseasePatient", JSON.stringify(register.value));
  router.push("/maindisease");
}

const toOther = () => {
  sessionStorage.setItem("diseasePatient", JSON.stringify(register.value));
  router.push("/otherdisease");
}

// 页面挂载时初始化数据
onMounted(() => {
  initPatient(1, pageSize.value, '');
});
</script>


<style scoped>
.patient-diagnosis-container {
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.header-section {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.header-icon {
  font-size: 28px;
  color: #409eff;
  margin-right: 12px;
}

.header-title {
  font-size: 22px;
  font-weight: 600;
  color: #1c2e4a;
  margin: 0;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin: 16px 0;
}

.diagnosis-btn {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  transition: all 0.3s;
}

.diagnosis-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.search-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin: 16px 0;
}

.search-input {
  flex: 1;
}

.table-wrapper {
  margin: 20px 0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin: 16px 0;
}

.patient-detail-card {
  margin-top: 30px;
  border: 1px solid #ebeef5;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.detail-header {
  background: #f0f7ff;
  padding: 16px 20px;
  font-size: 18px;
  font-weight: 600;
  color: #1c2e4a;
  display: flex;
  align-items: center;
  gap: 10px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-descriptions) {
  padding: 20px;
}

.age-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.small-select {
  width: 80px;
}

:deep(.el-divider) {
  margin: 20px 0;
  background-color: #f0f7ff;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
    gap: 12px;
  }
  
  .diagnosis-btn {
    width: 100%;
  }
  
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>