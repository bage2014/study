import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import { computed } from 'vue'

export function useAuth() {
  const authStore = useAuthStore()
  const { isAuthenticated, user, token } = storeToRefs(authStore)
  const { login, logout } = authStore
  
  const hasPermission = (permission) => {
    return computed(() => {
      if (!isAuthenticated.value) return false
      return user.value.permissions?.includes(permission) || false
    })
  }
  
  return {
    isAuthenticated,
    user,
    token,
    login,
    logout,
    hasPermission
  }
}