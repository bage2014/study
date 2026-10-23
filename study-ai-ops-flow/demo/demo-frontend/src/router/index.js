import { createRouter, createWebHistory } from 'vue-router'
import BookList from '../views/BookList.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: BookList }
  ]
})
