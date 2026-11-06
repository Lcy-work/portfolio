<template>
  <div class="page-container">
    <el-row :gutter="24" class="main-content">
      <!-- 左侧表格区域 -->
      <el-col :span="10">
        <el-card class="data-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><SetUp /></el-icon>
              <span class="header-title">药品保险报销等级列表</span>
            </div>
          </template>
          
          <div class="table-container">
            <!-- 功能按钮 -->
            <el-row class="btn-row">
              <el-col :span="4">
                <el-button 
                  type="primary" 
                  :icon="Plus" 
                  @click="addView"
                  class="operation-btn"
                >
                  新增
                </el-button>
              </el-col>
            </el-row>
            
            <!-- 数据表格 -->
            <el-table 
              :data="tableDataList" 
              highlight-current-row 
              border 
              @row-click="handleRowClick"
              class="custom-table"
              :row-style="{ height: '60px' }"
              :header-cell-style="tableHeaderStyle"
              stripe
            >
              <el-table-column 
                label="等级" 
                prop="drugReimbursementType" 
                width="120" 
                align="center"
              />
              <el-table-column 
                label="报销比例" 
                prop="drugReimbursementProportion" 
                align="center"
                formatter="formatPercentage"
              >
                <template #default="scope">
                  <span>{{ scope.row.drugReimbursementProportion }}%</span>
                </template>
              </el-table-column>
              <el-table-column 
                label="状态" 
                width="100" 
                align="center"
              >
                <template #default="scope">
                  <el-tag 
                    v-if="scope.row.status === 1" 
                    type="success"
                    class="status-tag active"
                  >启用</el-tag>
                  <el-tag 
                    v-else 
                    type="info"
                    class="status-tag inactive"
                  >禁用</el-tag>
                </template>
              </el-table-column>
              <el-table-column 
                label="操作" 
                align="center" 
                width="120"
              >
                <template #default="scope">
                  <el-button 
                    type="danger" 
                    :icon="Delete" 
                    circle 
                    plain
                    @click.stop="handleDelete(scope.row)"
                    class="delete-btn"
                    size="small"
                    :title="`删除 ${scope.row.drugReimbursementType}`"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧详情区域 -->
      <el-col :span="14">
        <el-card class="data-card detail-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><Operation /></el-icon>
              <span class="header-title">详细信息</span>
              <el-tag 
                type="success" 
                v-if="formData.drugReimbursementType" 
                size="small"
                class="type-tag"
              >{{ formData.drugReimbursementType }}</el-tag>
              <el-tag 
                type="warning" 
                v-else 
                size="small"
                class="type-tag"
              >请点击左侧列表选择</el-tag>
            </div>
          </template>
          
          <!-- 数据详情表单 -->
          <el-form 
            label-width="100px" 
            :model="formData" 
            ref="dataFormRef" 
            :rules="rules"
            class="detail-form"
          >
            <el-form-item label="等级名称" prop="drugReimbursementType" class="form-item">
              <el-input 
                v-model="formData.drugReimbursementType" 
                placeholder="请输入药品等级名称"
                class="form-input"
              />
            </el-form-item>
            <el-form-item label="报销比例" prop="drugReimbursementProportion" class="form-item">
              <el-input-number 
                v-model="formData.drugReimbursementProportion" 
                :precision="0" 
                :step="1" 
                :max="100" 
                :min="0" 
                placeholder="0"
                class="form-input"
                style="width: 100%;"
                suffix="%"
              />
            </el-form-item>
            <el-form-item label="状态" class="form-item">
              <el-radio-group v-model="formData.status" class="status-radio">
                <el-radio :label="1" class="radio-option">
                  <span class="radio-text">正常</span>
                </el-radio>
                <el-radio :label="0" class="radio-option">
                  <span class="radio-text">停用</span>
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="等级说明" class="form-item">
              <el-input 
                v-model="formData.drugReimbursementInfo" 
                type="textarea" 
                placeholder="请输入该药品等级的报销说明、适用范围等信息" 
                :autosize="{ minRows:3,maxRows:5}"
                class="form-input"
              />
            </el-form-item>
          </el-form>
          
          <!-- 操作按钮 -->
          <div class="form-actions">
            <el-button 
              type="primary" 
              @click="submitForm"
              class="submit-btn"
              :disabled="!formData.id"
            >
              修改
            </el-button>
            <el-button class="reset-btn" @click="resetForm">重置</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
// 原有脚本逻辑保持不变，新增重置表单功能
import { Search, Plus, Delete, SetUp, Operation } from "@element-plus/icons-vue";
import { ref, inject, onMounted } from "vue";
import { ElMessage, ElForm, ElMessageBox } from "element-plus";
const axios = inject("axios");
axios.defaults.baseURL = "http://127.0.0.1:8081/drugreimbursement"
import drugReimbursementApi from "@/api/drugReimbursement";

const tableDataList = ref([]);
const formData = ref({});
const addData = ref({})
const dataFormRef = ref(ElForm);
const rules = ref({
  drugReimbursementType: [{ required: true, message: '请输入等级名称', trigger: "blur" }],
  drugReimbursementProportion: [{ required: true, message: '请输入报销比例', trigger: "blur" }],
})

// 表格头部样式
const tableHeaderStyle = {
  background: '#f5f7fa',
  fontWeight: '550',
  color: '#333',
  padding: '12px 0'
}

// 初始化数据
const initData = () => {
  drugReimbursementApi.getAllList().then(
    (response) => {
      tableDataList.value = response.data;
    }
  );
};

// 行点击事件
const handleRowClick = (row) => {
  drugReimbursementApi.getById(row.id).then(
    (response) => {
      formData.value = { ...response.data }; // 深拷贝避免直接修改
    }
  );
};

// 提交修改
const submitForm = () => {
  dataFormRef.value.validate((isValid) => {
    if (isValid) {
      if (formData.value.id) {
        drugReimbursementApi.updateById(formData.value).then(
          (response) => {
            ElMessage.success(response.data);
            initData();
          }
        );
      }
    }
  });
};

// 重置表单
const resetForm = () => {
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }
};

// 新增操作
const addView = () => {
  // 清空表单用于新增
  formData.value = {};
  drugReimbursementApi.add(addData.value).then(
    (response) => {
      ElMessage.success(response.data);
      initData();
    }
  );
};

// 删除操作
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除【${row.drugReimbursementType}】吗？`,
    '删除确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    drugReimbursementApi.deleteById(row.id).then(
      (response) => {
        ElMessage.success(response.data);
        initData();
        // 清除选中状态
        if (formData.value.id === row.id) {
          formData.value = {};
        }
      }
    );
  });
};

onMounted(() => {
  initData();
});
</script>

<style scoped>
.page-container {
  padding: 24px;
  background-color: #f2f3f5;
  min-height: 100vh;
}

.main-content {
  margin-bottom: 20px;
}

.data-card {
  border-radius: 8px;
  transition: all 0.3s ease;
  overflow: hidden;
  min-height: 580px;
  border: none;
}

.data-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fff;
}

.header-icon {
  color: #1677ff;
  margin-right: 8px;
  font-size: 19px;
}

.header-title {
  font-size: 16px;
  font-weight: 550;
  color: #1f2329;
}

/* 表格区域样式 */
.table-container {
  padding: 20px;
  background-color: #fff;
}

.btn-row {
  margin-bottom: 18px;
}

.operation-btn {
  width: 100%;
  transition: all 0.25s ease;
  background-color: #1677ff;
  border-color: #1677ff;
}

.operation-btn:hover {
  background-color: #0f62d9;
  border-color: #0f62d9;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(22, 119, 255, 0.3);
}

.custom-table {
  width: 100%;
  font-size: 14px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #e5e7eb;
}

.custom-table :deep() .el-table__row {
  transition: background-color 0.2s ease;
}

.custom-table :deep() .el-table__row:hover > td {
  background-color: #f9fafb !important;
}

.custom-table :deep() .el-table__current-row > td {
  background-color: #e6f7ff !important;
}

.status-tag {
  padding: 3px 10px;
  border-radius: 4px;
  font-size: 12px;
  transition: all 0.2s ease;
}

.status-tag.active {
  background-color: #f0f9eb;
  color: #52c41a;
  border-color: #b7eb8f;
}

.status-tag.inactive {
  background-color: #f5f5f5;
  color: #8c8c8c;
  border-color: #d9d9d9;
}

.delete-btn {
  transition: all 0.2s;
  color: #ff4d4f;
  border-color: #ff4d4f;
}

.delete-btn:hover {
  background-color: rgba(255, 77, 79, 0.1);
  transform: scale(1.1);
}

/* 详情区域样式 */
.detail-card {
  min-height: 580px;
  background-color: #fff;
}

.detail-form {
  padding: 25px 0 10px 0;
  background-color: #fff;
}

.form-item {
  margin-bottom: 22px;
}

.form-input {
  width: 100%;
  max-width: 550px;
  border-radius: 4px;
  transition: all 0.2s ease;
  border-color: #d9d9d9;
}

.form-input:focus {
  border-color: #1677ff;
  box-shadow: 0 0 0 2px rgba(22, 119, 255, 0.2);
}

.status-radio {
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 25px;
}

.radio-option {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.radio-text {
  margin-left: 6px;
  color: #333;
}

.type-tag {
  margin-left: 12px;
  padding: 2px 10px;
  font-size: 12px;
}

.form-actions {
  margin-top: 30px;
  padding-top: 18px;
  border-top: 1px dashed #e5e7eb;
  display: flex;
  align-items: center;
  gap: 15px;
}

.submit-btn {
  padding: 8px 20px;
  background-color: #1677ff;
  border-color: #1677ff;
  transition: all 0.2s ease;
}

.submit-btn:hover {
  background-color: #0f62d9;
  border-color: #0f62d9;
  box-shadow: 0 2px 8px rgba(22, 119, 255, 0.3);
}

.submit-btn:disabled {
  background-color: #8cc5ff;
  border-color: #8cc5ff;
  cursor: not-allowed;
  box-shadow: none;
}

.reset-btn {
  padding: 8px 20px;
  background-color: #f5f5f5;
  color: #666;
  transition: all 0.2s ease;
}

.reset-btn:hover {
  background-color: #e9e9e9;
  color: #333;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .el-col {
    width: 100% !important;
    margin-bottom: 24px;
  }
  
  .form-input {
    max-width: 100%;
  }
}
</style>