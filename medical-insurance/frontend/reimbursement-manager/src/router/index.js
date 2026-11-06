import { createRouter, createWebHashHistory } from 'vue-router'
import Index from '../layout/index.vue'

const routes = [
  {
    path: '/home',
    name: 'home',
    component: Index,
	children:[
		{
			path:'/home',
			name:"参保人员信息管理",
			component:() => import('../views/PatientRegistration.vue')
		},
		{
			path: '/patientpay',
			name: '参保人员费用查询',
			component: () => import('../views/PatientPay.vue')
		},
		{
			path: '/patientreimbursement',
			name: '参保人员费用报销',
			component: () => import('../views/PatientReimbursement.vue')
		},
		{
			path: '/patientreimbursementinfo/:id',
			name: '参保人员费用报销详情',
			component: () => import('../views/PatientReimbursementInfo.vue')
		},
		{
			path: '/patientreimbursementdo/:id',
			name: '参保人员费用报销给付',
			component: () => import('../views/PatientReimbursementDo.vue')
		},		
	],	
  },
  {
	  path:"/",
	  name:"登录",
	  component:()=>import('../views/Login.vue')
  }  
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
