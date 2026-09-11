import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/client'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(null)
  const isAuthenticated = computed(() => !!token.value)

  async function login(phone, password) {
    const response = await api.post('/api/auth/login', { phone, password })
    token.value = response.data.token
    localStorage.setItem('token', response.data.token)
    await fetchProfile()
    return response.data
  }

  async function register(phone, password, fullName) {
    const response = await api.post('/api/auth/register', { phone, password, fullName })
    return response.data
  }

  async function fetchProfile() {
    if (!token.value) return null
    try {
      const response = await api.get('/api/auth/profile')
      user.value = response.data
      return response.data
    } catch (error) {
      if (error.response?.status === 401) {
        logout()
      }
      throw error
    }
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }

  return { token, user, isAuthenticated, login, register, fetchProfile, logout }
})
