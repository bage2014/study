import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Family from '@/views/Family.vue'
import Members from '@/views/Members.vue'
import Relationships from '@/views/Relationships.vue'
import FamilyTree from '@/views/FamilyTree.vue'
import Events from '@/views/Events.vue'
import Media from '@/views/Media.vue'
import AI from '@/views/AI.vue'
import TrackPoints from '@/views/TrackPoints.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/family',
    name: 'Family',
    component: Family,
    meta: { requiresAuth: true }
  },
  {
    path: '/members',
    name: 'Members',
    component: Members,
    meta: { requiresAuth: true }
  },
  {
    path: '/relationships',
    name: 'Relationships',
    component: Relationships,
    meta: { requiresAuth: true }
  },
  {
    path: '/family-tree',
    name: 'FamilyTree',
    component: FamilyTree,
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
    path: '/ai',
    name: 'AI',
    component: AI,
    meta: { requiresAuth: true }
  },
  {
    path: '/track',
    name: 'TrackPoints',
    component: TrackPoints,
    meta: { requiresAuth: true }
  },
  {
    path: '/',
    redirect: '/family'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')
  const requiresAuth = to.meta.requiresAuth

  if (requiresAuth && !token) {
    next('/login')
    return
  }

  if (!requiresAuth && token && to.path === '/login') {
    next('/family')
    return
  }

  next()
})

export default router