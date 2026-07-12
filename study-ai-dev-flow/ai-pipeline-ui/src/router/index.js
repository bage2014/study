import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '../components/AppLayout.vue'
import Home from '../views/Home.vue'
import PipelineList from '../views/PipelineList.vue'
import PipelineDetail from '../views/PipelineDetail.vue'
import ProjectManagement from '../views/ProjectManagement.vue'
import ProjectDetail from '../views/ProjectDetail.vue'
import DesignView from '../views/DesignView.vue'
import CodingView from '../views/CodingView.vue'
import PreviewView from '../views/PreviewView.vue'
import BuildView from '../views/BuildView.vue'
import DeployView from '../views/DeployView.vue'

const routes = [
  {
    path: '/',
    component: AppLayout,
    children: [
      { path: '', name: 'Home', component: Home },
      { path: 'projects', name: 'ProjectManagement', component: ProjectManagement },
      { path: 'projects/:id', name: 'ProjectDetail', component: ProjectDetail },
      { path: 'pipelines', name: 'PipelineList', component: PipelineList },
      { path: 'pipelines/:id', name: 'PipelineDetail', component: PipelineDetail },
      { path: 'design', name: 'DesignView', component: DesignView },
      { path: 'coding', name: 'CodingView', component: CodingView },
      { path: 'preview', name: 'PreviewView', component: PreviewView },
      { path: 'build', name: 'BuildView', component: BuildView },
      { path: 'deploy', name: 'DeployView', component: DeployView },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
