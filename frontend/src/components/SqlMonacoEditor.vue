<template>
  <MonacoEditor
    language="sql"
    test-id="sql-editor"
    aria-label="SQL query editor"
    v-bind="$attrs"
    @run="$emit('run')"
  />
</template>

<script setup>
import MonacoEditor from '@/components/MonacoEditor.vue'

defineOptions({ inheritAttrs: false })
defineEmits(['run'])
</script>
