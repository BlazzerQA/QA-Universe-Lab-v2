<template>
  <div class="app-shell" data-testid="app-shell">
    <AppHeader @toggle-sidebar="toggleSidebar" />
    <div class="app-shell-body">
      <AppSidebar :open="sidebarOpen" @navigate="closeSidebar" />
      <div class="app-shell-content">
        <router-view />
      </div>
    </div>
    <button
      v-if="sidebarOpen"
      type="button"
      class="app-shell-overlay"
      aria-label="Close menu"
      data-testid="sidebar-overlay"
      @click="closeSidebar"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import AppSidebar from '@/components/AppSidebar.vue'
import AppHeader from '@/components/AppHeader.vue'

const sidebarOpen = ref(false)

function toggleSidebar() {
  sidebarOpen.value = !sidebarOpen.value
}

function closeSidebar() {
  sidebarOpen.value = false
}

function onResize() {
  if (window.innerWidth > 768) closeSidebar()
}

onMounted(() => {
  window.addEventListener('resize', onResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
.app-shell {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--bg);
}

.app-shell-body {
  display: flex;
  flex: 1;
  min-width: 0;
  min-height: 0;
}

.app-shell-content {
  flex: 1;
  min-width: 0;
}

.app-shell-overlay {
  display: none;
}

@media (max-width: 768px) {
  .app-shell-overlay {
    display: block;
    position: fixed;
    top: var(--header-h);
    right: 0;
    bottom: 0;
    left: 0;
    border: 0;
    padding: 0;
    background: rgba(0, 0, 0, 0.45);
    z-index: 35;
    cursor: pointer;
  }
}
</style>
