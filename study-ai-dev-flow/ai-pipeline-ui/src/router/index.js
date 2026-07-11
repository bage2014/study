import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import PipelineList from '../views/PipelineList.vue'
import PipelineDetail from '../views/PipelineDetail.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/pipelines', name: 'PipelineList', component: PipelineList },
  { path: '/pipelines/:id', name: 'PipelineDetail', component: PipelineDetail }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
