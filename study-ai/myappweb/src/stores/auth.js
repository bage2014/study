import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const refreshToken = ref(localStorage.getItem('refreshToken') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
  const tokenExpireTime = ref(localStorage.getItem('tokenExpireTime') || '')
  const refreshTokenExpireTime = ref(localStorage.getItem('refreshTokenExpireTime') || '')
  
  const isAuthenticated = computed(() => !!token.value)
  
  function login(userData, userToken) {
    token.value = userToken.token
    refreshToken.value = userToken.refreshToken
    user.value = userData
    tokenExpireTime.value = userToken.tokenExpireTime
    refreshTokenExpireTime.value = userToken.refreshTokenExpireTime
    
    localStorage.setItem('token', userToken.token)
    localStorage.setItem('refreshToken', userToken.refreshToken)
    localStorage.setItem('user', JSON.stringify(userData))
    localStorage.setItem('tokenExpireTime', userToken.tokenExpireTime)
    localStorage.setItem('refreshTokenExpireTime', userToken.refreshTokenExpireTime)
  }
  
  function logout() {
    token.value = ''
    refreshToken.value = ''
    user.value = {}
    tokenExpireTime.value = ''
    refreshTokenExpireTime.value = ''
    
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    localStorage.removeItem('tokenExpireTime')
    localStorage.removeItem('refreshTokenExpireTime')
  }
  
  return { 
    token, 
    refreshToken, 
    user, 
    tokenExpireTime, 
    refreshTokenExpireTime,
    isAuthenticated, 
    login, 
    logout 
  }
})