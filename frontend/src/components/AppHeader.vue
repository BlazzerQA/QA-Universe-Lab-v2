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
      <div class="dropdown-content ui-card">
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
  height: var(--header-h);
  background: var(--panel);
  backdrop-filter: blur(12px);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 48px;
  z-index: 1000;
  border-bottom: 1px solid var(--border);
  box-sizing: border-box;
}

.fixed-header h1 {
  margin: 0;
  font-size: 1.3rem;
  color: var(--heading);
  letter-spacing: 0.4px;
}

.logo-link {
  text-decoration: none;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropbtn {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 60px;
  padding: 8px 16px 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: border-color 0.2s ease, background 0.2s ease;
  min-width: 140px;
  white-space: nowrap;
}

.dropbtn:hover {
  background: var(--card-hover);
  border-color: var(--primary);
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--primary-hover));
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
  font-weight: 700;
  color: var(--heading);
  display: block;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  min-width: 20px;
}

.avatar-label {
  font-size: 11px;
  color: var(--muted);
  display: block;
}

.dropdown-arrow {
  font-size: 12px;
  color: var(--muted);
  transition: transform 0.2s;
}

.dropdown.open .dropdown-arrow {
  transform: rotate(180deg);
}

.dropdown-content {
  position: absolute;
  right: 0;
  min-width: 200px;
  z-index: 1;
  margin-top: 12px;
  padding: 8px;
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
  color: var(--text);
  padding: 12px 18px;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: 0.2s;
  border-radius: 8px;
  margin: 0;
}

.dropdown-content a:hover {
  background: var(--primary-muted);
  color: var(--primary);
}

@media (max-width: 768px) {
  .fixed-header {
    padding: 12px 18px;
  }
}
</style>
