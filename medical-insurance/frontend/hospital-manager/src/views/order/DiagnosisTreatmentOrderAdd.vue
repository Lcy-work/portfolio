<template>
  <div class="diagnosis-order-add-container">
    <!-- 面包屑导航和返回按钮 -->
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{path:'/diagnosistreatmentorder'}">诊疗项目医嘱</el-breadcrumb-item>
        <el-breadcrumb-item>增加诊疗项目医嘱</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button @click="back" type="primary" :icon="ArrowLeftBold" class="back-btn">返回</el-button>
    </div>
    
    <!-- 患者信息 -->
    <div class="patient-info">
      <span class="info-label">增加诊疗项目医嘱：</span>
      <el-tag type="info" class="patient-tag">{{ patientName }}</el-tag>
    </div>
    
    <!-- 表单区域 -->
    <el-card class="form-card">
      <el-form 
        :model="formData" 
        label-width="150px" 
        ref="addFormRef" 
        :rules="formRules"
        label-position="left"
      >
        <el-form-item label="诊疗项目名称" prop="medicalName">
          <el-input v-model="medicalName" disabled />
        </el-form-item>
        
        <el-form-item label="医嘱内容" prop="doctorOrder">
          <el-input 
            type="textarea" 
            placeholder="请输入医嘱内容" 
            :autosize="{minRows:2,maxRows:4}" 
            v-model="formData.doctorOrder" 
            clearable
          />
        </el-form-item>
        
        <el-form-item label="开立时间" prop="orderTime">
          <el-date-picker 
            v-model="formData.orderTime" 
            type="datetime" 
            placeholder="选择开立时间"
            value-format="YYYY-MM-DD HH:mm:ss" 
            format="YYYY-MM-DD HH:mm:ss"
            :default-value="new Date()"
          />
        </el-form-item>
        
        <el-form-item label="用法详情说明" prop="useMethod">
          <el-input 
            type="textarea" 
            placeholder="请输入用法详情说明" 
            :autosize="{minRows:2,maxRows:4}" 
            v-model="formData.useMethod"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            @click="subOrder" 
            type="primary" 
            class="submit-btn"
          >
            提交医嘱
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ArrowLeftBold } from "@element-plus/icons-vue"
import { onMounted, ref } from "vue";
import { ElForm, ElMessage } from "element-plus"
import { useRoute } from "vue-router"
import router from "@/router"
import { addDiagnosisOrder } from '@/api/diagnosis';

const patientName = ref('');
const medicalName = ref('');
const formData = ref({
  doctorOrder: '',
  orderTime: new Date(),
  useMethod: ''
});
const route = useRoute();
const addFormRef = ref();

// 表单验证规则
const formRules = ref({
  doctorOrder: [
    { required: true, message: "请输入医嘱内容", trigger: "blur" },
    { min: 5, message: "医嘱内容至少5个字符", trigger: "blur" }
  ],
  orderTime: [
    { required: true, message: "请选择开立时间", trigger: "change" }
  ],
  useMethod: [
    { required: true, message: "请输入用法详情", trigger: "blur" },
    { min: 5, message: "用法详情至少5个字符", trigger: "blur" }
  ]
});

// 返回上一页
const back = () => {
  router.push("/diagnosistreatmentorder");
}

// 提交医嘱
const subOrder = () => {
  addFormRef.value.validate(async (valid) => {
    if(valid) {
      try {
        formData.value.diagnosisId = route.query.id;
        let patient = JSON.parse(sessionStorage.getItem("orderPatient"));
        formData.value.patientId = patient.id;
        formData.value.status = 1;
        
        const response = await addDiagnosisOrder(formData.value);
        
        ElMessage.success(response.data || "医嘱添加成功");
        back();
      } catch (error) {
        ElMessage.error(error.message || "医嘱添加失败");
      }
    }
  });
}

// 初始化数据
onMounted(() => {
  medicalName.value = route.query.medicalName;
  let patient = JSON.parse(sessionStorage.getItem("orderPatient"));
  patientName.value = patient.realName;
  
  // 设置默认时间为当前时间
  formData.value.orderTime = new Date();
});
</script>

<style scoped>
.diagnosis-order-add-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.back-btn {
  padding: 10px 20px;
}

.patient-info {
  margin: 20px 0;
  font-size: 16px;
  display: flex;
  align-items: center;
}

.info-label {
  margin-right: 10px;
  font-weight: 500;
}

.patient-tag {
  font-size: 16px;
  padding: 8px 12px;
}

.form-card {
  margin-top: 20px;
  width: 100%;
  max-width: 900px;
}

.submit-btn {
  padding: 12px 30px;
  font-size: 16px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .form-card {
    width: 100%;
  }
  
  :deep(.el-form-item__label) {
    width: 120px !important;
  }
}
</style>