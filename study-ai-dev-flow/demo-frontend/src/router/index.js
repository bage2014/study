import { createRouter, createWebHistory } from 'vue-router'
import ProductList from '../views/ProductList.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: ProductList }
  ]
})