import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/analyze' },
  {
    path: '/analyze',
    name: 'Analyze',
    component: () => import('@/views/Analyze.vue'),
    meta: { title: 'RCA 分析' }
  },
  {
    path: '/history',
    name: 'History',
    component: () => import('@/views/History.vue'),
    meta: { title: '分析历史' }
  },
  {
    path: '/recording',
    name: 'Recording',
    component: () => import('@/views/Recording.vue'),
    meta: { title: '录制明细' }
  },
  {
    path: '/replay',
    name: 'Replay',
    component: () => import('@/views/Replay.vue'),
    meta: { title: '回放对比' }
  },
  {
    path: '/health',
    name: 'Health',
    component: () => import('@/views/Health.vue'),
    meta: { title: '服务健康' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.afterEach((to) => {
  document.title = to.meta?.title ? `${to.meta.title} · RCA Agent` : 'RCA Agent'
})

export default router
