<template>
  <div class="user-menu" :class="{ open: isOpen }" data-testid="header-dropdown">
    <button
      type="button"
      class="user-btn"
      data-testid="header-menu"
      :aria-expanded="isOpen"
      @click="toggle"
    >
      <span class="avatar" aria-hidden="true">☕</span>
      <span class="user-meta">
        <span class="user-name">{{ userName }}</span>
        <span class="user-label">{{ t('header.profile') }}</span>
      </span>
      <span class="caret" aria-hidden="true">▼</span>
    </button>
    <div class="user-panel ui-card">
      <router-link to="/profile" data-testid="nav-profile" @click="close">
        {{ t('header.profile') }}
      </router-link>
      <a href="#" data-testid="logout-link" @click.prevent="logout">{{ t('header.logout') }}</a>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useShellI18n } from '@/composables/useShellI18n'

const authStore = useAuthStore()
const router = useRouter()
const { t } = useShellI18n()
const isOpen = ref(false)

const userName = computed(() => {
  if (authStore.user?.fullName) return authStore.user.fullName
  if (authStore.user?.phone) return authStore.user.phone
  return t('user.fallback')
})

function toggle() {
  isOpen.value = !isOpen.value
}

function close() {
  isOpen.value = false
}

function logout() {
  close()
  authStore.logout()
  router.push('/login')
}

function onDocumentClick(e) {
  if (!e.target.closest('.user-menu')) {
    close()
  }
}

function onKeydown(e) {
  if (e.key === 'Escape') close()
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
.user-menu {
  position: relative;
}

.user-btn {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 60px;
  padding: 6px 14px 6px 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 140px;
  color: inherit;
}

.user-btn:hover {
  background: var(--card-hover);
  border-color: var(--primary);
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--primary-hover));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.user-meta {
  text-align: left;
  min-width: 88px;
}

.user-name {
  display: block;
  font-size: 14px;
  font-weight: 700;
  color: var(--heading);
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-label {
  display: block;
  font-size: 11px;
  color: var(--muted);
}

.caret {
  font-size: 11px;
  color: var(--muted);
  transition: transform 0.2s;
}

.user-menu.open .caret {
  transform: rotate(180deg);
}

.user-panel {
  position: absolute;
  right: 0;
  min-width: 180px;
  margin-top: 10px;
  padding: 8px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-8px);
  transition: opacity 0.2s ease, visibility 0.2s ease, transform 0.2s ease;
  z-index: 20;
}

.user-menu.open .user-panel {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.user-panel a {
  color: var(--text);
  padding: 10px 14px;
  text-decoration: none;
  display: block;
  border-radius: 8px;
}

.user-panel a:hover {
  background: var(--primary-muted);
  color: var(--primary);
}

@media (max-width: 768px) {
  .user-meta,
  .caret {
    display: none;
  }

  .user-btn {
    min-width: 0;
    padding: 4px;
  }
}
</style>
