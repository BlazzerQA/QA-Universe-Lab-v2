<template>
  <header class="app-header" data-testid="app-header">
    <button
      type="button"
      class="menu-toggle shell-chip"
      data-testid="sidebar-toggle"
      :aria-label="t('header.menu')"
      @click="$emit('toggle-sidebar')"
    >
      ☰
    </button>
    <router-link to="/main" class="logo-link" data-testid="app-logo">
      <h1 data-testid="app-header-title">QA Universe Lab</h1>
    </router-link>
    <div class="header-actions">
      <ThemeToggle />
      <LocaleToggle />
      <UserMenu />
    </div>
  </header>
</template>

<script setup>
import ThemeToggle from '@/components/ThemeToggle.vue'
import LocaleToggle from '@/components/LocaleToggle.vue'
import UserMenu from '@/components/UserMenu.vue'
import { useShellI18n } from '@/composables/useShellI18n'

defineEmits(['toggle-sidebar'])

const { t } = useShellI18n()
</script>

<style scoped>
.app-header {
  height: var(--header-h);
  background: var(--panel);
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 30;
  box-sizing: border-box;
}

.app-header h1 {
  margin: 0;
  font-size: 1.2rem;
  color: var(--heading);
  letter-spacing: 0.3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  min-width: 0;
}

.logo-link {
  display: block;
  text-decoration: none;
  min-width: 0;
  flex: 1;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
  flex-shrink: 0;
}

.menu-toggle {
  display: none;
  font-size: 1.15rem;
  padding: 8px 12px;
}

@media (max-width: 768px) {
  .app-header {
    padding: 0 12px;
    gap: 8px;
  }

  .app-header h1 {
    font-size: 0.95rem;
  }

  .menu-toggle {
    display: inline-flex;
  }

  .header-actions {
    gap: 6px;
  }
}
</style>
