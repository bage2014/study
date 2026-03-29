import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import FamilyTree from '../views/FamilyTree.vue'
import Members from '../views/Members.vue'
import MemberDetail from '../views/MemberDetail.vue'
import Events from '../views/Events.vue'
import Media from '../views/Media.vue'
import FamilyManagement from '../views/FamilyManagement.vue'
import Settings from '../views/Settings.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/family-tree',
    name: 'FamilyTree',
    component: FamilyTree,
    meta: { requiresAuth: true }
  },
  {
    path: '/members',
    name: 'Members',
    component: Members,
    meta: { requiresAuth: true }
  },
  {
    path: '/member/:id',
    name: 'MemberDetail',
    component: MemberDetail,
    meta: { requiresAuth: true }
  },
  {
    path: '/events',
    name: 'Events',
    component: Events,
    meta: { requiresAuth: true }
  },
  {
    path: '/media',
    name: 'Media',
    component: Media,
    meta: { requiresAuth: true }
  },
  {
    path: '/family-management',
    name: 'FamilyManagement',
    component: FamilyManagement,
    meta: { requiresAuth: true }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const token = localStorage.getItem('token')
  
  if (requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router