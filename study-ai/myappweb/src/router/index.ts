import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

import VideoPlayer from '../pages/VideoPlayer.vue';
import VideoList from '../pages/VideoList.vue';

const routes: Array<RouteRecordRaw> = [
  { 
    path: '/school-list',
    name: 'SchoolList',
    component: () => import('@/pages/SchoolList.vue')
  },
  { 
    path: '/video-player',
    name: 'VideoPlayer',
    component: VideoPlayer
  },
  { 
    path: '/video-list',
    name: 'VideoList',
    component: VideoList
  },
  { 
    path: '/trajectory-checkpoint',
    name: 'TrajectoryCheckpoint',
    component: () => import('@/pages/TrajectoryCheckpoint.vue')
  }
];

const router = createRouter({ 
  history: createWebHistory(),
  routes
});

export default router;