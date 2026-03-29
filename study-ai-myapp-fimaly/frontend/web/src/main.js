import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './stores'
import axios from 'axios'
import './style.css'

// Set up axios defaults
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.headers.common['Content-Type'] = 'application/json'

// Add token to axios headers if it exists in localStorage
const token = localStorage.getItem('token')
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

// Create Vue app
const app = createApp(App)

// Use plugins
app.use(router)
app.use(pinia)

// Mount app
app.mount('#app')