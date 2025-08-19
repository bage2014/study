import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/Login.vue'
import MapTrajectoryView from '../views/MapTrajectory.vue'
import TvListView from '../views/TvList.vue'
import HomeView from '../views/Home.vue'
import AppListView from '../views/AppList.vue' // 导入APP列表组件

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login' // 未登录时重定向到登录页
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: {
        guest: true
      }
    },
    // 添加首页路由
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: {
        requiresAuth: true // 受保护路由
      }
    },
    {
      path: '/map-trajectory',
      name: 'mapTrajectory',
      component: MapTrajectoryView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/tv-list',
      name: 'tvList',
      component: TvListView,
      meta: {
        requiresAuth: true
      }
    },
    // 添加APP列表路由
    {
      path: '/app-list',
      name: 'appList',
      component: AppListView,
      meta: {
        requiresAuth: true
      }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 检查是否登录
  const isLoggedIn = !!localStorage.getItem('token')

  // 如果是登录页面且已登录，重定向到首页
  if (to.meta.guest && isLoggedIn) {
    return next({ name: 'home' })
  }

  // 如果是受保护路由且未登录，重定向到登录页
  if (to.meta.requiresAuth && !isLoggedIn) {
    return next({ name: 'login', query: { redirect: to.fullPath } })
  }

  next()
})

export default router