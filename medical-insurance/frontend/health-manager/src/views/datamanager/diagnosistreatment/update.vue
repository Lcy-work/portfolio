<template>
  <div class="bg-gray-50 min-h-screen p-4 md:p-6">
    <!-- 主内容容器 -->
    <div class="bg-white rounded-xl shadow-sm p-5 md:p-6 transition-all duration-300">
      <!-- 页面标题和返回按钮 -->
      <el-row justify="space-between" class="mb-6 pb-2 border-b">
        <el-col :span="5">
          <h2 class="text-xl font-semibold text-gray-800">修改医疗服务</h2>
        </el-col>
        <el-col :span="2">
          <el-button 
            type="primary" 
            @click="backToMain"
            class="w-full transition-all hover:translate-y-[-1px]"
          >
            返回
          </el-button>
        </el-col>
      </el-row>

      <!-- 表单区域 -->
      <div class="w-full max-w-3xl mx-auto">
        <el-form 
          :model="addDrug" 
          label-width="150px" 
          ref="addFormRef" 
          :rules="addrules"
          class="space-y-4"
        >
          <el-form-item label="项目类别:" prop="medicalType">
            <el-input 
              v-model="addDrug.medicalType" 
              class="w-full"
              placeholder="请输入项目类别"
            />
          </el-form-item>
          
          <el-form-item label="项目编码:" prop="medicalNumber">
            <el-input 
              v-model="addDrug.medicalNumber" 
              class="w-full"
              placeholder="请输入项目编码"
            />
          </el-form-item>
          
          <el-form-item label="项目国家编码:" prop="countryNumber">
            <el-input 
              v-model="addDrug.countryNumber" 
              class="w-full"
              placeholder="请输入项目国家编码"
            />
          </el-form-item>
          
          <el-form-item label="项目名称:" prop="medicalName">
            <el-input 
              v-model="addDrug.medicalName" 
              class="w-full"
              placeholder="请输入项目名称"
            />
          </el-form-item>
          
          <el-form-item label="项目说明:" prop="medicalInfo">
            <el-input 
              v-model="addDrug.medicalInfo" 
              class="w-full"
              placeholder="请输入项目说明"
            />
          </el-form-item>
          
          <el-form-item label="除外内容:" prop="medicalExclude">
            <el-input 
              v-model="addDrug.medicalExclude" 
              class="w-full"
              placeholder="请输入除外内容"
            />
          </el-form-item>
          
          <el-form-item label="计价单位:" prop="medicalUnit">
            <el-input 
              v-model="addDrug.medicalUnit" 
              class="w-full"
              placeholder="请输入计价单位"
            />
          </el-form-item>
          
          <el-form-item label="价格:" prop="medicalPrice">
            <el-input-number 
              v-model="addDrug.medicalPrice" 
              placeholder="0.00" 
              :precision="2" 
              :step="0.01"
              class="w-full"
            />
          </el-form-item>
          
          <el-form-item label="备注:" prop="remark">
            <el-input 
              v-model="addDrug.remark" 
              type="textarea" 
              placeholder="请输入备注" 
              :autosize="{minRows:2,maxRows:4}"
              class="w-full"
            />
          </el-form-item>
          
          <el-form-item class="pt-4">
            <el-button 
              type="primary" 
              @click="onSubmit"
              class="w-full md:w-auto px-6 py-2"
            >
              修改
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { ElForm, ElMessage } from 'element-plus';
import router from "@/router";
import { useRoute } from "vue-router";
// 引入API模块
import { diagnosisTreatmentApi } from '@/api/diagnosistreatment';

const addFormRef = ref(ElForm);

// 表单校验规则
const addrules = ref({
  medicalType: [{ required: true, message: "不能为空", trigger: "blur" }],
  medicalNumber: [{ required: true, message: "不能为空", trigger: "blur" }],
  countryNumber: [{ required: true, message: "不能为空", trigger: "blur" }],
  medicalName: [{ required: true, message: "不能为空", trigger: "blur" }],
  medicalInfo: [{ required: true, message: "不能为空", trigger: "blur" }],
  medicalUnit: [{ required: true, message: "不能为空", trigger: "blur" }],
})

// 表单数据
const addDrug = reactive({
  id: 0,
  medicalType: "",
  medicalNumber: '',
  countryNumber: '',
  medicalName: '',
  medicalInfo: '',
  medicalExclude: '',
  medicalUnit: '',
  medicalPrice: 0,
  remark: ''
})

// 提交表单
const onSubmit = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      // 使用API模块更新数据
      diagnosisTreatmentApi.update(addDrug).then(
        (response) => {
          ElMessage({
            type: "success",
            message: response.data,
          });
        }
      );
    }
  })
}

// 跳转回药品主管理界面
const backToMain = () => {
  router.push("/diagnosistreatment");
}

// 初始化生命周期函数
onMounted(() => {
  const route = useRoute();
  // 使用API模块获取详情数据
  diagnosisTreatmentApi.getById(route.params.id).then(
    (response) => {
      addDrug.id = response.data.id;
      addDrug.medicalType = response.data.medicalType;
      addDrug.medicalNumber = response.data.medicalNumber;
      addDrug.countryNumber = response.data.countryNumber;
      addDrug.medicalName = response.data.medicalName;
      addDrug.medicalInfo = response.data.medicalInfo;
      addDrug.medicalExclude = response.data.medicalExclude;
      addDrug.medicalUnit = response.data.medicalUnit;
      addDrug.medicalPrice = response.data.medicalPrice;
      addDrug.remark = response.data.remark;
    }
  )
})
</script>

<style scoped>

</style>