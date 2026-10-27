import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/components/pages/HomePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/samples',
      name: 'samples',
      component: () => import('@/components/pages/SamplesPage.vue'),
    },
    {
      path: '/users',
      name: 'users',
      component: () => import('@/components/pages/UsersPage.vue'),
    },
  ],
})

export default router
