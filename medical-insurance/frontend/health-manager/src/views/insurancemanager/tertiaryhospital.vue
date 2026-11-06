<template>
	<div>
		<el-row :gutter="10">
			<el-col :span="14">
				<el-card class="box-card" >
					<template #header>
						<el-icon><SetUp /></el-icon>
						三级医院 费用阶梯 （元）
					</template>
					<div>
						 <!-- 功能按钮 -->
						 <el-row style="margin-bottom: 10px;">
							<el-col :span="4" >
								<el-button type="primary" :icon="Plus" @click="addView">新增</el-button>
							</el-col>					 	
						 </el-row>
						 <el-table stripe :data="tableDataList" highlight-current-row border @row-click="handleRowClick">
							 <el-table-column label="起付线" prop="minPayLevel" align="center" />
							 <el-table-column label="等级线" prop="maxPayLevel" align="center" />
							 <el-table-column label="人员类别" prop="minPayLevel" width="100" align="center" >
								 <template #default="scope">
									 <el-tag v-if="scope.row.peopleType===1" effect="plain" type="info">在职人员</el-tag>
									 <el-tag v-else  type="success">退休人员</el-tag>
								 </template>
							 </el-table-column>
							 <el-table-column label="报销比例" prop="payProportion" width="100" align="center" >
								 <template #default="scope">
									 <el-tag effect="dark" type="info">{{scope.row.payProportion}} %</el-tag>									
								 </template>
							 </el-table-column>
							 <el-table-column label="状态"  width="80" align="center" >
								 <template #default="scope">
									 <el-tag v-if="scope.row.status===1" type="success">启用</el-tag>
									 <el-tag v-else  type="info">禁用</el-tag>
								 </template>
							 </el-table-column>
							 <el-table-column label="操作" align="center" width="150">
								 <template #default="scope">
									<el-button type="danger" :icon="Delete" circle plain
									@click.stop = "handleDelete(scope.row)"
									/> 
								 </template>
							 </el-table-column>
						 </el-table>
					</div>
				</el-card>
			</el-col>
			<el-col :span="10">
				<el-card class="box-card" >
					<template #header>
						<el-icon><Operation /></el-icon>
						详细信息
						<el-tag type="success" v-if="formData.minPayLevel" size="small">
							{{ formData.minPayLevel }} 至 {{ formData.maxPayLevel }} 区间报销比例
						</el-tag>
						<el-tag type="warning" v-else size="small">请点击左侧菜单列表选择</el-tag>
					</template>
					<el-form ref="dataFormRef" :model="formData" :rules="rules" label-width="80px">
						<el-form-item label="起付线" prop="minPayLevel">
							<el-input v-model="formData.minPayLevel" placeholder="人民币 元"/>
						</el-form-item>
						<el-form-item label="等级线" prop="maxPayLevel">
							<el-input v-model="formData.maxPayLevel" placeholder="人民币 元"/>
						</el-form-item>
						<el-form-item label="人员类别" prop="peopleType">
							 <el-radio-group v-model="formData.peopleType">
								 <el-radio :label="1">在职人员</el-radio>
								 <el-radio :label="0">退休人员</el-radio>
							 </el-radio-group>
						</el-form-item>
						<el-form-item label="报销比例" prop="payProportion">
							 <el-input-number v-model="formData.payProportion" :precision="0" :step="1" :max="100" :min="0" placeholder="0"/>
						</el-form-item>
						<el-form-item label="状态" prop="status">
							 <el-radio-group v-model="formData.status">
								 <el-radio :label="1">正常</el-radio>
								 <el-radio :label="0">停用</el-radio>
							 </el-radio-group>
						</el-form-item>
					</el-form>
					<div>
						 <el-button type="primary" @click="submitForm">修改</el-button>
						 <el-button>重置</el-button>
					</div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script setup>
import { ref,inject,onMounted } from "vue";
import { Delete } from "@element-plus/icons-vue"
import { ElForm,ElMessage,ElMessageBox } from "element-plus"
const axios = inject("axios");
axios.defaults.baseURL = "http://localhost:8081/hospitalreimbursement"
const formData = ref({})
const addData = ref({
	hospitalLevel:"三级医院"
})
const dataFormRef = ref(ElForm)
const rules = ref({
	minPayLevel:[{ required:true,message:"起付线不能为空",trigger:"blur"}],
	maxPayLevel:[{ required:true,message:"等级线不能为空",trigger:"blur"}],
	peopleType:[{ required:true,message:"人员类别不能为空",trigger:"blur"}],
	payProportion:[{ required:true,message:"报销比例不能为空",trigger:"blur"}],
	status:[{ required:true,message:"状态不能为空",trigger:"blur"}],
})
const tableDataList = ref([])
//初始化table数据
const initData = ()=>{
	axios.get("/listByHostpital?hospitalLevel=三级医院").then(
		(response)=>{
			tableDataList.value = response.data;
			console.log(tableDataList.value);
		}
	)
}
//点击table行
const handleRowClick = (row)=>{
	axios.get("/listById?id="+row.id).then(
		(response) =>{
			formData.value = response.data;
		}
	)
}
//点击右侧修改按钮
const submitForm = ()=>{
	dataFormRef.value.validate((isValid)=>{
		if(isValid){
			if(formData.value.id){
				axios.post("/updateById",formData.value).then(
					(response)=>{
						ElMessage.success(response.data);
						initData();
					}
				)
			}
		}
	})
}
//点击新增
const addView = ()=>{
	addData.hospitalLevel = "三级医院";
	axios.post("/add",addData).then(
		(response)=>{
			ElMessage.success(response.data);
			initData();
		}
	)
}
//点击删除
const handleDelete = (row)=>{
	ElMessageBox.confirm(
		"是否要删除",
		"删除操作",
		{
			confirmButtonText:'确认',
			concelButtonText:"取消",
			type:"warning"
		}
	).then(()=>{
		axios.get("/deleteById?id="+row.id).then(
			(response)=>{
				ElMessage.success(response.data);
				initData();
			}
		)
	})
}
//初始化生命周期
onMounted(()=>{
	initData();
})
</script>

<style>
</style>
