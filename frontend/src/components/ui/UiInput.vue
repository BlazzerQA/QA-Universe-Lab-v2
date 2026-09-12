<template>
  <div class="ui-field">
    <label v-if="label" class="ui-label" :for="inputId">{{ label }}</label>
    <input
      :id="inputId"
      class="ui-input"
      :class="{ 'ui-input--error': invalid || !!error }"
      :type="type"
      :value="modelValue"
      v-bind="$attrs"
      @input="$emit('update:modelValue', $event.target.value)"
    />
    <p v-if="error" class="ui-hint ui-hint--error" :id="errorId" :data-testid="errorTestid">{{ error }}</p>
  </div>
</template>

<script setup>
defineOptions({ inheritAttrs: false })

defineProps({
  modelValue: { type: [String, Number], default: '' },
  label: { type: String, default: '' },
  type: { type: String, default: 'text' },
  error: { type: String, default: '' },
  errorTestid: { type: String, default: undefined },
  errorId: { type: String, default: undefined },
  invalid: { type: Boolean, default: false },
  inputId: { type: String, default: undefined }
})

defineEmits(['update:modelValue'])
</script>
