<template>
  <div class="medical-order-add-container">
    <!-- 面包屑导航和返回按钮 -->
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{path:'/medicalserviceorder'}">医疗服务医嘱</el-breadcrumb-item>
        <el-breadcrumb-item>增加医疗服务医嘱</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button @click="back" type="primary" :icon="ArrowLeftBold" class="back-btn">返回</el-button>
    </div>
    
    <!-- 表单区域 -->
    <div class="form-wrapper">
      <el-card class="form-card">
        <div class="form-header">
          <span class="title">增加医疗服务医嘱：</span>
          <el-tag type="info" class="patient-tag">{{ patientName }}</el-tag>
        </div>
        
        <el-form 
          :model="formData" 
          ref="addFormRef" 
          :rules="formRules"
          label-width="150px"
          label-position="left"
          class="medical-form"
        >
          <!-- 医疗服务名称 -->
          <el-form-item label="医疗服务名称：" prop="medicalName">
            <el-input v-model="medicalName" disabled class="disabled-input" />
          </el-form-item>
          
          <!-- 医嘱内容 -->
          <el-form-item label="医嘱内容：" prop="doctorOrder">
            <el-input 
              type="textarea" 
              placeholder="请输入医嘱内容" 
              :autosize="{minRows:2,maxRows:4}" 
              v-model="formData.doctorOrder" 
              clearable
              class="textarea-input"
            />
          </el-form-item>
          
          <!-- 开立时间 -->
          <el-form-item label="开立时间：" prop="orderTime">
            <el-date-picker 
              v-model="formData.orderTime" 
              type="datetime" 
              placeholder="选择开立时间"
              value-format="YYYY-MM-DD HH:mm:ss" 
              format="YYYY-MM-DD HH:mm:ss"
              class="date-picker"
            />
          </el-form-item>
          
          <!-- 用法详情 -->
          <el-form-item label="用法详情说明：" prop="useMethod">
            <el-input 
              type="textarea" 
              placeholder="请输入医疗服务使用详情" 
              :autosize="{minRows:2,maxRows:4}" 
              v-model="formData.useMethod" 
              clearable
              class="textarea-input"
            />
          </el-form-item>
          
          <!-- 操作按钮 -->
          <el-form-item class="form-actions">
            <el-button 
              type="primary" 
              @click="subOrder" 
              class="submit-btn"
            >
              提交医嘱
            </el-button>
            <el-button 
              @click="resetForm" 
              class="reset-btn"
            >
              重置表单
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ArrowLeftBold } from "@element-plus/icons-vue"
import { ElForm, ElMessage } from "element-plus"
import { useRoute, useRouter } from "vue-router"
import { addMedicalServiceOrder } from '@/api/medicalService';

const route = useRoute();
const router = useRouter();
const addFormRef = ref();

// 数据
const patientName = ref('');
const medicalName = ref('');
const formData = ref({
  medicalId: 0,
  patientId: 0,
  status: 1,
  doctorOrder: "",
  orderTime: '',
  useMethod: ''
});

// 表单验证规则
const formRules = {
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
};

// 返回医疗服务医嘱列表
const back = () => {
  router.push("/medicalserviceorder");
}

// 重置表单
const resetForm = () => {
  formData.value = {
    ...formData.value,
    doctorOrder: "",
    orderTime: new Date(),
    useMethod: ''
  };
}

// 提交医疗服务医嘱
const subOrder = async () => {
  try {
    await addFormRef.value.validate();
    
    // 设置医疗服务ID
    formData.value.medicalId = route.query.id || 0;
    
    // 从sessionStorage获取患者信息
    const patientData = sessionStorage.getItem("orderPatient");
    if (!patientData) throw new Error("找不到患者信息");
    
    const patient = JSON.parse(patientData);
    formData.value.patientId = patient.id;
    
    // 提交数据
    const response = await addMedicalServiceOrder(formData.value);
    ElMessage.success(response.data || "医疗服务医嘱添加成功");
    back();
  } catch (error) {
    if (error.message !== "validate") {
      console.error("添加医疗服务医嘱失败:", error);
      ElMessage.error(error.message || "医嘱添加失败，请重试");
    }
  }
}

// 组件挂载时初始化数据
onMounted(() => {
  // 从路由参数获取医疗服务名称
  medicalName.value = route.query.medicalName || "未知服务";
  
  // 从sessionStorage获取患者姓名
  const patientData = sessionStorage.getItem("orderPatient");
  if (patientData) {
    try {
      const patient = JSON.parse(patientData);
      patientName.value = patient.realName || "未知患者";
    } catch (e) {
      console.error("解析患者数据失败:", e);
      patientName.value = "患者信息无效";
    }
  } else {
    patientName.value = "未选择患者";
  }
  
  // 设置默认开立时间为当前时间
  formData.value.orderTime = new Date();
});
</script>

<style scoped>
.medical-order-add-container {
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

.form-wrapper {
  display: flex;
  justify-content: center;
}

.form-card {
  width: 100%;
  max-width: 900px;
  padding: 20px;
}

.form-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.title {
  font-size: 18px;
  font-weight: 500;
  margin-right: 10px;
}

.patient-tag {
  font-size: 16px;
  padding: 8px 12px;
}

.medical-form {
  padding: 0 20px;
}

.disabled-input {
  opacity: 0.8;
}

.textarea-input {
  width: 100%;
}

.date-picker {
  width: 100%;
}

.form-actions {
  margin-top: 30px;
  text-align: center;
}

.submit-btn {
  padding: 12px 30px;
  font-size: 16px;
}

.reset-btn {
  padding: 12px 30px;
  font-size: 16px;
  margin-left: 20px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .form-card {
    padding: 15px;
  }
  
  .medical-form {
    padding: 0;
  }
  
  .form-actions {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .submit-btn,
  .reset-btn {
    width: 100%;
    margin-left: 0;
  }
}
</style>