<template>
  <div class="ui-tabs" role="tablist">
    <button
      v-for="tab in tabs"
      :id="tab.buttonId"
      :key="tab.id"
      type="button"
      class="ui-tab"
      :class="{ 'is-active': modelValue === tab.id }"
      role="tab"
      :aria-selected="modelValue === tab.id"
      :data-testid="tab.testid"
      @click="$emit('update:modelValue', tab.id)"
    >
      {{ tab.label }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: String, required: true },
  tabs: {
    type: Array,
    required: true
  }
})

defineEmits(['update:modelValue'])
</script>
