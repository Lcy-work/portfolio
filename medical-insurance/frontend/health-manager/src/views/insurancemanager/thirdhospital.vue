<template>
  <div class="page-container">
    <el-row :gutter="20" class="main-content">
      <el-col :span="14">
        <el-card class="box-card table-card">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><SetUp /></el-icon>
              <span class="header-title">三级医院 费用阶梯 （元）</span>
            </div>
          </template>
          <div class="table-content">
            <!-- 功能按钮 -->
            <el-row class="btn-row">
              <el-col :span="4">
                <el-button 
                  type="primary" 
                  :icon="Plus" 
                  @click="addView"
                  class="add-btn"
                >
                  新增
                </el-button>
              </el-col>
            </el-row>
            
            <el-table 
              stripe 
              :data="tableDataList" 
              highlight-current-row 
              border 
              @row-click="handleRowClick"
              class="custom-table"
              :row-style="{ height: '60px' }"
              :header-cell-style="{ 
                'background-color': '#f5f7fa', 
                'font-weight': 'bold',
                'color': '#333'
              }"
            >
              <!-- 表格列定义 -->
              <el-table-column label="起付线" prop="minPayLevel" align="center" />
              <el-table-column label="等级线" prop="maxPayLevel" align="center" />
              <el-table-column label="人员类别" width="120" align="center">
                <template #default="scope">
                  <el-tag 
                    v-if="scope.row.peopleType===1" 
                    effect="plain" 
                    type="info"
                    class="tag-style"
                  >
                    在职人员
                  </el-tag>
                  <el-tag 
                    v-else 
                    type="success"
                    class="tag-style"
                  >
                    退休人员
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="报销比例" prop="payProportion" width="120" align="center">
                <template #default="scope">
                  <el-tag effect="dark" type="info" class="proportion-tag">
                    {{ scope.row.payProportion }} %
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="状态" width="100" align="center">
                <template #default="scope">
                  <el-tag 
                    v-if="scope.row.status===1" 
                    type="success"
                    class="status-tag"
                  >
                    启用
                  </el-tag>
                  <el-tag 
                    v-else 
                    type="info"
                    class="status-tag"
                  >
                    禁用
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="100">
                <template #default="scope">
                  <el-button 
                    type="danger" 
                    :icon="Delete" 
                    circle 
                    plain 
                    @click.stop="handleDelete(scope.row)"
                    class="delete-btn"
                    size="small"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="10">
        <!-- 右侧表单 -->
        <el-card class="box-card form-card">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><Operation /></el-icon>
              <span class="header-title">详细信息</span>
              <el-tag 
                type="success" 
                v-if="formData.minPayLevel" 
                size="small"
                class="range-tag"
              >
                {{ formData.minPayLevel }} 至 {{ formData.maxPayLevel }} 区间报销比例
              </el-tag>
              <el-tag 
                type="warning" 
                v-else 
                size="small"
                class="range-tag"
              >
                请点击左侧列表选择
              </el-tag>
            </div>
          </template>
          
          <el-form 
            ref="dataFormRef" 
            :model="formData" 
            :rules="rules" 
            label-width="80px"
            class="detail-form"
          >
            <el-form-item label="起付线" prop="minPayLevel">
              <el-input 
                v-model="formData.minPayLevel" 
                placeholder="人民币 元"
                class="form-input"
              />
            </el-form-item>
            <el-form-item label="等级线" prop="maxPayLevel">
              <el-input 
                v-model="formData.maxPayLevel" 
                placeholder="人民币 元"
                class="form-input"
              />
            </el-form-item>
            <el-form-item label="人员类别" prop="peopleType">
              <el-radio-group v-model="formData.peopleType" class="radio-group">
                <el-radio :label="1" class="radio-item">在职人员</el-radio>
                <el-radio :label="0" class="radio-item">退休人员</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="报销比例" prop="payProportion">
              <el-input-number 
                v-model="formData.payProportion" 
                :precision="0" 
                :step="1" 
                :max="100" 
                :min="0" 
                placeholder="0"
                class="number-input"
                style="width: 100%;"
              />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status" class="radio-group">
                <el-radio :label="1" class="radio-item">正常</el-radio>
                <el-radio :label="0" class="radio-item">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          
          <div class="form-actions">
            <el-button 
              type="primary" 
              @click="submitForm"
              class="submit-btn"
            >
              修改
            </el-button>
            <el-button 
              class="reset-btn"
              @click="resetForm"
            >
              重置
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { Delete, Plus, SetUp, Operation } from "@element-plus/icons-vue";
import { ElForm, ElMessage, ElMessageBox } from "element-plus";
// 引入三级医院专属API
import thirdHospitalApi from "@/api/thirdHospitalReimbursement";

// 响应式数据定义
const formData = ref({}); // 详情表单数据
const addData = ref({
  hospitalLevel: "三级医院" // 固定医院等级为三级
});
const dataFormRef = ref(ElForm); // 表单引用
const rules = ref({
  minPayLevel: [{ required: true, message: "起付线不能为空", trigger: "blur" }],
  maxPayLevel: [{ required: true, message: "等级线不能为空", trigger: "blur" }],
  peopleType: [{ required: true, message: "人员类别不能为空", trigger: "blur" }],
  payProportion: [{ required: true, message: "报销比例不能为空", trigger: "blur" }],
  status: [{ required: true, message: "状态不能为空", trigger: "blur" }],
});
const tableDataList = ref([]); // 表格数据列表

// 初始化三级医院数据列表
const initData = () => {
  thirdHospitalApi.getThirdHospitalList().then(
    (response) => {
      tableDataList.value = response.data;
      console.log("三级医院数据列表：", tableDataList.value);
    }
  );
};

// 点击表格行加载详情
const handleRowClick = (row) => {
  thirdHospitalApi.getThirdHospitalById(row.id).then(
    (response) => {
      formData.value = { ...response.data }; // 深拷贝避免直接修改原数据
    }
  );
};

// 提交修改表单
const submitForm = () => {
  dataFormRef.value.validate((isValid) => {
    if (isValid && formData.value.id) {
      thirdHospitalApi.updateThirdHospitalById(formData.value).then(
        (response) => {
          ElMessage.success(response.data);
          initData(); // 刷新列表
        }
      );
    }
  });
};

// 重置表单
const resetForm = () => {
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }
};

// 新增数据
const addView = () => {
  // 清空表单数据
  formData.value = {};
  // 调用新增API
  thirdHospitalApi.addThirdHospital(addData.value).then(
    (response) => {
      ElMessage.success(response.data);
      initData(); // 刷新列表
    }
  );
};

// 删除数据
const handleDelete = (row) => {
  ElMessageBox.confirm(
    "是否要删除该费用阶梯？",
    "删除操作",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning"
    }
  ).then(() => {
    thirdHospitalApi.deleteThirdHospitalById(row.id).then(
      (response) => {
        ElMessage.success(response.data);
        initData(); // 刷新列表
        // 清除选中状态
        formData.value = {};
      }
    );
  });
};

// 页面挂载时初始化数据
onMounted(() => {
  initData();
});
</script>

<style scoped>
.page-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.main-content {
  margin-bottom: 20px;
}

.box-card {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border-radius: 6px;
}

.box-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 10px;
}

.header-icon {
  color: #fa541c; /* 三级医院使用橙色系作为主题色 */
  font-size: 18px;
}

.header-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.table-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.table-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.btn-row {
  margin-bottom: 15px;
  padding: 5px 0;
}

.add-btn {
  transition: all 0.2s ease;
  background-color: #fa541c;
  border-color: #fa541c;
}

.add-btn:hover {
  background-color: #e64e18;
  border-color: #e64e18;
  transform: translateY(-2px);
}

.custom-table {
  flex: 1;
  width: 100%;
  border-radius: 4px;
  overflow: hidden;
}

.tag-style {
  padding: 4px 8px;
  border-radius: 4px;
}

.proportion-tag {
  background-color: #fff2e8;
  color: #d44800;
  border-color: #ffbb96;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
}

.delete-btn {
  transition: all 0.2s ease;
}

.delete-btn:hover {
  background-color: rgba(237, 100, 166, 0.1);
  transform: scale(1.1);
}

.form-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.detail-form {
  padding: 10px 0;
  margin-bottom: 20px;
}

.form-input {
  width: 100%;
  transition: border-color 0.2s ease;
}

.form-input:focus {
  border-color: #fa541c;
  box-shadow: 0 0 0 2px rgba(250, 84, 28, 0.2);
}

.radio-group {
  display: flex;
  gap: 20px;
  margin-top: 5px;
}

.radio-item {
  cursor: pointer;
}

.range-tag {
  margin-left: auto;
  font-size: 12px;
}

.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 10px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.submit-btn {
  transition: all 0.2s ease;
  background-color: #fa541c;
  border-color: #fa541c;
}

.submit-btn:hover {
  background-color: #e64e18;
  border-color: #e64e18;
  transform: translateY(-2px);
}

.reset-btn {
  transition: all 0.2s ease;
}

.reset-btn:hover {
  background-color: #f5f5f5;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .el-col {
    width: 100% !important;
    margin-bottom: 20px;
  }
}
</style>