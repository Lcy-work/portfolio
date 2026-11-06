<template>
  <div class="bg-gray-50 min-h-screen p-4 md:p-6">
    <!-- 主内容容器 -->
    <div class="bg-white rounded-xl shadow-sm p-5 md:p-6 transition-all duration-300">
      <!-- 页面标题和返回按钮 -->
      <el-row justify="space-between" class="mb-6 pb-2 border-b">
        <el-col :span="5">
          <h2 class="text-xl font-semibold text-gray-800">增加药品</h2>
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
          <el-form-item label="药品分类" prop="insuranceType">
            <el-select 
              v-model="addDrug.insuranceType" 
              placeholder="请选择" 
              class="w-full"
            >
              <el-option label="甲类" :value="0" />
              <el-option label="乙类" :value="1" />
              <el-option label="丙类" :value="2" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="药品中文名称:" prop="chinaName">
            <el-input 
              v-model="addDrug.chinaName" 
              class="w-full"
              placeholder="请输入药品中文名称"
            />
          </el-form-item>
          
          <el-form-item label="品名:" prop="goodsName">
            <el-input 
              v-model="addDrug.goodsName" 
              class="w-full"
              placeholder="请输入品名"
            />
          </el-form-item>
          
          <el-form-item label="规格:" prop="specifications">
            <el-input 
              v-model="addDrug.specifications" 
              class="w-full"
              placeholder="请输入规格"
            />
          </el-form-item>
          
          <el-form-item label="单位:" prop="drugUnit">
            <el-input 
              v-model="addDrug.drugUnit" 
              class="w-full"
              placeholder="请输入单位"
            />
          </el-form-item>
          
          <el-form-item label="生产企业:" prop="drugManufacturer">
            <el-input 
              v-model="addDrug.drugManufacturer" 
              class="w-full"
              placeholder="请输入生产企业"
            />
          </el-form-item>
          
          <el-form-item label="价格:" prop="drugPrice">
            <el-input-number 
              v-model="addDrug.drugPrice" 
              placeholder="0.00" 
              :precision="2" 
              :step="0.01"
              class="w-full"
            />
          </el-form-item>
          
          <el-form-item label="备注:" prop="remarks">
            <el-input 
              v-model="addDrug.remarks" 
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
              保存
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>



<script setup>
import { reactive, ref } from 'vue';
import { ElForm, ElMessage } from 'element-plus';
import router from "@/router";
import { drugInfoApi } from "@/api/drugInfo"; // 导入药品信息 API

const addFormRef = ref(ElForm);

// 表单校验规则
const addrules = ref({
  insuranceType: [{ required: true, message: "请选择药品分类", trigger: "change" }],
  chinaName: [{ required: true, message: "不能为空", trigger: "blur" }],
  goodsName: [{ required: true, message: "不能为空", trigger: "blur" }],
  specifications: [{ required: true, message: "不能为空", trigger: "blur" }],
  drugUnit: [{ required: true, message: "不能为空", trigger: "blur" }],
  drugManufacturer: [{ required: true, message: "不能为空", trigger: "blur" }],
  drugPrice: [{ required: true, message: "不能为空", trigger: "blur" }],
})

// 表单数据
const addDrug = reactive({
  insuranceType: 0,
  chinaName: '',
  goodsName: '',
  specifications: '',
  drugUnit: '',
  drugManufacturer: '',
  drugPrice: '',
  remarks: '',  
})

// 提交表单
const onSubmit = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      drugInfoApi.add(addDrug).then(
        (response) => {
          ElMessage({
            type: "success",
            message: response.data,
          });
          // 重置表单
          addDrug.insuranceType = 0;
          addDrug.chinaName = "";
          addDrug.goodsName = "";
          addDrug.specifications = "";
          addDrug.drugUnit = "";
          addDrug.drugManufacturer = "";
          addDrug.drugPrice = '';
          addDrug.remarks = "";
        }
      );
    }
  })
}

// 跳转回药品主管理界面
const backToMain = () => {
  router.push("/drugManager");
}
</script>

<style scoped>

</style>