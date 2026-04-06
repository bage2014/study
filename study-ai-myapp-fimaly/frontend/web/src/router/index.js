import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import FamilyManagement from '../views/FamilyManagement.vue'
import FamilyTree from '../views/FamilyTree.vue'
import Members from '../views/Members.vue'
import MemberDetail from '../views/MemberDetail.vue'
import Events from '../views/Events.vue'
import Media from '../views/Media.vue'
import Settings from '../views/Settings.vue'
import Relationships from '../views/Relationships.vue'
import MemberSearch from '../views/MemberSearch.vue'

const routes = [
  {
    path: '/',
    redirect: (to) => {
      const token = localStorage.getItem('token')
      return token ? '/home' : '/login'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresGuest: true }
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/family-management',
    name: 'FamilyManagement',
    component: FamilyManagement,
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
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: { requiresAuth: true }
  },
  {
    path: '/relationships',
    name: 'Relationships',
    component: Relationships,
    meta: { requiresAuth: true }
  },
  {
    path: '/member-search',
    name: 'MemberSearch',
    component: MemberSearch,
    meta: { requiresAuth: true }
  },
  // 404 page
  {
    path: '/:pathMatch(.*)*',
    redirect: '/home'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresGuest = to.matched.some(record => record.meta.requiresGuest)
  const token = localStorage.getItem('token')
  
  if (requiresAuth && !token) {
    next('/login')
  } else if (requiresGuest && token) {
    next('/home')
  } else {
    next()
  }
})

export default router