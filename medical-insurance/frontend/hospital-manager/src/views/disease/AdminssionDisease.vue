<template>
  <div class="diagnosis-container">
    <!-- 患者信息头部 -->
    <div class="header-section">
      <div class="header-icon">
        <el-icon><DocumentChecked /></el-icon>
      </div>
      <div class="patient-info">
        <div class="patient-name">入院诊断患者: {{diseasePatient.realName}}</div>
        <div class="patient-id">患者ID: {{ diseasePatient.id }}</div>
      </div>
    </div>
    
    <!-- 当前诊断信息 -->
    <div class="current-diagnosis">
      <div class="diagnosis-title">
        <el-icon><Warning /></el-icon>
        入院诊断疾病：
      </div>
      <div class="diagnosis-content">{{ showDisease || '尚未添加诊断' }}</div>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-input 
        v-model="inputName" 
        placeholder="请输入疾病名称" 
        clearable
        @keyup.enter="searchName"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="searchName" icon="Search">搜索</el-button>
    </div>
    
    <!-- 疾病列表区域 -->
    <div class="table-container">
      <div class="table-header">
        <div>疾病列表</div>
        <div class="total-count">共 {{ total }} 条记录</div>
      </div>
      
      <!-- 疾病数据表格，展示疾病信息 -->
      <el-table 
        :data="dataList" 
        stripe 
        v-loading="loading" 
        element-loading-text="数据加载中..."
      >
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="diseaseName" label="疾病名称" min-width="180" />
        <el-table-column prop="diseaseCode" label="疾病编码" width="150" />
        <el-table-column prop="diseaseICD" label="国际ICD编码" width="200" />
        <el-table-column prop="diseaseCategory" label="疾病类型" width="150" align="center">
          <!-- 标签颜色由getDiseaseTagType方法决定 -->
          <template #default="scope">
            <el-tag :type="getDiseaseTagType(scope.row.diseaseCategory)">
              {{ scope.row.diseaseCategory }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <!-- 增加诊断按钮，点击后调用addDisease方法，传入当前行数据 -->
            <el-button 
              type="primary" 
              size="small"
              @click="addDisease(scope.row)"
            >
              增加诊断
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 空状态 -->
      <div v-if="dataList.length === 0 && !loading" class="empty-table">
        <el-icon class="empty-icon"><FolderOpened /></el-icon>
        <div class="empty-text">未找到相关疾病数据</div>
        <div class="empty-hint">请尝试其他搜索关键词</div>
      </div>
      
      <div class="table-footer">
        数据更新时间: {{ new Date().toLocaleString() }}
      </div>
    </div>
    
    <!-- 分页器 -->
    <div class="pagination-container">
      <el-pagination 
        background 
        :layout="paginationLayout"
        :total="total"     
        :page-size="pageSize"
        v-model:currentPage="pageNumberNow" 
        @current-change="pageChangeFunc"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { ElMessageBox, ElMessage } from 'element-plus'
import { getDiseaseListPage, addAdmissionDisease } from '@/api/disease';// 引入后端接口方法

// 患者信息初始为空对象
const diseasePatient = ref({
  id: '',
  realName: ''
});

// 搜索相关
const inputName = ref('');// 搜索框输入内容
const loading = ref(false); // 加载状态

// 分页相关
const total = ref(0);// 总数据条数，初始值为0
const pageNumberNow = ref(1);// 当前页码
const pageSize = ref(10);// 每页显示条数

// 分页器布局，宽屏和窄屏显示不同
const paginationLayout = computed(() => {
  return window.innerWidth < 768 ? 'prev, pager, next' : 'prev, pager, next, jumper';
});

// 数据列表
const dataList = ref([]);
const showDisease = ref('');

// 获取疾病标签类型（用于标签颜色）
const getDiseaseTagType = (category) => {
  const map = {
    '传染性疾病': 'danger',
    '慢性疾病': 'success',
    '一般疾病': 'primary'
  };
  // 根据疾病类型返回标签颜色类型
  return map[category] || '';
};

// 搜索疾病
const searchName = () => {
  // 重新请求第一页数据，带上搜索关键词
  initData(1, pageSize.value, inputName.value);
};

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
  getDiseaseListPage({
    pageNumber: currentPage,
    pageSize: currentSize,
    diseaseName: searchKey
  }).then((response) => {
    const result = response.data;
    console.log("后端响应数据：", result);

    if (!result) {
      ElMessage.error("后端返回数据格式错误");
      return;
    }
    if (result.code === 200 || result.code === "200") {
      // 绑定表格数据（修复赋值错误）
      dataList.value = Array.isArray(result.data.records) ? result.data.records : [];
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

// 分页变化
const pageChangeFunc = (currentPage) => {
  // 请求对应页码的数据
  initData(currentPage, pageSize.value, inputName.value);
  pageNumberNow.value = currentPage; // 更新当前页码
};

// 每页条数变化
const sizeChangeFunc = (size) => {
  pageSize.value = size;
  initData(1, size, inputName.value); // 切换每页条数后重新加载第一页
};

// 添加诊断方法，点击“增加诊断”按钮时触发
const addDisease = async (row) => {
  try {
    // 弹出确认框，提示用户是否确认添加该疾病为入院诊断
    await ElMessageBox.confirm(
      `确定将"${row.diseaseName}"添加为入院诊断吗？`,
      '确认诊断',
      {
        confirmButtonText: '确认添加',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    // 调用后端接口，传递患者ID和疾病ID
    const response = await addAdmissionDisease(diseasePatient.value.id,row.id);
    // 更新当前诊断疾病名称
    showDisease.value = row.diseaseName;
    // 添加成功弹出提示（后端返回消息在data或message中，根据实际接口调整）
    ElMessage.success(response.data || '添加诊断成功');
  } catch (error) {
    // 如果不是用户取消操作，则弹出错误提示
    if (error !== 'cancel') {
      ElMessage.error('添加诊断失败: ' + (error.message || '操作异常'));
    }
  }
};

// 初始化
onMounted(() => {
  // 从sessionStorage获取患者信息
  const patient = sessionStorage.getItem("diseasePatient");//diseasePatient信息由patientD.vue 文件中傳遞
  if (patient) {
    diseasePatient.value = JSON.parse(patient);//把字符串转换成对象
  }
  // 初始化第一页数据（修正原方法名拼写错误initdisease -> initData）
  initData(1, pageSize.value, '');
});
</script>

<style scoped>
/* 整个诊断页面的容器样式，设置宽度、背景、圆角、阴影和内边距 */
.diagnosis-container {
  width: 95%;
  max-width: 1200px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  padding: 30px;
  margin: 20px auto;
}

/* 顶部患者信息区域，横向排列，底部有分割线 */
.header-section {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #eaeff5;
}

/* 患者头像图标区域，蓝色背景，居中显示图标 */
.header-icon {
  background: #409eff;
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

/* 图标本身的颜色和大小 */
.header-icon .el-icon {
  color: white;
  font-size: 24px;
}

/* 患者信息内容区域，自动占满剩余空间 */
.patient-info {
  flex: 1;
}

/* 患者姓名样式，加粗加大字体 */
.patient-name {
  font-size: 22px;
  font-weight: 600;
  color: #1c2e4a;
  margin-bottom: 5px;
}

/* 患者ID样式，字体较小且颜色较淡 */
.patient-id {
  color: #5a6c85;
  font-size: 14px;
}

/* 当前诊断信息区域，浅蓝背景，左侧有蓝色竖线 */
.current-diagnosis {
  background: #f0f7ff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 25px;
  border-left: 4px solid #409eff;
}

/* 诊断标题样式，字体加粗，颜色较深，图标和文字有间距 */
.diagnosis-title {
  font-weight: 600;
  color: #5a6c85;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 诊断内容样式，字体较大，蓝色显示，左侧有内边距 */
.diagnosis-content {
  font-size: 18px;
  color: #409eff;
  font-weight: 500;
  padding-left: 28px;
}

/* 搜索区域样式，横向排列，背景浅灰，圆角和内边距 */
.search-section {
  display: flex;
  align-items: center;
  gap: 15px;
  background: #f8fafc;
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 25px;
}

/* 疾病列表表格外层容器，边框和圆角 */
.table-container {
  border: 1px solid #ebeef5;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 25px;
}

/* 表格头部样式，背景浅灰，左右分布，字体加粗 */
.table-header {
  background: #f8fafc;
  padding: 15px 20px;
  font-weight: 600;
  color: #2c3e50;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 总记录数样式，字体较小，颜色较淡 */
.total-count {
  color: #909399;
  font-size: 14px;
}

/* 表格空状态样式，居中显示提示内容 */
.empty-table {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

/* 空状态图标样式，图标很大且颜色很淡 */
.empty-icon {
  font-size: 60px;
  color: #dcdfe6;
  margin-bottom: 15px;
}

/* 空状态主提示文字样式 */
.empty-text {
  font-size: 16px;
  margin-bottom: 10px;
}

/* 空状态辅助提示文字样式 */
.empty-hint {
  color: #c0c4cc;
}

/* 表格底部更新时间样式，右对齐，字体较小 */
.table-footer {
  color: #909399;
  font-size: 14px;
  padding: 10px 20px;
  background: #f8fafc;
  border-top: 1px solid #ebeef5;
  text-align: right;
}

/* 分页器区域样式，居中显示分页器 */
.pagination-container {
  display: flex;
  justify-content: center;
  padding: 15px 0;
}

/* 响应式样式，屏幕宽度小于768px时的布局调整 */
@media (max-width: 768px) {
  /* 搜索区域改为纵向排列 */
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }
  /* 头部区域改为纵向排列并居中 */
  .header-section {
    flex-direction: column;
    text-align: center;
  }
  /* 图标居中显示并增加下边距 */
  .header-icon {
    margin: 0 auto 15px;
  }
  /* 诊断内容左侧不再有内边距 */
  .diagnosis-content {
    padding-left: 0;
  }
}
</style>