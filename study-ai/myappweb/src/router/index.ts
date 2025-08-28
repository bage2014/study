import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/Login.vue'
import MapTrajectoryView from '../views/MapTrajectory.vue'
import TvListView from '../views/TvList.vue'
import HomeView from '../views/Home.vue'
import AppListView from '../views/AppList.vue'
import TvPlayerView from '../views/TvPlayer.vue'
import MessagesView from '../views/MessagesView.vue'

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
      meta: { guest: true }
    },
    { 
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    { 
      path: '/map-trajectory',
      name: 'mapTrajectory',
      component: MapTrajectoryView,
      meta: { requiresAuth: true }
    },
    { 
      path: '/tv-list',
      name: 'tvList',
      component: TvListView,
      meta: { requiresAuth: true }
    },
    { 
      path: '/app-list',
      name: 'appList',
      component: AppListView,
      meta: { requiresAuth: true }
    },
    { 
      path: '/tv-player',
      name: 'tvPlayer',
      component: TvPlayerView,
      meta: { requiresAuth: true }
    },
    { 
      path: '/messages',
      name: 'messages',
      component: MessagesView,
      meta: { requiresAuth: true }
    }
  ]
})

router.beforeEach((to, _from, next) => {
  const isLoggedIn = !!localStorage.getItem('token')
  
  if (to.meta.guest && isLoggedIn) {
    return next({ name: 'home' })
  }
  
  if (to.meta.requiresAuth && !isLoggedIn) {
    return next({ name: 'login', query: { redirect: to.fullPath } })
  }
  
  next()
})

export default router