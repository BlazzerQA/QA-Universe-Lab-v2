<template>
  <header class="fixed-header">
    <router-link to="/main" class="logo-link" data-testid="app-logo">
      <h1 data-testid="app-header-title">QA Universe Lab</h1>
    </router-link>
    <div class="dropdown" :class="{ open: isOpen }" data-testid="header-dropdown">
      <button class="dropbtn" data-testid="header-menu" @click="toggleDropdown">
        <div class="avatar">
          <span class="avatar-icon">☕</span>
        </div>
        <div class="avatar-info">
          <span class="avatar-name">{{ userName || 'Пользователь' }}</span>
          <span class="avatar-label">Профиль</span>
        </div>
        <span class="dropdown-arrow">▼</span>
      </button>
      <div class="dropdown-content">
        <router-link to="/main" data-testid="nav-main">🏠 На главную</router-link>
        <router-link to="/profile" data-testid="nav-profile">👤 Мой профиль</router-link>
        <router-link to="/roadmap" data-testid="nav-roadmap">📋 Roadmap</router-link>
        <router-link to="/notes" data-testid="nav-notes">📝 Notes</router-link>
        <router-link to="/collections" data-testid="nav-collections">📚 Collections</router-link>
        <router-link to="/mocks" data-testid="nav-mocks">🧪 Mocks</router-link>
        <a href="#" data-testid="logout-link" @click.prevent="handleLogout">🚪 Выйти</a>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const router = useRouter()
const isOpen = ref(false)

const userName = computed(() => {
  if (authStore.user?.fullName) return authStore.user.fullName
  if (authStore.user?.phone) return authStore.user.phone
  return 'Пользователь'
})

function toggleDropdown() {
  isOpen.value = !isOpen.value
}

function closeDropdown() {
  isOpen.value = false
}

function handleLogout() {
  authStore.logout()
  router.push('/login')
}

function onDocumentClick(e) {
  const dropdown = e.target.closest('.dropdown')
  if (!dropdown) {
    closeDropdown()
  }
}

function onKeydown(e) {
  if (e.key === 'Escape') {
    closeDropdown()
  }
}

onMounted(() => {
  document.addEventListener('click', onDocumentClick)
  document.addEventListener('keydown', onKeydown)
  authStore.fetchProfile().catch(() => {})
})

onUnmounted(() => {
  document.removeEventListener('click', onDocumentClick)
  document.removeEventListener('keydown', onKeydown)
})
</script>

<style scoped>
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: rgba(18, 18, 18, 0.9);
  backdrop-filter: blur(10px);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 50px;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
  border-bottom: 1px solid rgba(0, 255, 65, 0.3);
  box-sizing: border-box;
}

.fixed-header h1 {
  margin: 0;
  font-size: 1.3rem;
  color: #00ff41;
  letter-spacing: 1px;
}

.logo-link {
  text-decoration: none;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropbtn {
  background: rgba(30, 30, 40, 0.45);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 60px;
  padding: 8px 16px 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s ease;
  min-width: 140px;
  white-space: nowrap;
}

.dropbtn:hover {
  background: rgba(30, 30, 40, 0.7);
  border-color: #00ff41;
  box-shadow: 0 0 10px rgba(0, 255, 65, 0.2);
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #00ff41, #0088cc);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.avatar-icon {
  font-size: 20px;
}

.avatar-info {
  text-align: left;
  min-width: 100px;
}

.avatar-name {
  font-size: 14px;
  font-weight: bold;
  color: #00ff41;
  display: block;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  min-width: 20px;
}

.avatar-label {
  font-size: 11px;
  color: #aaa;
  display: block;
}

.dropdown-arrow {
  font-size: 12px;
  color: #aaa;
  transition: transform 0.2s;
}

.dropdown.open .dropdown-arrow {
  transform: rotate(180deg);
}

.dropdown-content {
  position: absolute;
  right: 0;
  background-color: #1e1e1e;
  min-width: 200px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  z-index: 1;
  border-radius: 12px;
  border: 1px solid #333;
  margin-top: 12px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: opacity 0.2s ease, visibility 0.2s ease, transform 0.2s ease;
}

.dropdown.open .dropdown-content {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-content a {
  color: #e0e0e0;
  padding: 12px 18px;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: 0.2s;
  border-radius: 8px;
  margin: 4px 8px;
}

.dropdown-content a:hover {
  background-color: #2a2a2a;
  color: #00ff41;
}

@media (max-width: 768px) {
  .fixed-header {
    padding: 12px 18px;
  }
}
</style>
