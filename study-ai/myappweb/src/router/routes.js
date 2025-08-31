const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/profile/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/home',
    name: 'Home', 
    component: () => import('@/views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('@/views/messages/MessagesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/tv-list',
    name: 'TvList',
    component: () => import('@/views/tv/TvList.vue'), 
    meta: { requiresAuth: true }
  },
  {
    path: '/tv-player',
    name: 'TvPlayer',
    component: () => import('@/views/tv/TvPlayer.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/map-trajectory',
    name: 'MapTrajectory',
    component: () => import('@/views/map/MapTrajectory.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/app-list',
    name: 'AppList',
    component: () => import('@/views/app/AppList.vue'),
    meta: { requiresAuth: true }
  }

]

export default routes