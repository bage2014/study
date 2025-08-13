import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/Login.vue'
import MapTrajectoryView from '../views/MapTrajectory.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: {
        guest: true
      }
    },
    {
      path: '/map-trajectory',
      name: 'mapTrajectory',
      component: MapTrajectoryView,
      meta: {
        requiresAuth: true  // 添加受保护路由属性
      }
    }
  ]
})

// 添加路由守卫
router.beforeEach((to, from, next) => {
  // 检查是否登录
  const isLoggedIn = !!localStorage.getItem('token')

  // 如果是登录页面且已登录，重定向到首页
  if (to.meta.guest && isLoggedIn) {
    return next({ name: '/' })
  }

  // 如果是受保护路由且未登录，重定向到登录页
  if (to.meta.requiresAuth && !isLoggedIn) {
    return next({ name: 'login', query: { redirect: to.fullPath } })
  }

  next()
})

export default router