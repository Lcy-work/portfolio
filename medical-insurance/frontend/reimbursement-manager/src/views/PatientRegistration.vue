<template>
  <div class="insurance-management-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <h3 class="page-title">参保人员信息管理</h3>   
    </div>

    <!-- 主要内容区域 -->
    <el-card class="main-card">
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-input
          v-model="queryName"
          placeholder="请输入患者姓名"
          clearable
          style="width: 300px; margin-right: 10px;"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">
          搜索
        </el-button>
      </div>

      <!-- 内容布局 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 参保人员列表 -->
        <el-col :span="16">
          <el-card class="list-card">
            <template #header>
              <el-icon><User /></el-icon>
              参保人员列表
            </template>
            
            <el-table
              :data="tableDataList"
              highlight-current-row
              stripe
              border
              @row-click="handleRowClick"
              class="person-table"
            >
              <el-table-column
                type="index"
                label="序号"
                width="80"
                align="center"
              />
              <el-table-column
                label="住院号"
                prop="caseNumber"
                width="150"
                align="center"
              />
              <el-table-column label="姓名" prop="realName" />
              <el-table-column
                label="身份证"
                prop="cardNumber"
                width="230"
                align="center"
              >
                <template #default="scope">
                  <div class="card-number-container" @mousedown="startDrag(scope.$index, $event)"
                       @mousemove="onDrag(scope.$index, $event)" @mouseup="stopDrag(scope.$index)"
                       @mouseleave="stopDrag(scope.$index)">
                    <span class="card-number">{{ scope.row.cardNumber }}</span>
                    <el-tooltip 
                      v-if="scope.row.cardNumber && scope.row.cardNumber.length > 18" 
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
                width="80"
                align="center"
              />
              <el-table-column
                label="年龄"
                prop="age"
                width="80"
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
              style="margin-top: 15px; text-align: right;"
            />
          </el-card>
        </el-col>
        
        <!-- 参保人员详情 -->
        <el-col :span="8">
          <el-card class="detail-card">
            <template #header>
              <el-icon><Document /></el-icon>
              <span>参保人员详情</span>
              <el-tag 
                type="success" 
                size="small" 
                v-if="formData.realName"
                class="selected-user"
              >
                当前：{{ formData.realName }}
              </el-tag>
              <el-tag 
                type="warning" 
                size="small" 
                v-else
                class="selected-user"
              >
                请选择参保人员
              </el-tag>
            </template>
            
            <el-form :model="formData" label-width="80px" class="detail-form">
              <el-form-item label="姓名">
                <el-input v-model="formData.realName" disabled />
              </el-form-item>
              <el-form-item label="身份证号">
                <el-input v-model="formData.cardNumber" disabled />
              </el-form-item>
              <el-form-item label="年龄">
                <el-input v-model="formData.age" style="width: 80px;" disabled />
                {{ formData.ageType }}
              </el-form-item>
              <el-form-item label="出生日期">
                <el-input v-model="formData.birthday" disabled />
              </el-form-item>
              <el-form-item label="性别">
                <el-input v-model="formData.gender" disabled />
              </el-form-item>
              <el-form-item label="家庭住址">
                <el-input v-model="formData.homeAddress" disabled />
              </el-form-item>
              <el-form-item label="结算类别">
                <el-select v-model="formData.registMethod" style="width: 120px;">
                  <el-option key="自费" value="自费" label="自费"></el-option>
                  <el-option key="医保" value="医保" label="医保"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="工作状态">
                <el-select v-model="formData.workStatus" style="width: 120px;">
                  <el-option key="在职" value="在职" label="在职"></el-option>
                  <el-option key="退休" value="退休" label="退休"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
            
            <div class="action-buttons">
              <el-button type="primary" @click="handleEdit">
                <el-icon><Edit /></el-icon>修改
              </el-button>
              <el-button type="success" @click="submitForm" :disabled="!isEditing">
                <el-icon><Check /></el-icon>保存
              </el-button>
              <el-button type="info" @click="cancelEdit" :disabled="!isEditing">
                <el-icon><Close /></el-icon>取消
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { api } from "@/api/index"; // 导入API

// 状态管理
const queryName = ref("");
const tableDataList = ref([]);
const total = ref(0);
const formData = ref({});
const pageNumberNow = ref(1);
const isEditing = ref(false);
const originalData = ref({}); // 保存原始数据用于取消编辑
const cardNumberRefs = ref([]);
const dragState = reactive({
  isDragging: false,
  startX: 0,
  startScrollLeft: 0,
  currentIndex: -1
});

// // 初始化参保人员列表
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
//     ElMessage.error("获取参保人员列表失败，请稍后重试");
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

// 分页切换
const pageChangeFunc = (currentPage) => {
  pageNumberNow.value = currentPage;
  initData(currentPage, 10, queryName.value);
};

// 点击行选中参保人员
const handleRowClick = (row) => {
  // 如果正在编辑，询问是否保存
  if (isEditing.value) {
    ElMessageBox.confirm(
      "当前编辑尚未保存，是否继续选择其他人员？",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    ).then(() => {
      formData.value = { ...row };
      isEditing.value = false;
    }).catch(() => {
      // 取消操作，保持当前编辑状态
    });
    return;
  }
  
  formData.value = { ...row };
};

// 进入编辑状态
const handleEdit = () => {
  if (!formData.value.id) {
    ElMessage.warning("请先选择参保人员");
    return;
  }
  
  // 保存原始数据
  originalData.value = { ...formData.value };
  isEditing.value = true;
};

// 取消编辑
const cancelEdit = () => {
  if (isEditing.value) {
    formData.value = { ...originalData.value };
    isEditing.value = false;
  }
};

// 提交修改
const submitForm = () => {
  if (!isEditing.value) {
    handleEdit();
    return;
  }
  
  // 表单验证
  if (!formData.value.registMethod) {
    ElMessage.warning("请选择结算类别");
    return;
  }
  
  if (!formData.value.workStatus) {
    ElMessage.warning("请选择工作状态");
    return;
  }
  
  ElMessageBox.confirm(
    "确定要修改该参保人员信息吗？",
    "确认",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "question"
    }
  ).then(() => {
    api.patientRegistration.update(formData.value).then(response => {
      ElMessage.success("修改成功");
      isEditing.value = false;
      
      // 更新列表中的数据
      const index = tableDataList.value.findIndex(item => item.id === formData.value.id);
      if (index !== -1) {
        tableDataList.value[index] = { ...formData.value };
      }
    }).catch(error => {
      console.error("修改失败", error);
      ElMessage.error("修改失败，请稍后重试");
    });
  }).catch(() => {
    // 取消操作
  });
};

// 搜索
const handleQuery = () => {
  pageNumberNow.value = 1;
  initData(1, 10, queryName.value);
};

// 身份证号拖动功能
const startDrag = (index, e) => {
  const element = cardNumberRefs.value[index];
  if (!element) return;
  
  dragState.isDragging = true;
  dragState.startX = e.pageX;
  dragState.startScrollLeft = element.scrollLeft;
  dragState.currentIndex = index;
  element.style.cursor = "grabbing";
};

const onDrag = (index, e) => {
  if (!dragState.isDragging || index !== dragState.currentIndex) return;
  e.preventDefault();
  
  const element = cardNumberRefs.value[index];
  if (!element) return;
  
  const x = e.pageX - dragState.startX;
  element.scrollLeft = dragState.startScrollLeft - x;
};

const stopDrag = (index) => {
  if (index !== dragState.currentIndex) return;
  
  dragState.isDragging = false;
  const element = cardNumberRefs.value[index];
  if (element) {
    element.style.cursor = "grab";
  }
};

onMounted(() => {
  initData();
});
</script>
