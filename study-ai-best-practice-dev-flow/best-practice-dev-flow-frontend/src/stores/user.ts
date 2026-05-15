import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userAPI, type UserDTO, type LoginRequest, type RegisterRequest } from '@/api'

export const useUserStore = defineStore('user', () => {
  const user = ref<UserDTO | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))

  const isLoggedIn = computed(() => !!token.value && !!user.value)

  async function login(data: LoginRequest) {
    const result: { token: string; user: UserDTO } = await userAPI.login(data) as any
    token.value = result.token
    user.value = result.user
    localStorage.setItem('token', result.token)
    localStorage.setItem('user', JSON.stringify(result.user))
  }

  async function register(data: RegisterRequest) {
    await userAPI.register(data)
  }

  async function logout() {
    try {
      await userAPI.logout()
    } catch (e) {
      console.error('Logout error:', e)
    } finally {
      token.value = null
      user.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }

  async function loadUser() {
    if (token.value && !user.value) {
      try {
        user.value = await userAPI.getCurrentUser() as unknown as UserDTO
      } catch (e) {
        console.error('Load user error:', e)
        logout()
      }
    }
  }

  async function updateProfile(data: Partial<UserDTO>) {
    user.value = await userAPI.updateProfile(data) as unknown as UserDTO
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  return {
    user,
    token,
    isLoggedIn,
    login,
    register,
    logout,
    loadUser,
    updateProfile
  }
})