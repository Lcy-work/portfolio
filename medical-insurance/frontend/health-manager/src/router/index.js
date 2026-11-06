import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: '登录',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/home',
    name: '主界面',
    component: () => import('../layout/index.vue'),
    meta: {
        requiresAuth: true // 需要登录才能访问
    },
	children:[
		{
			path:'',
			name:'默认主界面',
			component: () => import('../views/HomeView.vue')
		},
		{
			path:'/drugManager',
			name:'医保药品管理',
			component: () => import('../views/datamanager/druginfo/main.vue')
		},
		{
			path:'/addDrug',
			name:'增加药品',
			component: () => import('../views/datamanager/druginfo/add.vue')
		},
		{
			path:'/updateDrug/:id',
			name:'修改药品',
			component: () => import('../views/datamanager/druginfo/update.vue')
		},
		{
			path:'/diagnosistreatment',
			name:'诊疗项目管理',
			component: () => import('../views/datamanager/diagnosistreatment/main.vue')
		},
		{
			path:'/addDiagnosisTreatment',
			name:'增加诊疗项目',
			component: () => import('../views/datamanager/diagnosistreatment/add.vue')
		},
		{
			path:'/updateDiagnosisTreatment/:id',
			name:'修改诊疗项目',
			component: () => import('../views/datamanager/diagnosistreatment/update.vue')
		},
		{
			path:'/medicalservice',
			name:'医疗服务管理',
			component: () => import('../views/datamanager/medicalservice/main.vue')
		},
		{
			path:'/addMedicalService',
			name:'增加医疗服务',
			component: () => import('../views/datamanager/medicalservice/add.vue')
		},
		{
			path:'/updateMedicalService/:id',
			name:'修改医疗服务',
			component: () => import('../views/datamanager/medicalservice/update.vue')
		},
		{
			path:'/drugreimbursement',
			name:'药品报销比例',
			component: () => import('../views/insurancemanager/drugreimbursement.vue')
		},
		{
			path:'/tertiaryhospital',
			name:'三级医院报销比例',
			component: () => import('../views/insurancemanager/thirdhospital.vue')
		},
		{
			path:'/secondaryhopsital',
			name:'二级医院报销比例',
			component: () => import('../views/insurancemanager/secondaryhopsital.vue')
		},
		{
			path:'/firstlevelhospital',
			name:'一级医院报销比例',
			component: () => import('../views/insurancemanager/firstlevelhospital.vue')
		}
	]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})



export default router
