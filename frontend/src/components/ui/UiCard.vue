<template>
  <div class="ui-card" :class="{ 'ui-card--accent': accent, 'ui-card--hover': hover }">
    <slot />
  </div>
</template>

<script setup>
defineProps({
  accent: { type: Boolean, default: false },
  hover: { type: Boolean, default: false }
})
</script>
