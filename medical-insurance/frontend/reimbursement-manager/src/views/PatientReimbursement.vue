<template>
  <div class="reimbursement-search-container">
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <el-icon class="card-icon"><SetUp /></el-icon>
          <span>费用报销的参保人员信息查询</span>
        </div>
      </template>
      
      <div class="search-area">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-input 
              v-model="queryName" 
              placeholder="参保人员姓名" 
              prefix-icon="Search"
              clearable
              @keyup.enter="handleQuery"
            />
          </el-col>
          <el-col :span="4">
            <el-button 
              type="primary" 
              :icon="Search" 
              @click="handleQuery"
              class="search-btn"
            >
              搜索
            </el-button>
          </el-col>
        </el-row>
      </div>
      
      <!-- 参保人员信息table -->
      <div class="table-container">
        <el-table 
          :data="tableDataList" 
          highlight-current-row 
          border
          stripe
          class="custom-table"
          :empty-text="loading ? '加载中...' : '暂无数据'"
        >
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="caseNumber" label="住院号" width="100" />
          <el-table-column prop="realName" label="姓名" width="100" />
          <el-table-column prop="gender" label="性别" width="60" align="center" />
          <el-table-column prop="cardNumber" label="身份证号" width="200" />
          <el-table-column prop="homeAddress" label="家庭住址" min-width="180" />
          <el-table-column prop="visitDate" label="入院日期" width="140" />
          <el-table-column prop="workStatus" label="工作状态" width="80" align="center" />
          <el-table-column prop="registMethod" label="结算类别" width="80" align="center" />
          <el-table-column label="操作" width="160" align="center">
            <template #default="scope">
              <el-button 
                size="small" 
                type="primary" 
                @click="toInfo(scope.$index, scope.row)"
                class="action-btn"
              >
                <el-icon><Document /></el-icon>
                详情
              </el-button>
              <el-button 
                size="small" 
                type="success" 
                @click="toDo(scope.$index, scope.row)"
                class="action-btn"
              >
                <el-icon><CreditCard /></el-icon>
                报销
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="pagination-container">
        <el-pagination 
          background 
          layout="total, prev, pager, next, sizes, jumper" 
          :total="total" 
          v-model:current-page="pageNumberNow"
          :page-sizes="[10, 20, 50]"
          @current-change="pageChangeFunc"
          @size-change="sizeChangeFunc"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import router from "@/router";
import { api } from "@/api/index"; // 导入API

const queryName = ref("");
const tableDataList = ref([]);
const total = ref(0);
const pageNumberNow = ref(1);
const pageSize = ref(10);
const loading = ref(false);

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

// 搜索
const handleQuery = () => {
  initData(1, pageSize.value, queryName.value);
};

// 分页切换
const pageChangeFunc = (currentPage) => {
  pageNumberNow.value = currentPage;
  initData(currentPage, pageSize.value, queryName.value);
};

// 每页数量变化
const sizeChangeFunc = (newSize) => {
  pageSize.value = newSize;
  initData(1, newSize, queryName.value);
};

// 跳转详情页
const toInfo = (index, row) => {
  router.push(`/patientreimbursementinfo/${row.id}`);
};

// 跳转报销页
const toDo = (index, row) => {
  router.push(`/patientreimbursementdo/${row.id}`);
};

onMounted(() => {
  initData();
});
</script>

<style scoped>
.reimbursement-search-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.search-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: #fff;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.card-icon {
  margin-right: 10px;
  color: #1890FF;
  font-size: 20px;
}

.search-area {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.search-btn {
  width: 100%;
}

.table-container {
  padding: 20px;
}

.custom-table {
  border-radius: 4px;
}

.custom-table th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 500;
}

.custom-table td {
  padding: 12px 0;
}

.action-btn {
  margin: 0 5px;
}

.pagination-container {
  padding: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .search-card {
    margin: 0 10px;
  }
  
  .custom-table .el-table-column--selection,
  .custom-table .el-table-column--expand {
    width: 40px;
  }
  
  .custom-table .el-table-column__content {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

@media (max-width: 768px) {
  .reimbursement-search-container {
    padding: 10px;
  }
  

  
  .search-btn {
    width: 120px;
  }
  
  .custom-table {
    font-size: 12px;
  }
  
  .pagination-container {
    justify-content: center;
  }
}
</style>