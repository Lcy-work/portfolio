import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/home',
    name: '功能主界面',
    component: () => import('../layout/index.vue'),
	children:[
		{
			path:"",
			name:"入院登记",
			component:()=>import('../views/HomeView.vue')
		},
		{
			path:"/patientdisease",
			name:"诊断患者选择",
			component:()=>import('../views/disease/patientD.vue')
		},
		{ 
			path:"/adminssiondisease",
			name:"入院诊断",
			component:()=>import('../views/disease/AdminssionDisease.vue')
		},
		{
			path:"/maindisease",
			name:"主要诊断",
			component:()=>import('../views/disease/MainDisease.vue')
		},
		{
			path:"/otherdisease",
			name:"其他诊断",
			component:()=>import('../views/disease/OtherDisease.vue')
		},
		{
			path:"/patientorder",
			name:"医嘱患者选择",
			component:()=>import('../views/order/patientO.vue')
		},
		{
			path:"/drugorder",
			name:"药品处方医嘱",
			component:()=>import('../views/order/DrugOrder.vue')
		},
		{
			path:"/drugorderadd",
			name:"增加药品处方医嘱",
			component:()=>import('../views/order/DrugOrderAdd.vue')
		},
		{
			path:"/diagnosistreatmentorder",
			name:"诊疗项目医嘱",
			component:()=>import('../views/order/DiagnosisTreatmentOrder.vue')
		},
		{
			path:"/diagnosistreatmentorderadd",
			name:"增加诊疗项目医嘱",
			component:()=>import('../views/order/DiagnosisTreatmentOrderAdd.vue')
		},
		{
			path:"/medicalserviceorder",
			name:"医疗服务医嘱",
			component:()=>import('../views/order/MedicalServiceOrder.vue')
		},
		{
			path:"/medicalserviceorderadd",
			name:"增加医疗服务医嘱",
			component:()=>import('../views/order/MedicalServiceOrderAdd.vue')
		}
	]
  },
  {
    path: '/',
    name: '登录',
    component: () => import('../views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
