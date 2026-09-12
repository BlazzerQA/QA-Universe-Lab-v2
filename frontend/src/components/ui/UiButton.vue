<template>
  <button
    class="ui-btn"
    :class="[`ui-btn--${variant}`, { 'ui-btn--block': block }]"
    :type="type"
    :disabled="disabled"
  >
    <slot />
  </button>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'secondary', 'danger', 'ghost'].includes(value)
  },
  type: { type: String, default: 'button' },
  disabled: { type: Boolean, default: false },
  block: { type: Boolean, default: false }
})
</script>
