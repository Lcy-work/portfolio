<template>
  <div class="drug-order-add-container">
    <!-- 面包屑导航和返回按钮 -->
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{path:'/drugorder'}">药品处方医嘱</el-breadcrumb-item>
        <el-breadcrumb-item>增加药品处方医嘱</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button @click="back" type="primary" :icon="ArrowLeftBold" class="back-btn">返回</el-button>
    </div>
    
    <!-- 表单区域 -->
    <div class="form-wrapper">
      <el-card class="form-card">
        <div class="form-header">
          <span class="title">药品医嘱：</span>
          <el-tag type="info" class="patient-tag">{{ patientName }}</el-tag>
        </div>
        
        <el-form 
          :model="formData" 
          ref="addFormRef" 
          :rules="formRules"
          label-width="120px"
          label-position="left"
          class="drug-form"
        >
          <!-- 药品名称 -->
          <el-form-item label="药品名称：" prop="chinaName">
            <el-input v-model="drugName" disabled class="disabled-input" />
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
          
          <!-- 时间选择 -->
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="起始时间：" prop="startTime">
                <el-date-picker 
                  v-model="formData.startTime" 
                  type="datetime" 
                  placeholder="选择起始时间"
                  value-format="YYYY-MM-DD HH:mm:ss" 
                  format="YYYY-MM-DD HH:mm:ss"
                  class="date-picker"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间：" prop="endTime">
                <el-date-picker 
                  v-model="formData.endTime" 
                  type="datetime" 
                  placeholder="选择结束时间"
                  value-format="YYYY-MM-DD HH:mm:ss" 
                  format="YYYY-MM-DD HH:mm:ss"
                  class="date-picker"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <!-- 使用频次 -->
          <el-form-item label="使用频次：" prop="orderNumber">
            <el-input-number 
              v-model="formData.orderNumber" 
              :precision="0" 
              :step="1" 
              :min="1" 
              :max="10"
              controls-position="right"
              class="number-input"
            />
            <span class="unit">次/天</span>
          </el-form-item>
          
          <!-- 药品用法 -->
          <el-form-item label="药品用法：" prop="useMethod">
            <el-input 
              type="textarea" 
              placeholder="请输入药品使用方法" 
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
import { ref, onMounted } from 'vue';
import { ArrowLeftBold } from "@element-plus/icons-vue"
import { ElForm, ElMessage } from "element-plus"
import { useRoute, useRouter } from 'vue-router'
import { addDrugOrder } from '@/api/drug';

const route = useRoute();
const router = useRouter();
const addFormRef = ref();

// 数据
const drugName = ref('');
const patientName = ref('');
const formData = ref({
  drugId: 0,
  patientId: 0,
  status: 1,
  doctorOrder: "",
  startTime: '',
  endTime: '',
  orderNumber: 1,
  useMethod: ''
});

// 表单验证规则
const formRules = {
  doctorOrder: [
    { required: true, message: "请输入医嘱内容", trigger: "blur" },
    { min: 5, message: "医嘱内容至少5个字符", trigger: "blur" }
  ],
  startTime: [
    { required: true, message: "请选择起始时间", trigger: "change" }
  ],
  endTime: [
    { required: true, message: "请选择结束时间", trigger: "change" },
    { validator: validateEndTime, trigger: "change" }
  ],
  orderNumber: [
    { required: true, message: "请输入使用频次", trigger: "blur" },
    { type: "number", min: 1, message: "频次不能小于1", trigger: "blur" },
    { type: "number", max: 10, message: "频次不能大于10", trigger: "blur" }
  ],
  useMethod: [
    { required: true, message: "请输入药品用法", trigger: "blur" },
    { min: 5, message: "用法说明至少5个字符", trigger: "blur" }
  ]
};

// 验证结束时间是否晚于开始时间
function validateEndTime(rule, value, callback) {
  if (!value) {
    callback(new Error("请选择结束时间"));
  } else if (new Date(value) <= new Date(formData.value.startTime)) {
    callback(new Error("结束时间必须晚于开始时间"));
  } else {
    callback();
  }
}

// 返回药品医嘱列表
const back = () => {
  router.push("/drugorder");
}

// 重置表单
const resetForm = () => {
  formData.value = {
    ...formData.value,
    doctorOrder: "",
    startTime: '',
    endTime: '',
    orderNumber: 1,
    useMethod: ''
  };
}

// 提交药品医嘱
const subOrder = async () => {
  try {
    await addFormRef.value.validate();
    
    // 设置药品ID
    formData.value.drugId = route.query.id || 0;
    
    // 从sessionStorage获取患者信息
    const patientData = sessionStorage.getItem("orderPatient");
    if (!patientData) throw new Error("找不到患者信息");
    
    const patient = JSON.parse(patientData);
    formData.value.patientId = patient.id;
    
    // 提交数据
    const response = await addDrugOrder(formData.value);
    ElMessage.success(response.data || "药品医嘱添加成功");
    back();
  } catch (error) {
    if (error.message !== "validate") {
      console.error("添加药品医嘱失败:", error);
      ElMessage.error(error.message || "医嘱添加失败，请重试");
    }
  }
}

// 组件挂载时初始化数据
onMounted(() => {
  // 从路由参数获取药品名称
  drugName.value = route.query.chinaName || "未知药品";
  
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
  
  // 设置默认起始时间为当前时间
  formData.value.startTime = new Date();
});
</script>

<style scoped>
.drug-order-add-container {
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

.drug-form {
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

.number-input {
  width: 120px;
}

.unit {
  margin-left: 10px;
  color: #666;
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
  
  .drug-form {
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