<template>
  <button
    type="button"
    class="shell-chip"
    :title="label"
    :aria-label="label"
    data-testid="theme-toggle"
    @click="themeStore.toggle()"
  >
    <svg v-if="themeStore.theme === 'dark'" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
      <path d="M21 14.3A9 9 0 1 1 9.7 3 7 7 0 0 0 21 14.3z" />
    </svg>
    <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
      <path
        d="M12 4.5a1 1 0 0 1 1 1V7a1 1 0 1 1-2 0V5.5a1 1 0 0 1 1-1zm0 11a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7zm7.5-4.5a1 1 0 0 1-1 1H17a1 1 0 1 1 0-2h1.5a1 1 0 0 1 1 1zM7 12a1 1 0 0 1-1 1H4.5a1 1 0 1 1 0-2H6a1 1 0 0 1 1 1zm10.6 5.6a1 1 0 0 1-1.42 0l-1.06-1.06a1 1 0 1 1 1.42-1.42l1.06 1.06a1 1 0 0 1 0 1.42zM8.88 8.88a1 1 0 0 1-1.42 0L6.4 7.82A1 1 0 0 1 7.82 6.4L8.88 7.46a1 1 0 0 1 0 1.42zm0 6.24a1 1 0 0 1 0 1.42L7.82 17.6A1 1 0 1 1 6.4 16.18l1.06-1.06a1 1 0 0 1 1.42 0zM17.6 6.4a1 1 0 0 1 0 1.42l-1.06 1.06a1 1 0 1 1-1.42-1.42L16.18 6.4A1 1 0 0 1 17.6 6.4zM12 17a1 1 0 0 1 1 1v1.5a1 1 0 1 1-2 0V18a1 1 0 0 1 1-1z"
      />
    </svg>
  </button>
</template>

<script setup>
import { computed } from 'vue'
import { useThemeStore } from '@/stores/theme'
import { useShellI18n } from '@/composables/useShellI18n'

const themeStore = useThemeStore()
const { t } = useShellI18n()
const label = computed(() =>
  themeStore.theme === 'dark' ? t('header.themeLight') : t('header.themeDark')
)
</script>
