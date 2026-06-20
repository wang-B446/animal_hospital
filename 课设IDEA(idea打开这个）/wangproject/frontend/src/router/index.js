import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/dashboard' },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { title: '数据仪表盘', requiresAuth: true }
  },
  {
    path: '/animal',
    name: 'Animal',
    component: () => import('../views/Animal.vue'),
    meta: { title: '动物管理', requiresAuth: true }
  },
  {
    path: '/owner',
    name: 'Owner',
    component: () => import('../views/Owner.vue'),
    meta: { title: '主人管理', requiresAuth: true }
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: () => import('../views/Doctor.vue'),
    meta: { title: '医生管理', requiresAuth: true }
  },
  {
    path: '/appointment',
    name: 'Appointment',
    component: () => import('../views/Appointment.vue'),
    meta: { title: '预约管理', requiresAuth: true }
  },
  {
    path: '/medical-record',
    name: 'MedicalRecord',
    component: () => import('../views/MedicalRecord.vue'),
    meta: { title: '就诊记录', requiresAuth: true }
  },
  {
    path: '/ai-advice',
    name: 'AiAdvice',
    component: () => import('../views/AiAdvice.vue'),
    meta: { title: 'AI小建议', requiresAuth: true }
  },
  {
    path: '/log',
    name: 'Log',
    component: () => import('../views/Log.vue'),
    meta: { title: '操作日志', requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 路由守卫：未登录跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
