import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import AddressList from '../views/AddressList.vue'
import SearchHistory from '../views/SearchHistory.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/addresses',
    name: 'AddressList',
    component: AddressList
  },
  {
    path: '/history',
    name: 'SearchHistory',
    component: SearchHistory
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router