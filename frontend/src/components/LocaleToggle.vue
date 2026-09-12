<template>
  <button
    type="button"
    class="shell-chip"
    :title="t('header.locale')"
    :aria-label="t('header.locale')"
    data-testid="locale-toggle"
    @click="localeStore.toggle()"
  >
    {{ localeStore.locale === 'ru' ? 'RU' : 'EN' }}
  </button>
</template>

<script setup>
import { useLocaleStore } from '@/stores/locale'
import { useShellI18n } from '@/composables/useShellI18n'

const localeStore = useLocaleStore()
const { t } = useShellI18n()
</script>
