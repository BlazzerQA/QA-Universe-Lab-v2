<template>
  <div class="sql-sandbox-page ui-page" data-testid="sql-sandbox-page">
    <div class="ui-page-body">
      <h1 class="ui-title">{{ t('sql.title') }}</h1>
      <p class="ui-muted" data-testid="sql-sandbox-stub">{{ t('sql.stub') }}</p>
    </div>
  </div>
</template>

<script setup>
import { useShellI18n } from '@/composables/useShellI18n'

const { t } = useShellI18n()
</script>
