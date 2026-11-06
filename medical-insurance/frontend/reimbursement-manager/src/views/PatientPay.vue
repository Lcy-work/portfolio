<template>
  <div class="insurance-cost-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h3 class="page-title">参保人员费用查询</h3>
     
    </div>

    <el-row :gutter="20" class="main-content">
      <!-- 参保人员信息卡片 -->
      <el-col :span="8">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <el-icon><User /></el-icon>
              <span>参保人员信息</span>
            </div>
          </template>
          
          <!-- 搜索区域 -->
          <div class="search-area">
            <el-input
              v-model="queryName"
              placeholder="请输入参保人员姓名"
              clearable
              class="search-input"
            >
              <template #append>
                <el-button type="primary" icon="Search" @click="handleQuery">
                  搜索
                </el-button>
              </template>
            </el-input>
          </div>
          
          <!-- 参保人员列表 -->
          <el-table
            :data="tableDataList"
            highlight-current-row
            stripe
            border
            @row-click="handleRowClick"
            class="person-list"
          >
            <el-table-column
              type="index"
              label="序号"
              width="60"
              align="center"
            />
            <el-table-column
              label="住院号"
              prop="caseNumber"
              width="100"
              align="center"
            />
            <el-table-column label="姓名" prop="realName" width="80" />
            <el-table-column
              label="身份证号"
              prop="cardNumber"
              width="180"
              align="center"
            >
              <template #default="scope">
                <div 
                  class="card-number-container"
                  :class="{ 'has-tooltip': scope.row.cardNumber && scope.row.cardNumber.length > 10 }"
                  @mousedown="startDrag(scope.$index, $event)"
                  @mousemove="onDrag(scope.$index, $event)"
                  @mouseup="stopDrag(scope.$index)"
                  @mouseleave="stopDrag(scope.$index)"
                  ref="cardNumberRefs"
                >
                  <span 
                    class="card-number"
                    :title="scope.row.cardNumber"
                  >
                    {{ scope.row.cardNumber }}
                  </span>
                  <el-tooltip 
                    v-if="scope.row.cardNumber && scope.row.cardNumber.length > 10" 
                    :content="scope.row.cardNumber" 
                    placement="top" 
                  >
                    
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="性别"
              prop="gender"
              width="60"
              align="center"
            />
          </el-table>
          
          <!-- 分页 -->
          <el-pagination
            background
            layout="prev, pager, next, total"
            :total="total"
            v-model:currentPage="pageNumberNow"
            @current-change="pageChangeFunc"
            class="pagination"
          />
        </el-card>
      </el-col>

      <!-- 费用详情卡片 -->
      <el-col :span="16">
        <el-card class="cost-card">
          <template #header>
            <div class="card-header">
              <el-icon><Document /></el-icon>
              <span>费用详情</span>
              <el-tag 
                type="success" 
                size="small" 
                class="selected-user"
                v-if="formData.realName"
              >
                当前：{{ formData.realName }}
              </el-tag>
              <el-tag 
                type="warning" 
                size="small" 
                class="selected-user"
                v-else
              >
                请选择参保人员
              </el-tag>
            </div>
          </template>
          
          <!-- 费用汇总 -->
          <div class="cost-summary">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-card class="summary-card">
                  <div class="summary-title">药品费用</div>
                  <div class="summary-value">
                    {{ drugTotal | currency }}
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="summary-card">
                  <div class="summary-title">诊疗项目</div>
                  <div class="summary-value">
                    {{ diagnosisTotal | currency }}
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="summary-card">
                  <div class="summary-title">医疗服务</div>
                  <div class="summary-value">
                    {{ medicalTotal | currency }}
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <div class="grand-total">
              <span class="total-label">总计费用：</span>
              <span class="total-value">{{ totalCost | currency }}</span>
            </div>
          </div>
          
          <!-- 费用明细标签页 -->
          <el-tabs type="border-card" class="cost-tabs">
            <el-tab-pane label="药品费用明细">
              <el-table
                :data="drugList"
                stripe
                border
                fit
                class="detail-table"
              >
                <el-table-column
                  type="index"
                  label="序号"
                  width="60"
                  align="center"
                />
                <el-table-column
                  prop="chinaName"
                  label="药品名称"
                  width="150"
                />
                <el-table-column
                  prop="specifications"
                  label="规格"
                  width="120"
                />
                <el-table-column
                  prop="drugUnit"
                  label="单位"
                  width="80"
                />
                <el-table-column
                  prop="drugManufacturer"
                  label="生产厂家"
                />
                <el-table-column
                  prop="drugPrice"
                  label="单价(元)"
                  width="100"
                  align="right"
                />
                <el-table-column
                  label="数量"
                  width="80"
                  align="right"
                >
                  <template #default="scope">
                    {{ scope.row.drugCount || 1 }}
                  </template>
                </el-table-column>
                <el-table-column
                  label="金额(元)"
                  width="120"
                  align="right"
                >
                  <template #default="scope">
                    {{ (parseFloat(scope.row.drugPrice || 0) * 
                        (parseFloat(scope.row.drugCount) || 1)).toFixed(2) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            
            <el-tab-pane label="诊疗项目明细">
              <el-table
                :data="diagnosisList"
                stripe
                border
                fit
                class="detail-table"
              >
                <el-table-column
                  type="index"
                  label="序号"
                  width="60"
                  align="center"
                />
                <el-table-column
                  prop="medicalName"
                  label="诊疗项目名称"
                  width="150"
                />
                <el-table-column
                  prop="medicalNumber"
                  label="诊疗项目编码"
                  width="120"
                />
                <el-table-column
                  prop="medicalExclude"
                  label="诊疗项目除外内容"
                />
                <el-table-column
                  prop="medicalUnit"
                  label="计价单位"
                  width="80"
                />
                <el-table-column
                  prop="medicalPrice"
                  label="单价(元)"
                  width="100"
                  align="right"
                />
                <el-table-column
                  label="数量"
                  width="80"
                  align="right"
                >
                  <template #default="scope">
                    {{ scope.row.medicalCount || 1 }}
                  </template>
                </el-table-column>
                <el-table-column
                  label="金额(元)"
                  width="120"
                  align="right"
                >
                  <template #default="scope">
                    {{ (parseFloat(scope.row.medicalPrice || 0) * 
                        (parseFloat(scope.row.medicalCount) || 1)).toFixed(2) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            
            <el-tab-pane label="医疗服务明细">
              <el-table
                :data="medicalList"
                stripe
                border
                fit
                class="detail-table"
              >
                <el-table-column
                  type="index"
                  label="序号"
                  width="60"
                  align="center"
                />
                <el-table-column
                  prop="medicalName"
                  label="医疗服务名称"
                  width="150"
                />
                <el-table-column
                  prop="medicalNumber"
                  label="医疗服务编码"
                  width="120"
                />
                <el-table-column
                  prop="medicalExclude"
                  label="医疗服务除外内容"
                />
                <el-table-column
                  prop="medicalUnit"
                  label="计价单位"
                  width="80"
                />
                <el-table-column
                  prop="medicalPrice"
                  label="单价(元)"
                  width="100"
                  align="right"
                />
                <el-table-column
                  label="数量"
                  width="80"
                  align="right"
                >
                  <template #default="scope">
                    {{ scope.row.medicalCount || 1 }}
                  </template>
                </el-table-column>
                <el-table-column
                  label="金额(元)"
                  width="120"
                  align="right"
                >
                  <template #default="scope">
                    {{ (parseFloat(scope.row.medicalPrice || 0) * 
                        (parseFloat(scope.row.medicalCount) || 1)).toFixed(2) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from "vue";
import { api } from "@/api/index"; // 导入API

// 状态管理
const queryName = ref("");
const tableDataList = ref([]);
const total = ref(0);
const formData = ref({});
const pageNumberNow = ref(1);
const drugList = ref([]);
const diagnosisList = ref([]);
const medicalList = ref([]);
const cardNumberRefs = ref([]);
const isDragging = ref({});
const startX = ref({});
const startScrollLeft = ref({});

// 计算属性 - 各项费用总计
const drugTotal = computed(() => {
  return drugList.value.reduce((sum, item) => {
    const price = parseFloat(item.drugPrice || 0);
    const count = parseFloat(item.drugCount || 1);
    return sum + (price * count);
  }, 0).toFixed(2);
});

const diagnosisTotal = computed(() => {
  return diagnosisList.value.reduce((sum, item) => {
    const price = parseFloat(item.medicalPrice || 0);
    const count = parseFloat(item.medicalCount || 1);
    return sum + (price * count);
  }, 0).toFixed(2);
});

const medicalTotal = computed(() => {
  return medicalList.value.reduce((sum, item) => {
    const price = parseFloat(item.medicalPrice || 0);
    const count = parseFloat(item.medicalCount || 1);
    return sum + (price * count);
  }, 0).toFixed(2);
});

const totalCost = computed(() => {
  return (parseFloat(drugTotal.value) + 
          parseFloat(diagnosisTotal.value) + 
          parseFloat(medicalTotal.value)).toFixed(2);
});

// 初始化参保人员列表
// const initData = (pageNumber = 1, pageSize = 10, searchName = "") => {
//   api.patientRegistration.getPageList({
//     pageNumber,
//     pageSize,
//     searchName
//   }).then(response => {
//     tableDataList.value = response.data.data || [];
//     total.value = response.data.total || 0;
//   }).catch(error => {
//     console.error("获取参保人员列表失败", error);
//     this.$message.error("获取参保人员列表失败，请稍后重试");
//   });
// };

// 初始化表格数据
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
  api.patientRegistration.getPageList({
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

// 搜索参保人员
const handleQuery = () => {
  pageNumberNow.value = 1;
  initData(1, 10, queryName.value);
};

// 点击参保人员行加载费用详情
const handleRowClick = (row) => {
  formData.value = row;
  
  // 加载药品费用
  api.inpatientDrug.getByPatientId(row.id).then(res => {
    drugList.value = res.data.data || [];
  }).catch(error => {
    console.error("获取药品费用失败", error);
    drugList.value = [];
  });
  
  // 加载诊疗项目费用
  api.inpatientDiagnosis.getByPatientId(row.id).then(res => {
    diagnosisList.value = res.data.data || [];
  }).catch(error => {
    console.error("获取诊疗项目费用失败", error);
    diagnosisList.value = [];
  });
  
  // 加载医疗服务费用
  api.inpatientMedical.getByPatientId(row.id).then(res => {
    medicalList.value = res.data.data || [];
  }).catch(error => {
    console.error("获取医疗服务费用失败", error);
    medicalList.value = [];
  });
};

// 分页切换
const pageChangeFunc = (currentPage) => {
  pageNumberNow.value = currentPage;
  initData(currentPage, 10, queryName.value);
};

// 格式化货币过滤器
const currency = (value) => {
  return parseFloat(value).toFixed(2);
};

// 拖动功能实现
const startDrag = (index, e) => {
  const element = cardNumberRefs.value[index];
  if (!element) return;
  
  isDragging.value[index] = true;
  startX.value[index] = e.pageX;
  startScrollLeft.value[index] = element.scrollLeft;
  element.style.cursor = "grabbing";
};

const onDrag = (index, e) => {
  if (!isDragging.value[index]) return;
  e.preventDefault();
  const element = cardNumberRefs.value[index];
  if (!element) return;
  
  const x = e.pageX - startX.value[index];
  element.scrollLeft = startScrollLeft.value[index] - x;
};

const stopDrag = (index) => {
  isDragging.value[index] = false;
  const element = cardNumberRefs.value[index];
  if (element) {
    element.style.cursor = "grab";
  }
};

onMounted(() => {
  initData();
});

onUnmounted(() => {
  // 清理所有拖动状态
  Object.keys(isDragging.value).forEach(index => {
    isDragging.value[index] = false;
  });
});
</script>

