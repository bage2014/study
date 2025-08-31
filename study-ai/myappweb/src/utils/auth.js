import { useAuthStore } from '@/stores/auth'

export function setAuthToken(token, refreshToken = '') {
  if (token) {
    localStorage.setItem('token', token)
    if (refreshToken) {
      localStorage.setItem('refreshToken', refreshToken)
    }
  } else {
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }
}

export function getAuthToken() {
  return localStorage.getItem('token')
}

export function getRefreshToken() {
  return localStorage.getItem('refreshToken')
}

export function removeAuthToken() {
  localStorage.removeItem('token')
  localStorage.removeItem('refreshToken')
  localStorage.removeItem('tokenExpireTime')
  localStorage.removeItem('refreshTokenExpireTime')
}

export function parseJwt(token) {
  try {
    return JSON.parse(atob(token.split('.')[1]))
  } catch (e) {
    return null
  }
}

export function isTokenExpired(token) {
  const decoded = parseJwt(token)
  if (!decoded) return true
  
  const now = Date.now() / 1000
  return decoded.exp < now
}

export function validateAuth() {
  const token = getAuthToken()
  const tokenExpireTime = localStorage.getItem('tokenExpireTime')
  
  if (!token || !tokenExpireTime) {
    const authStore = useAuthStore()
    authStore.logout()
    return false
  }
  
  const expireDate = new Date(tokenExpireTime)
  const now = new Date()
  
  if (now >= expireDate) {
    const authStore = useAuthStore()
    authStore.logout()
    return false
  }
  
  return true
}