import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LogsView from '../views/LogsView.vue'
import ReplayView from '../views/ReplayView.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/logs', component: LogsView },
    { path: '/replay', component: ReplayView }
  ]
})