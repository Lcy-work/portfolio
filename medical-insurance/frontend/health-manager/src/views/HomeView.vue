<template>
  <div class="medical-insurance-container">
    <!-- 标题区域 -->
    <div class="title-section">
      <h2 class="main-title">医疗保险报销的三大目录</h2>
      <div class="divider"></div>
    </div>
    
    <!-- 内容区域 -->
    <div class="content-section">
      <!-- 药品目录 -->
      <div class="catalog-item">
        <h3 class="catalog-title">
          <el-icon class="title-icon"><FirstAidKit /></el-icon>
          基本医疗保险药品目录
        </h3>
        <div class="catalog-content">
          <p class="content-text">
            基本医疗保险药品目录是医疗保险、工伤保险和生育保险基金支付参保人员药品费用的政策依据及标准，而医保药品目录内的药品又可以分为甲、乙两类。
          </p>
          
          <div class="drug-category">
            <div class="category-card">
              <div class="category-header">
                <span class="category-badge type-a">甲类药品</span>
              </div>
              <div class="category-content">
                是全国基本统一的，临床治疗必需的，使用广泛效果好的，同类药品中价格相对较低的药品。这类药品的费用可以全部纳入医保基金的报销范围，并按照规定的比例进行报销。
              </div>
            </div>
            
            <div class="category-card">
              <div class="category-header">
                <span class="category-badge type-b">乙类药品</span>
              </div>
              <div class="category-content">
                也是由国家规定的，但各省市可以自行调整，其中的药品相比甲类同类型的药品，价格会偏高。参保人使用这类药品时，需要自行支付一定比例的费用，而剩下的部分才会纳入报销范围，再按规定比例进行报销。
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 诊疗项目目录 -->
      <div class="catalog-item">
        <h3 class="catalog-title">
          <el-icon class="title-icon"><FirstAidKit /></el-icon>
          诊疗项目目录
        </h3>
        <div class="catalog-content">
          <p class="content-text">
            诊疗项目目录是临床诊疗必需的、安全有效的、费用适宜的且由物价部门制定了收费标准的诊疗项目，是医院在救治时所使用的临床检查、治疗等项目，比如治疗费、检查费、手术费等。
          </p>
          
          <div class="treatment-categories">
            <ul class="category-list">
              <li class="list-item">
                <span class="list-badge">全额统筹项目</span>
                <span class="list-desc">全部计入统筹支付的项目</span>
              </li>
              <li class="list-item">
                <span class="list-badge">部分统筹项目</span>
                <span class="list-desc">需要个人先支付一定比例后，再纳入统筹范围的项目</span>
              </li>
              <li class="list-item">
                <span class="list-badge">范围外项目</span>
                <span class="list-desc">完全需要自行支付的项目</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
      
      <!-- 医疗服务设施目录 -->
      <div class="catalog-item">
        <h3 class="catalog-title">
          <el-icon class="title-icon"><OfficeBuilding /></el-icon>
          医疗服务设施目录
        </h3>
        <div class="catalog-content">
          <p class="content-text">
            医疗服务设施目录是由定点医院提供的，是在接受诊断、治疗和护理过程中必须的服务设施，包括急诊留观床位费、住院床位费等等。不同的省市也有自己不同的支付范围，范围以内的可以按照标准进行报销，超过标准则需要自付。
          </p>
          
          <div class="facility-example">
            <el-card class="example-card">
              <div class="example-title">常见可报销服务设施</div>
              <div class="example-content">
                <el-tag class="facility-tag">急诊留观床位费</el-tag>
                <el-tag class="facility-tag">普通住院床位费</el-tag>
                <el-tag class="facility-tag">门诊煎药费</el-tag>
                <el-tag class="facility-tag">住院空调费（基础标准）</el-tag>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {  FirstAidKit,  OfficeBuilding } from "@element-plus/icons-vue";
import { ElIcon, ElCard, ElTag } from "element-plus";

export default {
  components: {
    ElIcon,
    FirstAidKit,
    FirstAidKit,
    OfficeBuilding,
    ElCard,
    ElTag
  }
};
</script>

<style scoped>
.medical-insurance-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
  background-color: #f9fafb;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* 标题样式 */
.title-section {
  text-align: center;
  margin-bottom: 40px;
}

.main-title {
  color: #1f2329;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 15px;
  position: relative;
  display: inline-block;
}

.main-title::after {
  content: "";
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 3px;
  background-color: #1677ff;
  border-radius: 3px;
}

.divider {
  height: 1px;
  background-color: #e5e7eb;
  margin-top: 20px;
}

/* 内容区域样式 */
.content-section {
  line-height: 1.8;
  color: #4e5969;
  font-size: 15px;
}

.catalog-item {
  margin-bottom: 40px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.catalog-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.catalog-title {
  color: #1f2329;
  font-size: 19px;
  font-weight: 500;
  margin-bottom: 18px;
  display: flex;
  align-items: center;
}

.title-icon {
  color: #1677ff;
  margin-right: 10px;
  font-size: 20px;
}

.catalog-content {
  padding-left: 10px;
}

.content-text {
  margin-bottom: 20px;
  text-align: justify;
}

/* 药品目录样式 */
.drug-category {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.category-card {
  flex: 1;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
  transition: all 0.2s ease;
}

.category-card:hover {
  border-color: #1677ff;
  box-shadow: 0 2px 8px rgba(22, 119, 255, 0.15);
}

.category-header {
  background-color: #f5f7fa;
  padding: 12px 15px;
  border-bottom: 1px solid #e5e7eb;
}

.category-badge {
  display: inline-block;
  font-weight: 500;
  color: #fff;
  padding: 3px 10px;
  border-radius: 4px;
  font-size: 14px;
}

.type-a {
  background-color: #52c41a;
}

.type-b {
  background-color: #faad14;
}

.category-content {
  padding: 15px;
  font-size: 14px;
}

/* 诊疗项目目录样式 */
.treatment-categories {
  margin-top: 15px;
}

.category-list {
  list-style: none;
  padding: 0;
}

.list-item {
  padding: 12px 15px;
  border-bottom: 1px dashed #e5e7eb;
  display: flex;
  align-items: flex-start;
  transition: background-color 0.2s ease;
}

.list-item:last-child {
  border-bottom: none;
}

.list-item:hover {
  background-color: #f9fafb;
}

.list-badge {
  display: inline-block;
  min-width: 120px;
  font-weight: 500;
  color: #1677ff;
}

.list-desc {
  flex: 1;
}

/* 医疗服务设施目录样式 */
.facility-example {
  margin-top: 20px;
}

.example-card {
  border-radius: 6px;
  border: 1px solid #e5e7eb;
}

.example-title {
  font-weight: 500;
  color: #1f2329;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.example-content {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.facility-tag {
  background-color: #e6f7ff;
  color: #1890ff;
  border-color: #91d5ff;
}

/* 响应式样式 */
@media (max-width: 768px) {
  .drug-category {
    flex-direction: column;
  }
  
  .medical-insurance-container {
    padding: 20px 15px;
  }
  
  .main-title {
    font-size: 22px;
  }
  
  .catalog-title {
    font-size: 17px;
  }
}
</style>