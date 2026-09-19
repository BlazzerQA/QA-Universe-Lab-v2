<template>
  <div
    ref="host"
    class="qa-monaco"
    :style="{ height }"
    :data-testid="testId"
    role="textbox"
    :aria-label="ariaLabel"
  />
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useThemeStore } from '@/stores/theme'
import '@/lib/monacoEnv'
import * as monaco from 'monaco-editor/editor/editor.api'

const props = defineProps({
  modelValue: { type: String, default: '' },
  language: { type: String, default: 'plaintext' },
  disabled: { type: Boolean, default: false },
  height: { type: String, default: '220px' },
  testId: { type: String, default: 'monaco-editor' },
  ariaLabel: { type: String, default: 'Code editor' }
})

const emit = defineEmits(['update:modelValue', 'run'])

const host = ref(null)
let editor = null

const themeStore = useThemeStore()
const { theme } = storeToRefs(themeStore)

function monacoTheme() {
  return theme.value === 'light' ? 'qa-light' : 'qa-dark'
}

function defineThemes() {
  monaco.editor.defineTheme('qa-dark', {
    base: 'vs-dark',
    inherit: true,
    rules: [],
    colors: {
      'editor.background': '#0d1524',
      'editor.foreground': '#e8eef7',
      'editorLineNumber.foreground': '#64748b',
      'editorLineNumber.activeForeground': '#94a3b8',
      'editor.selectionBackground': '#3b82f640',
      'editor.lineHighlightBackground': '#16203380',
      'focusBorder': '#3b82f6'
    }
  })
  monaco.editor.defineTheme('qa-light', {
    base: 'vs',
    inherit: true,
    rules: [],
    colors: {
      'editor.background': '#ffffff',
      'editor.foreground': '#1e293b',
      'editorLineNumber.foreground': '#94a3b8',
      'editorLineNumber.activeForeground': '#64748b',
      'editor.selectionBackground': '#2563eb33',
      'editor.lineHighlightBackground': '#f8fafc',
      'focusBorder': '#2563eb'
    }
  })
}

onMounted(() => {
  defineThemes()
  editor = monaco.editor.create(host.value, {
    value: props.modelValue,
    language: props.language,
    theme: monacoTheme(),
    automaticLayout: true,
    minimap: { enabled: false },
    fontSize: 14,
    fontFamily: "SFMono-Regular, Consolas, 'Liberation Mono', Menlo, monospace",
    lineNumbers: 'on',
    scrollBeyondLastLine: false,
    wordWrap: 'on',
    tabSize: 2,
    padding: { top: 8, bottom: 8 },
    readOnly: props.disabled,
    fixedOverflowWidgets: true,
    accessibilitySupport: 'on'
  })

  editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter, () => emit('run'))

  editor.onDidChangeModelContent(() => {
    const value = editor.getValue()
    if (value !== props.modelValue) {
      emit('update:modelValue', value)
    }
  })
})

watch(
  () => props.modelValue,
  (value) => {
    if (!editor) return
    const next = value ?? ''
    if (editor.getValue() !== next) {
      editor.setValue(next)
    }
  }
)

watch(
  () => props.disabled,
  (disabled) => {
    editor?.updateOptions({ readOnly: disabled })
  }
)

watch(theme, () => {
  monaco.editor.setTheme(monacoTheme())
})

onBeforeUnmount(() => {
  editor?.dispose()
  editor = null
})

defineExpose({
  layout() {
    editor?.layout()
  }
})
</script>

<style scoped>
.qa-monaco {
  margin-bottom: 0.85rem;
  overflow: hidden;
  border: 1px solid var(--input-border);
  border-radius: var(--radius-sm);
}

.qa-monaco:focus-within {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--focus-ring);
}
</style>
