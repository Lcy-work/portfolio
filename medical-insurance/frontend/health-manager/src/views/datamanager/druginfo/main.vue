<template>
  <div class="bg-gray-50 min-h-screen p-4 md:p-6">
    <!-- 主内容容器 -->
    <div class="bg-white rounded-xl shadow-sm p-5 md:p-6 transition-all duration-300 hover:shadow-md">
      <!-- 页面标题 -->
      <h2 class="text-xl font-semibold text-gray-800 mb-6">药品管理</h2>

      <!-- 搜索区域 -->
      <div class="flex flex-col sm:flex-row gap-4 mb-6 items-stretch sm:items-center">
        <el-row :gutter="0" class="search-row">
          <el-col :span="20">
            <el-input 
              v-model="inputDrugName" 
              placeholder="药品名称" 
              class="search-input"
            />
          </el-col>
          <el-col :span="2">
            <el-button 
              type="primary" 
              @click="searchDrugByName()" 
              class="search-button"
            >
              <Search class="mr-2" />搜索
            </el-button>
          </el-col>
        </el-row>
      </div>

      <el-divider class="my-5" />

      <!-- 操作按钮区域 -->
      <div class="flex flex-wrap gap-3 mb-6">
        <el-button 
          type="primary" 
          :icon="Plus" 
          @click="toAddView"
          class="mr-2"
          size="default"
        >
          增加药品
        </el-button>
        <el-button 
          type="danger" 
          :icon="Delete" 
          @click="delSelection"
          class="ml-2"
          size="default"
          :disabled="multipleSelection.length === 0"
        >
          删除选中
        </el-button>
      </div>

      <!-- 表格区域 -->
      <div class="overflow-x-auto rounded-lg border border-gray-100 shadow-sm mb-6" style="height: 400px;">
        <el-table 
          ref="tableRef"
          :data="drugList" 
          stripe 
          style="height: 100%; width: 100%;" 
          @selection-change="handleSelectionChange"
          size="default"
          :header-cell-style="{ 
            background: '#f5f7fa', 
            color: '#333', 
            fontWeight: 500 
          }"
          :cell-style="{ padding: '8px 12px' }"
        >
          <el-table-column type="selection" width="45" />
          <el-table-column type="index" width="50" align="center" />
          <el-table-column prop="insuranceType" label="药品类型" width="180" align="center" />
          <el-table-column prop="chinaName" label="药品名称" width="150" />
          <el-table-column prop="goodsName" label="品名" width="100" />
          <el-table-column prop="specifications" label="规格" width="200" />
          <el-table-column prop="drugUnit" label="单位" width="100" />
          <el-table-column prop="drugPrice" label="价格" width="100" />
          <el-table-column prop="drugManufacturer" label="生产厂家" />
          <el-table-column prop="remarks" label="备注" width="100" />
          <el-table-column label="操作" width="120" align="center">
            <template #default="scope">
              <el-button 
                link 
                type="primary" 
                @click="toUpdate(scope.$index,scope.row)"
                class="text-blue-600 hover:text-blue-800 transition-colors"
                size="small"
              >
                修改
              </el-button>
              <el-button 
                link 
                type="danger" 
                @click="delData(scope.$index,scope.row)"
                class="text-red-600 hover:text-red-800 transition-colors"
                size="small"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页区域 -->
      <div class="flex flex-col sm:flex-row justify-between items-center mt-4 gap-4">
        <div class="text-sm text-gray-600">
          共 {{ total }} 条记录
        </div>
        <el-pagination 
          background 
          layout="prev, pager, next" 
          :total="total" 
          @current-change="pageChangeFunc"
          v-model:currentPage="pageNumberNow"
          size="small"
          :transition="false"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>



<script setup>
import { inject, ref, onMounted, onUnmounted, nextTick } from "vue";
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search, Plus, Delete } from "@element-plus/icons-vue";
import router from "@/router";
import { drugInfoApi } from "@/api/drugInfo"; // 导入药品信息 API

// 自定义节流的ResizeObserver
const useThrottledResizeObserver = (callback, delay = 100) => {
  let timer = null;
  let lastArgs = null;
  
  const throttledCallback = (...args) => {
    lastArgs = args;
    if (!timer) {
      timer = setTimeout(() => {
        callback(...lastArgs);
        timer = null;
      }, delay);
    }
  };
  
  const observer = new ResizeObserver(throttledCallback);
  
  return {
    observe: (target) => observer.observe(target),
    unobserve: (target) => observer.unobserve(target),
    disconnect: () => {
      if (timer) clearTimeout(timer);
      observer.disconnect();
    }
  };
};

const total = ref(1);
const inputDrugName = ref('');
const drugList = ref([]);
const pageNumberNow = ref(1);
const multipleSelection = ref([]);
const tableRef = ref(null);

// 使用节流的ResizeObserver
const { observe, disconnect } = useThrottledResizeObserver(() => {
  if (tableRef.value) {
    // 可选：强制表格重新计算布局
    // nextTick(() => {
    //   tableRef.value.doLayout();
    // });
  }
});

// 初始化表格数据
const initDrug = (pageNumber, pageSize, searchName) => {
  // 参数校验
  const currentPage = typeof pageNumber === "number" && pageNumber > 0 
    ? pageNumber 
    : 1;
  const currentSize = typeof pageSize === "number" && pageSize > 0 
    ? pageSize 
    : 10;
  const searchKey = typeof searchName === "string" ? searchName : "";

  // 更新当前页码（用于分页组件回显）
  pageNumberNow.value = currentPage;

  // 调用后端分页接口
//   axios.get(
//     "selectPageList?pageNumber="+pageNumber+"&pageSize="+pageSize+"&drugName="+searchName
//   )
 drugInfoApi.getPageList({
  pageNumber,
  pageSize,
  drugName: searchName
}).then((response) => {
    // 后端返回格式：ResultVo<Map<String, Object>>，结构为 {data: {data: [], total: 0}, code: 200, msg: "success"}
    const result = response.data;
	console.log("后端响应数据：", result); // 打印完整响应，用于调试
	 // 1. 校验响应是否存在
    if (!result) {
      ElMessage.error("后端返回数据格式错误");
      return;
    }
    if (result.code === 200 || result.code === "200") {
      // 绑定表格数据（确保是数组）
      drugList.value =  Array.isArray(result.data.records) ? result.data.records : [];
      // 绑定总记录数
      total.value = typeof result.data.total === "number" ? result.data.total : 0;
	  ElMessage.success(`加载成功，共${total.value}条数据`);
    } else {
      ElMessage.error(`获取数据失败：${result.msg}`);
    }
  }).catch((error) => {
    console.error("请求分页接口失败：", error);
    ElMessage.error("网络错误，无法获取数据");
  });
};

// 分页组件页面改变触发事件
const pageChangeFunc = (currentPage) => {
  initDrug(currentPage, 10, inputDrugName.value);
};

// 根据药品名称进行搜索
const searchDrugByName = () => {
  initDrug(1, 10, inputDrugName.value);
};

// 删除方法
const delData = (index, row) => {
  ElMessageBox.confirm(
    `是否要删除药品：${row.chinaName}`,
    '删除操作',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    drugInfoApi.deleteById(row.id).then(
      (response) => {
        ElMessage({
          type: 'success',
          message: response.data,
        });
        initDrug(1, 10, inputDrugName.value);
        pageNumberNow.value = 1;
      }
    );
  });
};

// table多选得到数据
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

// 点击删除选中按钮，发送选中数据到后端
const delSelection = () => {
  if (multipleSelection.value.length === 0) {
    ElMessage({
      type: 'warning',
      message: '请选择要删除的药品',
    });
    return;
  }

  const ids = multipleSelection.value.map(item => item.id);
  ElMessageBox.confirm(
    `是否要删除${multipleSelection.value.length}个药品`,
    '删除操作',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    drugInfoApi.deleteBatch(ids).then(
      (response) => {
        ElMessage({
          type: 'success',
          message: response.data,
        });
        initDrug(1, 10, inputDrugName.value);
        pageNumberNow.value = 1;
      }
    );
  });
};

// 点击增加药品按钮，实现页面跳转
const toAddView = () => {
  router.push("/addDrug");
};

// 点击修改药品按钮，携带药品id，实现页面跳转
const toUpdate = (index, row) => {
  router.push(`/updateDrug/${row.id}`);
};

// 初始化生命周期函数
onMounted(() => {
  initDrug(1, 10, '');
  
  // 监听表格容器的尺寸变化
  nextTick(() => {
    if (tableRef.value) {
      observe(tableRef.value.$el);
    }
  });
});

onUnmounted(() => {
  // 清理资源
  disconnect();
});
</script>

<style scoped>
/* 响应式调整 */
@media (max-width: 768px) {
  .el-input {
    width: 100% !important;
  }
}
</style>