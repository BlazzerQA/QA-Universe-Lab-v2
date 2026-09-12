<template>
  <aside class="app-sidebar" :class="{ open }" data-testid="app-sidebar">
    <nav class="sidebar-nav" aria-label="Main">
      <router-link
        v-for="item in items"
        :key="item.id"
        :to="item.to"
        class="nav-link"
        exact-active-class="is-active"
        :data-testid="item.testId"
        @click="$emit('navigate')"
      >
        {{ t(item.labelKey) }}
      </router-link>
    </nav>
  </aside>
</template>

<script setup>
import { NAV_ITEMS } from '@/config/navigation'
import { useShellI18n } from '@/composables/useShellI18n'

defineProps({
  open: { type: Boolean, default: false }
})

defineEmits(['navigate'])

const items = NAV_ITEMS
const { t } = useShellI18n()
</script>

<style scoped>
.app-sidebar {
  width: var(--sidebar-w);
  flex-shrink: 0;
  height: calc(100vh - var(--header-h));
  position: sticky;
  top: var(--header-h);
  background: var(--panel);
  border-right: 1px solid var(--border);
  z-index: 40;
  overflow-y: auto;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 16px 12px;
}

.nav-link {
  display: block;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  color: var(--text);
  text-decoration: none;
  font-size: 0.95rem;
  font-weight: 500;
  border-left: 3px solid transparent;
  transition: background 0.15s ease, color 0.15s ease, border-color 0.15s ease;
}

.nav-link:hover {
  background: var(--primary-muted);
  color: var(--heading);
}

.nav-link.is-active {
  background: var(--primary-muted);
  color: var(--primary);
  font-weight: 700;
  border-left-color: var(--primary);
}

@media (max-width: 768px) {
  .app-sidebar {
    position: fixed;
    left: 0;
    top: var(--header-h);
    transform: translateX(-100%);
    transition: transform 0.2s ease;
    box-shadow: none;
  }

  .app-sidebar.open {
    transform: translateX(0);
    box-shadow: var(--shadow);
  }
}
</style>
