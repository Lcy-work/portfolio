<template>
  <div class="patient-registration-container">
    <!-- 头部信息 -->
    <div class="header-section">
      <el-icon class="header-icon"><DocumentChecked /></el-icon>
      <h2 class="header-title">入院登记</h2>
    </div>
    
    <el-divider />
    
    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button 
        type="primary" 
        @click="addNewRegist"
        icon="CircleCheck"
        class="registration-btn"
      >
        登记
      </el-button>
      <el-button 
        @click="clearRegist"
        icon="Refresh"
        class="registration-btn"
      >
        清空
      </el-button>
    </div>
    
    <el-divider />
    
    <el-form ref="dataFormRef" :model="register" label-position="top">
      <!-- 基本信息卡片 -->
      <div class="form-card">
        <div class="detail-header">
          <el-icon><User /></el-icon>
          <span>基本信息</span>
        </div>
        
        <el-descriptions border :column="2">
          <el-descriptions-item label="住院号">
            <el-form-item 
              prop="caseNumber" 
              :rules="[
                {required:true,message:'请输入住院号'},
                {type:'number',message:'住院号必须为数字'}
              ]"
            >
              <el-input v-model.number="register.caseNumber" placeholder="请输入住院号"/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="姓名">
            <el-form-item 
              prop="realName" 
              :rules="[{required:true,message:'请输入姓名'}]"
            >
              <el-input v-model="register.realName" placeholder="请输入姓名"/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="身份证号">
            <el-form-item 
              prop="cardNumber" 
              :rules="[{required:true,message:'请输入身份证号'}]"
            >
              <el-input v-model="register.cardNumber" placeholder="请输入身份证号"/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="年龄">
            <el-form-item 
              prop="age" 
              :rules="[
                {required:true,message:'请输入年龄'},
                {type:'number',message:'年龄必须为数字'}
              ]"
            >
              <div class="age-container">
                <el-input-number 
                  v-model.number="register.age" 
                  :min="0" 
                  :max="120" 
                  controls-position="right"
                />
                <el-select v-model="register.ageType" class="small-select">
                  <el-option value="岁" label="岁"></el-option>
                  <el-option value="月" label="月"></el-option>
                  <el-option value="日" label="日"></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="出生日期">
            <el-form-item 
              prop="birthday" 
              :rules="[{required:true,message:'请选择出生日期'}]"
            >
              <el-date-picker 
                v-model="register.birthday" 
                type="date" 
                format="YYYY-MM-DD" 
                value-format="YYYY-MM-DD" 
                placeholder="选择出生日期"
                class="full-width"
              />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            <el-select v-model="register.gender" class="small-select">
              <el-option value="男" label="男"></el-option>
              <el-option value="女" label="女"></el-option>
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="家庭住址" :span="2">
            <el-input 
              v-model="register.homeAddress" 
              placeholder="请输入家庭住址（可选）"
            />
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <!-- 登记信息卡片 -->
      <div class="form-card">
        <div class="detail-header">
          <el-icon><Document /></el-icon>
          <span>登记信息</span>
        </div>
        
        <el-descriptions border :column="2">
          <el-descriptions-item label="结算类别">
            <el-select v-model="register.registMethod" class="small-select">
              <el-option value="自费" label="自费"></el-option>
              <el-option value="医保" label="医保"></el-option>
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="工作状态">
            <el-select v-model="register.workStatus" class="small-select">
              <el-option value="在职" label="在职"></el-option>
              <el-option value="退休" label="退休"></el-option>
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="入院时间">
            <el-form-item 
              prop="visitDate" 
              :rules="[{required:true,message:'请选择入院时间'}]"
            >
              <el-date-picker
                v-model="register.visitDate"
                type="datetime"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择入院时间"
                class="full-width"
              />
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { ElMessage } from "element-plus";
import { addPatientRegistration } from '@/api/patient';

const dataFormRef = ref(null);
const register = reactive({
  id: "",
  caseNumber: "",
  realName: "",
  gender: "男",
  cardNumber: "",
  birthday: "",
  age: "",
  ageType: "岁",
  homeAddress: "",
  visitDate: "",
  workStatus: "在职",
  registMethod: "医保"
});

// 添加新登记
const addNewRegist = async () => {
  try {
    const valid = await dataFormRef.value.validate();
    if (valid) {
      const response = await addPatientRegistration(register);
      ElMessage.success(response.data || "登记成功");
      clearRegist();
    }
  } catch (error) {
    console.error("登记失败:", error);
    ElMessage.error(error.response?.data?.message || "登记失败，请检查输入");
  }
};

// 清空表单
const clearRegist = () => {
  Object.assign(register, {
    id: "",
    caseNumber: "",
    realName: "",
    gender: "男",
    cardNumber: "",
    birthday: "",
    age: "",
    ageType: "岁",
    homeAddress: "",
    visitDate: "",
    workStatus: "在职",
    registMethod: "医保"
  });
  dataFormRef.value?.resetFields();
};
</script>

<style scoped>
.patient-registration-container {
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  max-width: 1200px;
  margin: 0 auto;
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

.registration-btn {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  transition: all 0.3s;
}

.registration-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.form-card {
  margin-bottom: 24px;
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
  width: 120px;
}

.full-width {
  width: 100%;
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
  
  .registration-btn {
    width: 100%;
  }
  
  :deep(.el-descriptions-item) {
    display: flex;
    flex-direction: column;
  }
  
  :deep(.el-descriptions-item__content) {
    display: flex;
    justify-content: flex-start;
  }
}
</style>